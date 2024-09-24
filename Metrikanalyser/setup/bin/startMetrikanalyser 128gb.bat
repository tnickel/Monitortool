@echo off

rem Überprüfen, ob Java existiert
if exist "C:\Forex\Metrikanalyser\j64_fast\bin\java.exe" (
    set java="C:\Forex\Metrikanalyser\j64_fast\bin\java.exe"
) else (
    echo Java konnte nicht gefunden werden. Überprüfen Sie den Pfad.
    exit /b
)

rem Angepasste Speicheroptionen (120G maximaler Heap für eine 128G Maschine)
set XMS=-Xms120G
set XMX=-Xmx120G

rem Direkt-Speichergröße angepasst
set DIRECTMEMORY=-XX:MaxDirectMemorySize=16G

rem Metaspace-Größe beibehalten
set METASPACE=-XX:MetaspaceSize=512M -XX:MaxMetaspaceSize=8G

rem Garbage Collector Optionen für Java 8 (oder höher)
set GC=-XX:+UseG1GC -XX:MaxGCPauseMillis=200

rem Initiating Heap Occupancy für G1GC (optional)
set GCINIT=-XX:InitiatingHeapOccupancyPercent=30

rem Heap-Dump bei OutOfMemoryError und GC-Logging (neue Syntax ab Java 9)
set HEAPDUMP=-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=C:\Forex\Metrikanalyser\heapdump.hprof
set GCLOG=-Xlog:gc*:C:\Forex\Metrikanalyser\logs\gc.log:time,level,tags

rem Stack-Größe angepasst
set STACKSIZE=-Xss5M

rem Java-Anwendung ausführen
%java% %XMS% %XMX% %DIRECTMEMORY% %METASPACE% %GC% %GCINIT% %HEAPDUMP% %GCLOG% %STACKSIZE% -jar C:\Forex\Metrikanalyser\bin\metrikanalyser.jar C:\Forex\Metrikanalyser 2> C:\Forex\Metrikanalyser\logs\error.txt
