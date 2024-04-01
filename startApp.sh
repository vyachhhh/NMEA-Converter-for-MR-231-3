#!/bin/bash
/opt/mssql/bin/sqlservr

sleep 1m

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "P@ssw0rd" -i /app/mssql/nmeadata.sql

java -Dprism.order=sw -jar javafxApp-1.0-SNAPSHOT-jar-with-dependencies.jar