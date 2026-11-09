@echo off
REM Abre el sistema con formularios (version grafica). Requiere JDK 8 o superior.
cd /d "%~dp0"
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\cochera\*.java src\cochera\datos\*.java src\cochera\dto\*.java src\cochera\excepcion\*.java src\cochera\gui\*.java src\cochera\impresion\*.java src\cochera\modelo\*.java src\cochera\prueba\*.java src\cochera\servicio\*.java src\cochera\util\*.java src\cochera\vista\*.java
if errorlevel 1 (
  echo Error de compilacion. Verifica que el JDK este instalado: javac -version
  pause
  exit /b 1
)
java -cp bin cochera.Ejec_Cochera
pause
