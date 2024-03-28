#!/bin/bash
set -m
/opt/mssql/bin/sqlservr & /var/opt/mssql/data/nmeadata.sh &
fg %1