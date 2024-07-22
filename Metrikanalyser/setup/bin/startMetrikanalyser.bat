set java="${installer:sys.installationDir}\jre1.8.0_251\bin\java.exe"
      %java%  -Xmx110G -jar  ${installer:sys.installationDir}\bin\metrikanalyser.jar ${installer:sys.installationDir} 2> error.txt