# MonitorTool - Projektübersicht

**Systemarchitektur & Forex Trading Analyseplattform**

Das *MonitorTool* ist eine eigenentwickelte, umfangreiche Software-Suite zur Echtzeit-Überwachung, Backtesting-Analyse und algorithmischen Optimierung von Forex-Trading-Strategien. Ursprünglich als monolithisches Java-Projekt gestartet, wurde die Plattform in eine moderne **Maven Multi-Module Architektur** überführt, um Skalierbarkeit und Wartbarkeit zu gewährleisten.

---

## 📊 Projektstatistiken

Das Projekt zeugt von massiver fachlicher und technischer Tiefe:

* **Gesamter Java-Code (Lines of Code):** > 137.600 Zeilen
* **Anzahl der Java-Klassen:** 728 Dateien
* **Architektur:** 14 gekapselte Maven-Module

*(Stand: März 2026)*

---

## 🧩 Modul-Architektur

Die Anwendung ist strikt modular aufgebaut. Jedes der 14 Teilprojekte erfüllt einen dedizierten, abgegrenzten Zweck innerhalb der Trading-Pipeline:

### 1. Kern- & UI-Komponenten
* **MonitorTool**: Die Haupt-GUI zur Echtzeit-Visualisierung der Markt- und Trading-Aktivitäten. Nutzt SWT und JFreeChart für hochperformantes Rendering von Marktdaten.
* **Libs**: Zentrale Schnittstellen, Basisklassen und geteilte UI-Komponenten.
* **Analyser**: Umfangreiche Werkzeuge für die Auswertung historischer Kursdaten und das Backtesting grundlegender Strategien.

### 2. Strategie & Optimierung
* **Optimizer**: Die Engine zur algorithmischen Parameter-Optimierung von Trading-Strategien (via Walk-Forward Analyse etc.).
* **WalkforwardCluster**: Ersteller und Verwalter von rechenintensiven Cluster-Analysen für Walk-Forward Tests, um Overfitting zu vermeiden.
* **Toolbox**: Spezial-Workflows (z.B. SQ4x-Workflows) für komplexe Datenanalysen und die Vorbereitung von Strategien.
* **GbAnalyser**: (Gewinnbereich-Analyse) Modul zur Visualisierung und statistischen Bewertung der Profitabilitätsbereiche von Handelssystemen.
* **Metrikanalyser**: Tiefgehende Evaluierung und Auswertung von Performance-Metriken (Sharpe Ratio, Max Drawdown, Profit Factor).
* **LotsizeDemo**: Tool zur Simulation und Konfiguration von dynamischen Positionsgrößen (Money Management/Position Sizing).

### 3. Server & Infrastruktur
* **Server**: Die lokale Netzwerkschnittstelle und Backend-Dienst für Datenaggregation.
* **TradeMonitor**: Ein spezialisierter Client/Dienst zur isolierten Überwachung aktiver Handelspositionen.
* **Kurstool**: Downloader, Konverter und Parser für historische Kursdatenformate (z.B. Tick-Daten).
* **Dropboxcopy**: Integrierte Datensynchronisations-Komponente für das Cloud-Backup relevanter Trading-Logs und Strategie-Setups.

---

## 🛠️ Eingesetzte Technologien & Stack

* **Java (JDK 8)**: Typisierter, objektorientierter Core mit tiefgreifender Multithreading-Architektur für parallele Backtests.
* **Maven**: Vollständiges Dependency-Management, Multi-Module Lifecycles und lokales Repository-Handling.
* **Eclipse SWT & JFreeChart**: Native, reaktionsschnelle Benutzeroberflächen und wissenschaftliche Chart-Visualisierungen.
* **XStream & iText**: Persistenz von Analyseobjekten und systematischer Export von Trading-Reports in PDF.
* **Weka / Apache Commons**: Fortgeschrittene Mathematik-Bibliotheken und Machine-Learning Metriken für die Strategie-Bewertung.

## Zusammenfassung
Das *MonitorTool* ist nicht nur ein reines Anzeigeprogramm, sondern ein ausgewachsenes, algorithmisches Forschungszentrum für Finanzmärkte. Mit knapp **140.000 Zeilen Code** demonstriert das System exzellente Beherrschung von komplexer Business-Logik, nativer Desktop-UI-Entwicklung und modularer Software-Architektur.
