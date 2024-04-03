@echo off

echo Moving one directory up
cd ..
echo.

echo Running the program
call ./gradlew.bat run -t --info
echo.