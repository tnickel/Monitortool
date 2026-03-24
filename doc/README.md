# Monitortool – Projektdokumentation

## Überblick

**Monitortool** ist eine modulare Java-Desktop-Anwendung zur **automatisierten Überwachung, Analyse und Optimierung von Forex-Trading-Strategien** (Expert Advisors / EAs). Das Projekt besteht aus ca. 16 Eclipse-Teilprojekten, die über ein gemeinsames Workspace zusammenarbeiten.

**Lizenz:** GNU General Public License v3 (GPLv3)

---

## Architektur

```
┌─────────────────────────────────────────────────────────┐
│                    MonitorTool (SWT)                     │
│            Haupt-GUI: Trading-Überwachung               │
├──────────────┬──────────────┬───────────────────────────┤
│ TradeMonitor │   Server     │      Analyser             │
│  (SWT)       │  (Swing)     │  (Analyse-Engine)         │
├──────────────┴──────────────┴───────────────────────────┤
│                      CommonLib                          │
│         Gemeinsame Datenklassen & Hilfsfunktionen       │
├─────────────────────────────────────────────────────────┤
│  Optimizer │ Toolbox │ Metrikanalyser │ WalkforwardCluster│
│  Kurstool  │ GbAnalyser │ LotsizeDemo │ Dropboxcopy     │
└─────────────────────────────────────────────────────────┘
```

---

## Module

| Modul | Typ | Java-Dateien | Beschreibung |
|-------|-----|-------------|--------------|
| **MonitorTool** | SWT Desktop-App | 114 | Haupt-GUI zur Echtzeit-Überwachung von Brokern, Trades und Gewinnen |
| **Server** | Swing Desktop-App | 12 | Netzwerk-Server für Client-Verbindungen (Socket-basiert) |
| **TradeMonitor** | SWT Desktop-App | 7 | Separater Trade-Monitor mit Server-Anbindung |
| **Analyser** | Bibliothek/App | 145 | Kern-Analyse-Engine: Threading, Datenbank, Charts, Filter |
| **CommonLib** | Bibliothek | 37 | Gemeinsame Klassen: `Trade`, `Tradeliste`, Dateizugriff, Tools |
| **Optimizer** | Swing Desktop-App | 40 | Optimierung von Trading-Algorithmen und Indikatoren |
| **Metrikanalyser** | SWT Desktop-App | 31 | Metrik-Analyse mit Weka Machine Learning Integration |
| **Toolbox** | Swing Desktop-App | 48 | SQ4x-Workflows, Cluster-Analyse, XML-Parser |
| **Dropboxcopy** | SWT Desktop-App | 17 | Datei-Synchronisation zwischen Broker-Accounts |
| **GbAnalyser** | SWT Desktop-App | 4 | Spezial-Analyser (Gewinn-Bereich) |
| **WalkforwardCluster** | SWT Desktop-App | 4 | Walk-Forward-Analyse mit Cluster-Bildung |
| **LotsizeDemo** | Demo/Util | 8 | Demo für Lotsize-Konfiguration und EA-Einstellungen |
| **Kurstool** | Util | 5 | Kurs-Datenverarbeitung und -Kompression |
| **Guittest** | Test | 1 | SWT GUI-Test (Scroller) |
| **Test** | Test | 4 | Test-Diagramme und Composites |
| **Libs** | Bibliotheken | – | Externe JAR-Abhängigkeiten |

---

## Technologie-Stack

| Technologie | Verwendung |
|-------------|------------|
| **Java SE** | Kernsprache für alle Module |
| **Eclipse SWT** | GUI-Framework (MonitorTool, TradeMonitor, Metrikanalyser, u.a.) |
| **Java Swing** | GUI-Framework (Server, Optimizer, Toolbox) |
| **JFreeChart** | Chart-Visualisierung (Profit-Charts, Kurs-Charts) |
| **Weka** | Machine Learning für Metrik-Analyse |
| **XStream** | XML-Serialisierung |
| **Eclipse IDE** | Entwicklungsumgebung (`.classpath`, `.project` Dateien) |

---

## Detaillierte Modul-Dokumentation

Für jedes Modul gibt es eine eigene Dokumentationsdatei:

- [MonitorTool](MonitorTool.md) – Haupt-Überwachungs-GUI
- [Server](Server.md) – Netzwerk-Server
- [Analyser](Analyser.md) – Kern-Analyse-Engine
- [CommonLib](CommonLib.md) – Gemeinsame Bibliothek
- [Optimizer](Optimizer.md) – Strategie-Optimierung
- [Metrikanalyser](Metrikanalyser.md) – ML-basierte Metrik-Analyse
- [Toolbox](Toolbox.md) – SQ4x-Workflows & Tools
- [Weitere Module](WeitereModule.md) – TradeMonitor, Dropboxcopy, GbAnalyser, u.a.

---

## Projekt einrichten

1. **Eclipse IDE** installieren (mit SWT-Unterstützung)
2. Repository klonen: `git clone <repository-url>`
3. Alle Teilprojekte als Eclipse-Projekte importieren
4. `Libs`-Verzeichnis enthält die benötigten externen JARs
5. `CommonLib` muss als Dependency in den abhängigen Projekten konfiguriert sein

## Starten

| Anwendung | Einstiegspunkt |
|-----------|----------------|
| MonitorTool | `StartFrame.StartMonitor.main()` |
| Server | `Main.StartServer.main()` |
| TradeMonitor | `StartTradeMonitor.StartSwtTradeMonitor.main()` |
| Optimizer | `Start.StartOptimizer.main()` |
| Metrikanalyser | `Main.NewSWTApp.main()` |
| Toolbox | `start.StartToolbox.main()` |
| Dropboxcopy | `StartFrame.StartDropBoxCopy.main()` |
