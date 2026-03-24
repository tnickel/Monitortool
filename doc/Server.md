# Server – Netzwerk-Server

## Zweck
Der **Server** stellt eine Netzwerkschnittstelle bereit, über die Clients (z.B. TradeMonitor oder mobile Apps) auf Trading-Daten zugreifen können. Er basiert auf Socket-Kommunikation.

## Technologie
- **GUI-Framework:** Java Swing
- **Netzwerk:** Java Socket API (NIO)
- **Protokoll:** Eigenes Text-basiertes Protokoll

## Einstiegspunkt
```
Main.StartServer.main(String[] args)
```
Erwartet ein Arbeitsverzeichnis mit `bin`-Unterverzeichnis. Konfiguration liegt in `/conf`.

## Paketstruktur

```
Server/src/
├── Main/
│   ├── StartServer.java          # Swing-GUI mit Start/Stop-Buttons
│   └── StartServerSwt.java       # Alternative SWT-Version
├── data/
│   ├── Client.java               # Client-Datenmodell
│   ├── Clientlist.java           # Verwaltung verbundener Clients
│   ├── ConfigServer.java         # Server-Konfiguration
│   └── GlobServer.java           # Globale Server-Variablen
├── infobox/
│   └── ShowMessage.java          # Nachrichten-Anzeige (JList)
├── netw/
│   ├── ServerHandler.java        # Client-Verbindungshandler
│   ├── ServerNetworkService.java # Netzwerk-Service-Schicht
│   ├── ServerProtokoll.java      # Protokoll-Implementierung
│   └── ServerWork.java           # Server-Arbeitslogik
└── soc/
    └── ServerExample.java        # Socket-Server Implementierung
```

## Funktionsweise

1. **StartServer** öffnet eine Swing-GUI mit Start- und Stop-Buttons
2. Beim Starten wird `ServerExample.main()` aufgerufen
3. Der Server lauscht auf eingehende Socket-Verbindungen
4. Jeder Client wird über einen `ServerHandler` bedient
5. Das `ServerProtokoll` definiert die Kommunikation
6. Client-Liste wird in `Clientlist` verwaltet

## Konfiguration
- Konfigurationsdateien liegen im `conf/`-Verzeichnis
- Root-Pfad wird über `Rootpath`-Klasse verwaltet
