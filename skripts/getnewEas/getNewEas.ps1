$monroot="D:\Forex\StrategienImEinsatz"
$srcroot="C:\Users\Thomas Nickel\Google Drive\United Generation 2019\InternalResourcesNotForPublic\Thomas\EAs"


$src="Weekly EAs for Real\MT4+EAs+Week1+Feb+2021"  
$dest="FxOpen2_FSB_weekly_for_real"
Remove-Item  "$monroot\$dest\*" 
Copy-Item -Path "$srcroot\$src\*" "$monroot\$dest" 

$src="Weekly EAs for Real\MT4+EAs+Week1+Feb+2021"  
$dest="JFD2_FSB_weekly_for_real"
Remove-Item  "$monroot\$dest\*" 
Copy-Item -Path "$srcroot\$src\*" "$monroot\$dest" 

$src="Weekly EAs for Real\MT4+EAs+Week1+Feb+2021"  
$dest="tickmill2_FSB_weekly_for_real"
Remove-Item  "$monroot\$dest\*" 
Copy-Item -Path "$srcroot\$src\*" "$monroot\$dest" 