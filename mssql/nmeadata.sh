#!/bin/bash
# Wait for database to startup
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "P@ssw0rd" -i /var/opt/mssql/data/nmeadata.sql