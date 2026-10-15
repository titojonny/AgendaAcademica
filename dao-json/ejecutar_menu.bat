@echo off
chcp 65001 > nul
title Menú Interactivo DAO JSON - Consola
echo Compilando e iniciando Menú Interactivo...
set "CP=C:\Users\adona\.m2\repository\com\google\code\gson\gson\2.11.0\gson-2.11.0.jar"
if not exist "bin" mkdir bin
javac -encoding UTF-8 -cp "%CP%" -d bin src\main\java\sv\edu\ufg\dao\modelo\*.java src\main\java\sv\edu\ufg\dao\dao\*.java src\main\java\sv\edu\ufg\dao\Main.java
java -Dfile.encoding=UTF-8 -cp "bin;%CP%" sv.edu.ufg.dao.Main --menu
pause
