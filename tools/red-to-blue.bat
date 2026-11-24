@echo off
rem Runs RedToBlue.java. Usage (from the repo folder, in the Android Studio terminal):
rem     .\tools\red-to-blue 9,63,0 21,63,0
rem     .\tools\red-to-blue TeamCode\src\main\java\org\firstinspires\ftc\teamcode\Autos\legacy\ShortRed.java
rem Finds Java in JAVA_HOME, then on the PATH, then the copy that comes with Android Studio.
setlocal
set "JAVA="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA=%JAVA_HOME%\bin\java.exe"
if not defined JAVA for /f "delims=" %%J in ('where java 2^>nul') do if not defined JAVA set "JAVA=%%J"
if not defined JAVA if exist "%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe" set "JAVA=%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe"
if not defined JAVA (
    echo Could not find Java. Install Android Studio, or set JAVA_HOME. 1>&2
    exit /b 1
)
"%JAVA%" "%~dp0RedToBlue.java" %*
