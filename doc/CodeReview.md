# Code Review – Monitortool

**Datum:** 24.03.2026  
**Scope:** Alle Module, Schwerpunkt `MonitorTool`, `Server`, `CommonLib`

---

## 🔴 Kritische Probleme

### 1. Hardcodierter Verschlüsselungsschlüssel
**Datei:** `MonitorTool/src/StartFrame/StartMonitorWork.java` (Zeile ~540)
```java
crypto.encryptFile(fnamzip2, datenfilenamecr, "f15q8t93");
```
Der Verschlüsselungsschlüssel steht im Klartext im Quellcode. Jeder mit Zugang zum Repository kann alle verschlüsselten Tradelisten entschlüsseln.

**Empfehlung:** Schlüssel aus Umgebungsvariable oder verschlüsseltem Keystore lesen.

---

### 2. AES/ECB-Modus (unsicher)
**Datei:** `MonitorTool/src/network/Crypto.java`
```java
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
```
ECB-Modus verschlüsselt identische Blöcke identisch – Muster bleiben erkennbar.

**Empfehlung:** `AES/CBC/PKCS5Padding` oder `AES/GCM/NoPadding` verwenden + zufälligen IV generieren.

---

### 3. Myfxbook-Passwort im Klartext gespeichert
**Datei:** `MonitorTool/src/data/GlobalVar.java`
```java
public static String getMyfxbookpassword() { return gc.getMyfxbookpassword(); }
public static void setMyfxbookpassword(String password) { gc.setMyfxbookpassword(password); }
```
Das Passwort wird über `Inf.saveXST()` (XStream XML) im Klartext in `config.xml` gespeichert.

**Empfehlung:** Passwort verschlüsselt speichern oder Credential-Manager nutzen.

---

### 4. Integer-Overflow in FridayClose-Timer
**Datei:** `MonitorTool/src/aut/FridayClose.java`
```java
long diffsec_ms = (timefriday - timeakt);
if (diffsec_ms < 0)
    diffsec_ms = diffsec_ms + (60*60*24*7*1000);
Display.getDefault().timerExec((int)diffsec_ms, fridaytimer);
```
`diffsec_ms` kann bis zu ~604.800.000ms (7 Tage) betragen. Das passt in einen `int`, aber der Cast `(int)diffsec_ms` ist fragil. Sollte der Wert doch `> Integer.MAX_VALUE` werden (z.B. durch einen Timezone-Bug), entsteht ein Overflow und der Timer startet sofort oder mit negativem Wert.

**Empfehlung:** Explizite Prüfung auf `Integer.MAX_VALUE` und Fallback-Logik.

---

## 🟠 Architektur-Probleme

### 5. God Object: `GlobalVar`
**Datei:** `MonitorTool/src/data/GlobalVar.java` (~280 Zeilen)

`GlobalVar` ist eine statische Klasse mit ~50 Getter/Setter für völlig unterschiedliche Belange:
- Netzwerk-IPs (`serverip1`, `serverip2`)
- Myfxbook-Login
- MetaTrader-Konfiguration
- Lizenz-Informationen
- Update-Channel
- Sortier-Kriterien

**Empfehlung:** Aufteilen in `NetworkConfig`, `TradeConfig`, `UserConfig`, `LicenseConfig`.

---

### 6. File-basiertes Locking mit Busy-Wait
**Datei:** `MonitorTool/src/Sync/LockTradeliste.java`
```java
while (locktrade.exists() == true) {
    Thread.sleep(1000);
    if (locktrade.exists() == true)
        continue;
}
```
- **Kein Timeout** → Endlosschleife möglich wenn Lock-Datei nicht gelöscht wird
- **Redundante Prüfung** → `if (locktrade.exists()) continue;` ist doppelt zur While-Bedingung
- **Keine Fehlerbehandlung** → Wenn Datei-Rechte fehlen, hängt Programm ewig

**Empfehlung:** `java.nio.file.FileLock` oder `java.util.concurrent.locks` verwenden. Timeout einbauen.

---

### 7. Methoden mit exzessiven Parametern
**Datei:** `MonitorTool/src/StartFrame/StartMonitorWork.java`
```java
public void loadallbroker(Display dis, Table table1, Table table2, Table table3, 
    Tradefilter tf, Text anzincommingtrades, Text anzeas, 
    org.eclipse.swt.widgets.Label broker, int showflag, int forceloadflag,
    int onlyopenflag, Text prof7, Text prof30, Text profall, boolean onlytodayflag)
```
**15 Parameter** → schwer lesbar, fehleranfällig, nicht testbar.

**Empfehlung:** Parameter-Objekt (z.B. `LoadBrokerRequest`) einführen.

---

## 🟡 Code-Qualität

