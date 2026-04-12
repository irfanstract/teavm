@echo off


set PROJECT_HOME=%~dp0
set JAVA_HOME=%PROJECT_HOME%.jdks\jdk-17.0.8+7
%PROJECT_HOME%.gradle-dist\gradle-8.5\bin\gradle.bat %*
