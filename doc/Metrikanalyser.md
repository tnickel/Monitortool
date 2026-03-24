# Metrikanalyser – ML-basierte Metrik-Analyse

## Zweck
Der **Metrikanalyser** integriert **Weka Machine Learning** zur Analyse von Trading-Metriken. Er berechnet Korrelationen, erstellt Portfolios und nutzt ML-Klassifikatoren für Strategie-Bewertungen.

## Technologie
- **GUI:** Eclipse SWT
- **Machine Learning:** Weka
- **Verarbeitung:** Multi-threaded Batch-Processing

## Einstiegspunkt
```
Main.NewSWTApp.main()
```

## Paketstruktur

```
Metrikanalyser/src/
├── Main/
│   └── NewSWTApp.java            # SWT-Einstiegspunkt
├── Batch/
│   └── AutoWorkflow.java         # Automatisierter Workflow
├── WekaTools/            # Weka ML-Integration
│   ├── WekaLearn.java            # Lernalgorithmen
│   ├── WekaModelTester.java      # Modell-Tester
│   ├── WekaModelTesterSimple.java # Vereinfachter Tester
│   ├── WekaClassifierBestlist.java# Beste Klassifikatoren
│   ├── WekaClassifierElem.java   # Klassifikator-Element
│   ├── WekaAttributeReduction.java# Attribut-Reduktion
│   ├── WekaMatrixCollector.java  # Matrix-Sammlung
│   ├── WekaResultCollector.java  # Ergebnis-Sammlung
│   ├── WekaResultElem.java       # Ergebnis-Element
│   ├── WekaTabTools.java         # Tabellen-Werkzeuge
│   └── ClassifiedElem.java       # Klassifizierte Elemente
├── calcPack/             # Berechnungen
│   ├── CalcCorrelationAllWorkflows.java  # Korrelation aller Workflows
│   ├── CalculationOneSetting.java        # Einzelberechnungen
│   └── CalOpt100Sammler.java             # Optimierung (Top 100)
├── data/
│   ├── Best100Portfolios.java    # Top 100 Portfolios
│   ├── EndtestResult.java        # Endtest-Ergebnisse
│   ├── MFilter.java              # Metrik-Filter
│   ├── Metrikglobalconf.java     # Globale Konfiguration
│   ├── Personlist.java           # Personen-Liste
│   ├── Portfolio.java            # Portfolio-Modell
│   ├── Stratliste.java           # Strategie-Liste
│   └── meglob.java               # Globale Variablen
├── fileActorPack/        # Datei-Operationen
│   ├── AutoGenVerzeichnisse.java # Automatische Verzeichniserstellung
│   └── Filemanager.java          # Datei-Manager
├── filterPack/
│   └── Filterfiles.java          # Datei-Filter
└── userinterface/        # Konfigurationsinterface
    ├── SetConfig.java            # Konfiguration setzen
    ├── SetCorrelationConfig.java # Korrelations-Konfiguration
    └── SetFilter.java            # Filter setzen
```

## Kernfunktionen

1. **Weka ML-Analyse** – Einsatz von Weka-Klassifikatoren zur Strategie-Bewertung
2. **Attribut-Reduktion** – Automatische Reduktion relevanter Features
3. **Korrelationsanalyse** – Berechnung von Korrelationen zwischen Workflows
4. **Portfolio-Optimierung** – Erstellung und Bewertung von Top-100-Portfolios
5. **Batch-Verarbeitung** – Automatisierte Workflow-Ausführung
