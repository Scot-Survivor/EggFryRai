@echo off

echo Moving one directory up
cd ..
echo.

echo Running Spotless formatting...
call ./gradlew spotlessApply

echo Running tests...
call ./gradlew test

echo Pre-commit checks completed.
