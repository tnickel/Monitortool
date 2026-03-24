# MonitorTool – Haupt-Überwachungs-GUI

## Zweck
Das **MonitorTool** ist die zentrale Desktop-Anwendung zur Echtzeitüberwachung von Forex-Trading-Aktivitäten. Es bietet eine umfassende Oberfläche zur Verwaltung von Broker-Verbindungen, Trade-Überwachung und Gewinnvisualisierung.

## Technologie
- **GUI-Framework:** Eclipse SWT
- **Layout:** FormLayout-basiertes Panel-System
- **Architektur:** Event-Bus Pattern für Panel-Kommunikation

## Einstiegspunkt
```
StartFrame.StartMonitor.main(String[] args)
```
Das erste Argument kann ein benutzerdefiniertes Arbeitsverzeichnis sein.

## Architektur

### Panel-System
Die Anwendung ist in funktionale Panels aufgeteilt, die über einen **EventBus** kommunizieren:

| Panel | Klasse | Funktion |
|-------|--------|----------|
| Controls | `ControlsPanel` | Steuerungselemente (oben) |
| Trade Management | `TradeManagementPanel` | Trade-Verwaltung (links) |
| Broker Management | `BrokerManagementPanel` | Broker-Verwaltung (rechts) |
| Profit Display | `ProfitDisplayPanel` | Gewinn-Anzeige (unten) |

### Event-Bus (`StartMonitorEventBus`)
- `BrokerSelectionListener` – Reagiert auf Broker-Auswahl
- `TradeSelectionListener` – Reagiert auf Trade-Auswahl  
- `ProfitUpdateListener` – Aktualisiert Gewinn-Daten

### Manager
- `MenuManager` – Verwaltet die Menüstruktur der Shell
- `StartMonitorConfig` – Konfigurationsmanagement

## Paketstruktur

```
MonitorTool/src/
├── StartFrame/           # Hauptfenster & Startup-Logik
│   ├── StartMonitor.java         # Hauptklasse
│   ├── StartMonitorWork.java     # Business-Logik Worker
│   ├── AutomaticWork.java        # Automatisierte Abläufe
│   ├── Brokerview.java           # Broker-Ansicht
│   ├── Tableview.java            # Tabellen-Ansicht
│   ├── config/                   # Konfiguration
│   ├── events/                   # Event-Bus & Datenklassen
│   ├── managers/                 # Menu-Manager
│   └── panels/                   # UI-Panels
├── Panels/               # EA-Konfigurationspanels
├── Sync/                 # Lock-Mechanismen für Tradelisten
├── aut/                  # Automatische Aktionen (z.B. FridayClose)
├── charttool/            # Chart-Visualisierungen
│   ├── ProfitPanel.java          # Gewinn-Charts
│   ├── HourChart.java            # Stunden-basierte Charts
│   ├── WeekdayChart.java         # Wochentag-Charts
│   ├── ShowAllMetrics.java       # Metriken-Übersicht
│   └── ShowAllProfits.java       # Gewinn-Übersicht
├── data/                 # Datenmodelle
│   ├── Ea.java                   # Expert Advisor Datenklasse
│   ├── Ealiste.java              # EA-Liste
│   ├── GlobalVar.java            # Globale Variablen
│   ├── Trade.java / Tradeliste   # HTML-Trade-Daten
│   └── Historyexporter.java      # History-Export
├── network/              # Netzwerk-Kommunikation
└── tools/                # Hilfswerkzeuge & Utilities
```

## Kernfunktionen

1. **Broker-Verwaltung** – Mehrere Broker-Accounts verwalten und überwachen
2. **Trade-Monitoring** – Offene und geschlossene Trades in Echtzeit anzeigen
3. **Gewinn-Tracking** – Gewinnentwicklung über verschiedene Zeiträume visualisieren
4. **EA-Konfiguration** – Expert Advisors konfigurieren und deren Status überwachen
5. **Chart-Analyse** – Verschiedene Charttypen (Stunden, Wochentage, Gesamtgewinn)
6. **Auto-Aktionen** – Automatische Aktionen wie FridayClose
7. **History-Export** – Trading-History exportieren
