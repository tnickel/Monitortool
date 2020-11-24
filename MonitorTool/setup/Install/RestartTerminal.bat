@echo off
rem RestartTerminal.bat
set /a initialCount=0
for /f "usebackq delims==" %%F in (`tasklist ^| findstr "terminal.exe"`) do (
    set/a initialCount+=1
)
:loop
    ping -n 15 localhost >nul& rem Sleep 5 seconds
    set /a newCount=0
    for /f "usebackq delims==" %%F in (`tasklist ^| findstr "terminal.exe"`) do (
        set/a newCount+=1
    )
    if %newCount% == %initialCount%     goto loop





:execute


start "terminal" "%~dp0terminal.exe"

