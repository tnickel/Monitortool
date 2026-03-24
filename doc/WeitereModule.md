# Weitere Module

## TradeMonitor

**Zweck:** Separater SWT-basierter Trade-Monitor mit Server-Anbindung.

**Einstiegspunkt:** `StartTradeMonitor.StartSwtTradeMonitor.main()`

**Paketstruktur:**
```
TradeMonitor/src/
├── StartTradeMonitor/
│   ├── StartSwtTradeMonitor.java  # Einstiegspunkt
│   └── TradeMonitorWork.java      # Business-Logik
├── SwtFenter/
│   └── SwtSMConfig.java          # SWT-Konfigurationsfenster
├── data/
│   ├── SMglobalConfig.java       # Globale Konfiguration
│   ├── ServerTradeliste.java     # Server-Tradeliste
│   └── ServerTradelisten.java    # Mehrere Server-Tradelisten
└── com/cloudgarden/resource/
    └── SWTResourceManager.java   # SWT-Ressourcen
```

Konfiguriert sich über eine `.start`-Datei (z.B. `Strato01___h2177083___6806___Pepper50___.start`), die Server, Account und Port enthält.

---

## Dropboxcopy

**Zweck:** Synchronisationstool für Trading-Daten zwischen verschiedenen Broker-Accounts über Dropbox-ähnliche Mechanismen.

**Einstiegspunkt:** `StartFrame.StartDropBoxCopy.main()`

**Struktur (17 Java-Dateien):**
- `StartFrame/` – Start und Business-Logik
- `data/` – Konfiguration, Magic-Listen, Globale Variablen
- `dia/` – Dialoge
- `droptool/` – Dropbox-Distributionswerkzeug
- `ftool/` – File-Tools (CheckUser, DropFile, DropUpdater)
- `swtDropwindow/` – SWT-Konfigurationsfenster

---

## GbAnalyser

**Zweck:** Spezial-Analyser für Gewinnbereich-Analyse.

**Einstiegspunkt:** `StartFrame.StartGbAnalyser.main()`

**Struktur (4 Java-Dateien):**
- `StartGbAnalyser.java` – Einstiegspunkt
- `StartGbAnaylserWork.java` – Business-Logik
- `EditGbAnalyserConfig.java` – Konfiguration

---

## WalkforwardCluster

**Zweck:** Walk-Forward-Analyse kombiniert mit Cluster-Bildung zur Validierung von Strategien.

**Einstiegspunkt:** `mainPackage.MainClass.main()`

**Struktur (4 Java-Dateien):**
- `MainClass.java` – Einstiegspunkt
- `SWTmainApp.java` – SWT-Anwendung
- `FileRequester.java` – Datei-Anfragen

---

## LotsizeDemo

**Zweck:** Demo-Anwendung zur Visualisierung und Konfiguration von Lotsize-Einstellungen für Expert Advisors.

**Struktur (8 Java-Dateien):**
- `Panels/` – EA-Konfigurationspanels
- `hilfsklasse/` – Hilfsklassen (GC, Tracer, UI, Chart-Demo)
- `start.java` – Einstiegspunkt

---

## Kurstool

**Zweck:** Werkzeug zur Verarbeitung und Kompression von Kursdaten.

**Einstiegspunkt:** `main.java`

**Struktur (5 Java-Dateien):**
- `main.java` – Einstiegspunkt
- `tools/Composer.java` – Daten-Zusammenstellung
- `tools/Kurs1.java` – Einzelner Kursdatensatz
- `tools/Kursliste.java` – Kursdaten-Liste
- `tools/Kursworker.java` – Kurs-Verarbeitung

---

## Guittest

**Zweck:** Einfacher SWT-GUI-Test.

**Struktur:** Nur `ScrollerTest.java`

---

## Test

**Zweck:** Test-Modul mit Chart-Prototypen.

**Struktur (4 Java-Dateien):**
- `Frame1.java` – Test-Frame
- `PriceVolumeDemo1.java` – Preis-Volumen-Chart Demo
- `PriceVolumeDemo2.java` – Preis-Volumen-Chart Demo v2
- `v1/NewComposite.java` – SWT Composite Test

---

## Libs

**Zweck:** Enthält externe JAR-Bibliotheken als Abhängigkeiten.

---

## skripts

**Zweck:** Skripte zum Abrufen neuer EAs.

**Struktur:** Verzeichnis `getnewEas/`
