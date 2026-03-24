# Optimizer – Strategie-Optimierung

## Zweck
Der **Optimizer** dient zur automatisierten Optimierung von Trading-Algorithmen und Indikatoren. Er ermöglicht die systematische Suche nach optimalen Parametern für Expert Advisors.

## Technologie
- **GUI:** Java Swing
- **Charts:** JFreeChart

## Einstiegspunkt
```
Start.StartOptimizer.main()
```

## Paketstruktur

```
Optimizer/src/
├── Start/
│   └── StartOptimizer.java       # Einstiegspunkt
├── Opti/
│   ├── GdXCalculation.java       # GDX-Berechnungen
│   └── IndiCalculation.java      # Indikator-Berechnungen
├── data/
│   ├── Algodat.java              # Algorithmus-Daten
│   ├── Algoliste.java            # Algorithmus-Liste
│   ├── Config.java               # Konfiguration
│   ├── ConfigData.java           # Konfigurationsdaten
│   ├── EaMasterPatcher.java      # EA Master-Patcher
│   ├── IndiGrundfunkt.java       # Indikator-Grundfunktionen
│   ├── IndiWorker.java           # Indikator-Worker
│   ├── IndicatordataHashmap_0A.java  # Indikator-Daten (HashMap)
│   ├── IndicatordataListe_0B.java    # Indikator-Daten (Liste)
│   ├── Indidat.java              # Einzelne Indikator-Daten
│   ├── MqlIndikatorEa.java       # MQL4/5 Indikator-EA
│   ├── MqlPatchEa.java           # MQL4/5 EA-Patcher
│   ├── OGC.java                  # Optimizer-Globale Konstanten
│   └── OptimizeAlgo.java         # Optimierungsalgorithmus
├── jHilfsfenster/        # Hilfs-Dialogfenster
│   ├── JShowIndikator.java       # Indikator-Anzeige
│   ├── JShowTradelist.java       # Tradeliste-Anzeige
│   ├── SelectFile.java           # Datei-Auswahl
│   ├── SelectFile2Strategy.java  # Strategie-Auswahl
│   └── jCompareTradelist.java    # Tradeliste-Vergleich
├── jhilf/                # GUI-Hilfsmittel
│   ├── ColoredTableCellRenderer*.java  # Farbige Tabellenrenderer
│   ├── JProgressBarDemo.java     # Fortschrittsanzeige
│   └── JTableTool.java           # JTable-Werkzeuge
└── work1/                # Arbeits-/Worker-Klassen
    ├── FrameWorker.java          # Frame-Worker
    ├── GuiWorker.java            # GUI-Worker
    └── JTable*Comperator.java    # Tabellen-Sortierung
```

## Kernfunktionen

1. **Indikator-Optimierung** – Systematische Suche nach optimalen Indikator-Parametern
2. **Algorithmus-Vergleich** – Vergleich verschiedener Trading-Algorithmen
3. **MQL-Integration** – Patchting und Anpassung von MQL4/5 Expert Advisors
4. **Ergebnis-Visualisierung** – Farbcodierte Tabellen und Fortschrittsanzeigen
5. **Tradeliste-Vergleich** – Vergleichende Analyse verschiedener Strategien
