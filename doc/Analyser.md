# Analyser – Kern-Analyse-Engine

## Zweck
Der **Analyser** ist das umfangreichste Modul (145 Java-Dateien) und bildet die zentrale Analyse-Engine des Projekts. Er verarbeitet Trading-Daten, führt statistische Analysen durch und bietet vielfältige Visualisierungsmöglichkeiten.

## Technologie
- **GUI:** Swing + SWT (Mischbetrieb)
- **Charts:** JFreeChart
- **Datenbank:** Eigene komprimierte DB-Schicht
- **Serialisierung:** XStream (XML)

## Paketstruktur

```
Analyser/src/
├── swingOberfläche/      # Swing-basierte Oberfläche
│   └── OberflaecheStart.java     # Einstiegspunkt
├── attribute/            # Attribut-Analyse
│   ├── Attrib.java               # Attribut-Datenmodell
│   ├── Diagramm1.java            # Attribut-Diagramme
│   └── ThreadAttribObjI.java     # Thread-basierte Verarbeitung
├── bank/                 # Depot-/Bank-Simulation
│   ├── Bank.java                 # Bank-Modell
│   ├── Depot.java                # Depot-Verwaltung
│   └── Kauf.java                 # Kauf-Transaktionen
├── barChartDemo/         # Chart-Demos (JFreeChart)
├── basis/                # Basis-Verarbeitungslogik
│   ├── BAWorkHtml.java           # HTML-basierte Verarbeitung
│   └── BAWorkThreads.java        # Thread-basierte Verarbeitung
├── championAnalyser/     # Börsen-Champion-Analyse
│   ├── BoerseDe.java             # Boerse.de Integration
│   ├── BoerseMasterliste.java    # Master-Liste
│   └── Championaktie.java        # Champion-Aktien
├── comperatoren/         # Sortier-Komparatoren
│   ├── BesteAlgo.java            # Beste Algorithmen
│   ├── StabilComparator.java     # Stabilitäts-Vergleich
│   ├── SliderComparator.java     # Slider-Sortierung
│   └── ...                       # Diverse Sortierungen
├── compressor/           # Daten-Kompression
│   ├── Compressor.java           # Kompressionslogik
│   └── CompressedThreadDBx.java  # Komprimierte Thread-DB
├── db/                   # Datenbank-Schicht
│   ├── DB.java                   # Datenbank-Zugriff
│   ├── DBlauf.java               # DB-Iterationen
│   └── CompressedPostingObj.java # Komprimierte Datensätze
├── filter/               # Datenfilter
│   ├── Filter.java               # Filter-Interface
│   └── ThreadsDbFilter.java      # Thread-DB-Filter
├── htmlDisplay/          # HTML-basierte Anzeige
├── kursPackage/          # Kurs-Datenverarbeitung
│   ├── Kurs.java                 # Kurs-Datenmodell
│   ├── KursListe.java            # Kurs-Listen
│   └── MetaTraderKursWorker.java # MetaTrader-Integration
├── net/                  # Netzwerk-Kommunikation
├── profitLoop/           # Gewinn-Berechnungsschleifen
├── slider/               # Zeitfenster-Schieberegler
├── statistik/            # Statistische Analysen
├── tagesSchleife/        # Tages-basierte Verarbeitung
├── threading/            # Multi-Threading Framework
│   ├── ThreadPortfolio.java      # Portfolio-Thread
│   └── ThreadWorker.java         # Worker-Threads
├── tradeAnalyse/         # Trade-Analyse
├── walkforward/          # Walk-Forward-Analyse
│   └── WalkforwardMain.java      # Walk-Forward Hauptlogik
└── xml/                  # XML-Verarbeitung
```

## Kernfunktionen

1. **Thread-Analyse** – Multi-threaded Analyse von Trading-Strategien
2. **Datenbank-Kompression** – Effiziente Speicherung großer Datenmengen
3. **Walk-Forward-Analyse** – Validierung von Strategien über Zeiträume
4. **Chart-Visualisierung** – Profit-Charts, Balkendiagramme, Kursverläufe
5. **Filter-System** – Flexible Filterung von Strategien und Trades
6. **MetaTrader-Integration** – Import von MT4/MT5-Kursdaten
7. **Champion-Analyse** – Analyse von Top-Performer-Aktien
8. **Statistik-Engine** – Diverse statistische Kennzahlen
