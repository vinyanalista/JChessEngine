set SCRIPT_FOLDER=%~dp0
winboard.exe -cp -fcp "java -jar %SCRIPT_FOLDER%/DumbEngine.jar" -scp "java -jar %SCRIPT_FOLDER%/DumbEngine.jar" -coords -debug