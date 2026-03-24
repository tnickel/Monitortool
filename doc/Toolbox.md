# Toolbox – SQ4x-Workflows & Tools

## Zweck
Die **Toolbox** integriert StrategyQuant (SQ4x) Workflows und bietet erweiterte Analyse-Tools wie Cluster-Bildung, Kommentierung, Chart-Generierung und XML-Verarbeitung.

## Technologie
- **GUI:** Java Swing + SWT
- **Charts:** JFreeChart
- **Daten:** XML-Parser (eigene Implementierung)

## Einstiegspunkt
```
start.StartToolbox.main()
```

## Paketstruktur

```
Toolbox/src/
├── start/
│   └── StartToolbox.java         # Einstiegspunkt
├── cluster/
│   └── Cluster.java              # Cluster-Algorithmus
├── sq4xWorkflow/         # StrategyQuant Integration
│   ├── Sq.java                   # SQ-Basis
│   ├── SqBaseElem.java           # SQ-Basiselement
│   ├── SqBaseList.java           # SQ-Basisliste
│   ├── SqWorkflowMaster.java    # Workflow-Master
│   ├── SqGenerateWorkflowMain.java # Workflow-Generator
│   ├── SqCollectStoreResultsMain.java # Ergebnis-Sammlung
│   ├── SqExporterBatch.java     # Export-Batch
│   ├── SqDatabaseHandler.java   # Datenbank-Handler
│   ├── SqGoogle.java            # Google-Integration
│   ├── SqWorkflowPacker.java   # Workflow-Packer
│   ├── SqSumElem.java          # Summenelement
│   ├── SqSumWorkflow.java      # Summen-Workflow
│   └── ProjectSq.java          # SQ-Projekt
├── data/                 # Datenmodelle
│   ├── ClusterSet.java           # Cluster-Set
│   ├── ClusterStrategy.java     # Cluster-Strategie
│   ├── Conf100Tools.java        # Konfigurations-Tools
│   ├── CorelAllResult.java      # Korrelations-Ergebnisse
│   ├── Endtestdata.java         # Endtest-Daten
│   ├── FilterResponse.java     # Filter-Antworten
│   ├── GlobToolbox.java        # Globale Toolbox-Variablen
│   ├── Metriktools.java        # Metrik-Werkzeuge
│   ├── Profitelement.java      # Gewinn-Element
│   ├── RtTools.java            # Runtime-Tools
│   ├── Toolboxconf.java        # Konfiguration
│   └── TradeResultliste.java   # Trade-Ergebnisliste
├── graphic/              # Visualisierung
│   ├── GesammtSumChart.java     # Gesamtsummen-Chart
│   ├── GesammtSumChart2.java    # Gesamtsummen-Chart v2
│   ├── MonatsChart.java         # Monats-Chart
│   └── SumChart.java            # Summen-Chart
├── pricedataseries/
│   └── PriceDataSeries.java     # Preisdaten-Serien
├── work/                 # Arbeitsklassen
│   ├── ClusterWork.java         # Cluster-Verarbeitung
│   ├── CommentWork.java         # Kommentar-Verarbeitung
│   └── MboxToolQuest.java       # Message-Box Tool
└── xml/                  # XML-Verarbeitung
    ├── Parser.java               # XML-Parser
    └── TnParser.java             # TN-spezifischer Parser
```

## Kernfunktionen

1. **SQ4x-Workflow-Management** – Erstellung, Ausführung und Export von StrategyQuant-Workflows
2. **Cluster-Analyse** – Gruppierung ähnlicher Strategien
3. **Chart-Generierung** – Monats-, Summen- und Gesamt-Charts
4. **XML-Verarbeitung** – Import/Export von Konfigurationen
5. **Batch-Export** – Automatisierter Export von Workflow-Ergebnissen
