# CommonLib – Gemeinsame Bibliothek

## Zweck
**CommonLib** stellt gemeinsam genutzte Klassen bereit, die von allen anderen Modulen als Abhängigkeit verwendet werden. Es enthält grundlegende Datenmodelle, Hilfsklassen und Werkzeuge.

## Paketstruktur

```
CommonLib/src/
├── ComData/              # Kern-Datenklassen
│   ├── Trade.java                # Trade-Datenmodell (Einzelner Trade)
│   ├── Tradeliste.java           # Liste von Trades
│   ├── FileAccess.java           # Dateizugriff
│   ├── GC.java                   # Globale Konstanten
│   ├── Inf.java                  # Informations-Klasse
│   ├── OptimizeResult.java       # Optimierungsergebnisse
│   ├── PanelResult.java          # Panel-Darstellung von Ergebnissen
│   ├── ProfitPanel.java          # Gewinn-Panel
│   ├── SG.java                   # String-Grundfunktionen
│   ├── Swttool.java              # SWT-Hilfswerkzeuge
│   ├── Tools.java                # Allgemeine Werkzeuge
│   ├── ToolsException.java       # Custom Exception
│   ├── Tracer.java               # Logging/Tracing
│   ├── WeekdayChart.java         # Wochentag-Chart
│   └── DemoPanel.java            # Demo-Panel
└── hilfsklasse/          # Hilfsklassen
    ├── Archive.java              # Archivierung
    ├── FileAccessDyn.java        # Dynamischer Dateizugriff
    ├── Infbuf.java               # Info-Buffer
    ├── IsValid.java              # Validierung
    ├── KeyReader.java            # Schlüssel-Leser
    ├── Keywortliste.java         # Schlüsselwort-Liste
    ├── Ma.java                   # Moving Average
    ├── Memsucher.java            # Speicher-Suche
    ├── Moni.java                 # Monitor-Hilfsfunktionen
    ├── Prop2.java                # Properties-Verwaltung
    ├── Propx.java                # Properties-Erweiterung
    ├── SG.java                   # String-Grundfunktionen
    ├── SWTwindow.java            # SWT-Fenster Basisklasse
    ├── StringGrundfunktionenDyn.java  # Dynamische String-Tools
    ├── Swttool.java              # SWT-Hilfswerkzeuge
    ├── Sys.java                  # System-Funktionen
    ├── Sysaktion.java            # System-Aktionen
    ├── TDate.java                # Datum-Hilfsfunktionen
    ├── ToolsDyn.java             # Dynamische Werkzeuge
    └── Tracer.java               # Logging/Tracing

```

## Wichtige Klassen

### `Trade`
Repräsentiert einen einzelnen Trade mit allen relevanten Daten (Ticket, Typ, Volumen, Gewinn, Zeitstempel, etc.).

### `Tradeliste`
Container-Klasse für eine Liste von `Trade`-Objekten mit Sortier- und Filterfunktionen.

### `Tools` / `ToolsDyn`
Allgemeine Hilfsmethoden für String-Verarbeitung, Dateioperationen und Datenkonvertierung.

### `Tracer`
Logging-Framework für die Ausgabe von Trace-Informationen mit konfigurierbaren Levels.

### `TDate`
Datum-/Zeit-Hilfsfunktionen für die Verarbeitung von Trading-Zeitstempeln.

### `FileAccess` / `FileAccessDyn`
Abstraktion für den Dateizugriff auf Trading-Daten und Konfigurationsdateien.
