@echo off
setlocal

set JAR_NAME=tubemq-cli-${project.version}-jar-with-dependencies.jar

set DIR=%~dp0
set JAR=%DIR%%JAR_NAME%

java -jar "%JAR%" %*

endlocal
exit /b