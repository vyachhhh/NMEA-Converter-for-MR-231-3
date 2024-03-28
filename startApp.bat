@echo off

echo Creating network
docker network create nmea-sql

echo Starting MSSQL container
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=P@ssw0rd" -p 1433:1433 --name mssql -d mcr.microsoft.com/mssql/server:2019-latest

echo Connecting...
docker network connect nmea-sql mssql

timeout /t 5 > nul

echo Building NMEA-Container...
docker build -t nmea .

echo Creating Database
docker cp "%~dp0\mssql\nmeadata.sql" mssql:/var/opt/mssql/data/nmeadata.sql

docker exec -it mssql /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P P@ssw0rd -i /var/opt/mssql/data/nmeadata.sql

echo Starting...
docker run -it --name nmea -d nmea

echo Connecting...
docker network connect nmea-sql nmea

echo Wait for XLaunch to start. Don't close this window.
pause