### 8. `int` statt `boolean` für Flags
An vielen Stellen werden `int`-Werte als Flags verwendet:
```java
int forceflag     // 0 oder 1
int showflag      // 0 oder 1
int onlyopenflag  // 0 oder 1
```
**Empfehlung:** `boolean` verwenden für Ja/Nein-Werte. Enums für mehrwertige Flags.

---

### 9. Magic Number `32` statt `SWT.OK`
**Datei:** `StartMonitorWork.java`
```java
if (dialog.open() != 32)
    return;
```
`32` ist der numerische Wert von `SWT.OK`, aber sollte als Konstante referenziert werden.

---

### 10. Deprecated Code (`_dep`) noch in Verwendung
15 Dateien enthalten `_dep`-suffigierte Methoden/Klassen, die als deprecated markiert aber noch aufgerufen werden:

| Datei | Beispiel |
|-------|----------|
| `AutomaticCheck_dep.java` | `runAutomatic_dep()` wird von `checkGd20_dep()` aufgerufen |
| `Tradelogic_dep.java` | Weiterhin referenziert |
| `Networker_dep.java` | Weiterhin im Classpath |
| `Dialog_dep.java` | Weiterhin referenziert |
| `CopyOfStartMonitor.java` | Komplette Dateikopie als "Backup" |

**Empfehlung:** Deprecated Code entfernen oder durch neue Implementierung ersetzen.

---

### 11. 95+ TODO-Kommentare (unerledigte Arbeit)
Allein im `MonitorTool` gibt es **95+ TODO-Kommentare**, die meisten sind auto-generierte `// TODO Auto-generated catch block` oder `// TODO add your code for...`.

Top-Dateien:
- `SwtEditBrokerConfig.java` – 20+ TODOs
- `TradeFilterConfig.java` – 12+ TODOs
- `SwtSetSortCriteria.java` – 12+ TODOs

---

### 12. Generisches Exception-Handling
**19+ Dateien** fangen nur `catch (Exception e)` mit `e.printStackTrace()`:
```java
} catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
```
Fehler werden geschluckt, nicht geloggt und nicht an den Benutzer weitergegeben.

**Empfehlung:** Spezifische Exceptions fangen, über `Tracer.WriteTrace()` loggen, dem User ggf. eine Meldung zeigen.

---

### 13. Inkonsistente Namenskonventionen

| Problem | Beispiel |
|---------|----------|
| Paketnamen beginnen mit Großbuchstaben | `StartFrame`, `Sync`, `Panels` |
| Methoden beginnen mit Großbuchstaben | `LoadBrokerTable()`, `ShowBrokerTable()`, `CalcProfitTable()` |
| Tippfehler in Methoden/Variablennamen | `tooggleallprofits`, `toogleOnOff`, `Verzeichniss`, `Gesammt` |
| Gemischte Sprachen (Deutsch/Englisch) | `getMqlquellverz()`, `holeStrFilename()`, `Gewinnliste` |
| `glob_`-Präfix statt `this.` | `glob_selectedBroker`, `display_glob`, `pb1_glob` |

**Empfehlung:** Java-Namenskonventionen einhalten (Pakete lowercase, Methoden camelCase).

---

### 14. Windows-spezifische Pfade hardcodiert
```java
dialog.setFilterPath(glob_selectedBrokerShare + "\\experts");
userdir = userdir.substring(0, userdir.lastIndexOf("\\"));
```
Backslashes (`\\`) sind hardcodiert statt `File.separator` oder `Path` zu verwenden.

**Empfehlung:** `java.nio.file.Path` oder `File.separator` nutzen.

---

### 15. Leere/Stub-Klassen
**Datei:** `MonitorTool/src/StartFrame/AutomaticWork.java`
```java
public class AutomaticWork {
  ///
}
```
Komplett leere Klasse ohne Inhalt.

---

## 📊 Zusammenfassung

| Kategorie | Anzahl | Priorität |
|-----------|--------|-----------|
| 🔴 Sicherheitsprobleme | 3 | Sofort beheben |
| 🔴 Potenzielle Bugs | 1 | Sofort beheben |
| 🟠 Architektur-Probleme | 3 | Mittelfristig |
| 🟡 Code-Qualität | 8 | Langfristig |

### Empfohlene Reihenfolge
1. **Sofort:** Verschlüsselungsschlüssel externalisieren, AES/ECB → AES/GCM, Passwort-Handling
2. **Kurzfristig:** FridayClose Integer-Overflow, Magic Number 32 → SWT.OK, Timeout im Locking
3. **Mittelfristig:** GlobalVar aufteilen, Parameter-Objekte, `_dep` Code entfernen
4. **Langfristig:** Namenskonventionen, TODO-Bereinigung, Exception-Handling
