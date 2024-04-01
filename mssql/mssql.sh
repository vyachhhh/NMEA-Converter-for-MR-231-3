#!/bin/bash
apt-get update
apt-get install -y wget
apt-get install -y ufw
wget -qO- https://packages.microsoft.com/keys/microsoft.asc | tee /etc/apt/trusted.gpg.d/microsoft.asc
add-apt-repository "$(wget -qO- https://packages.microsoft.com/config/ubuntu/20.04/mssql-server-2022.list)"
apt-get install -y mssql-server
/opt/mssql/bin/mssql-conf setup
systemctl status mssql-server

apt-get install -y gnupg2
apt-get install openjfx

ufw default deny incoming
ufw default allow outgoing

ufw allow 1433

ufw enable

curl https://packages.microsoft.com/keys/microsoft.asc | sudo tee /etc/apt/trusted.gpg.d/microsoft.asc
curl https://packages.microsoft.com/config/ubuntu/20.04/prod.list | sudo tee /etc/apt/sources.list.d/msprod.list
apt-get update
apt-get install mssql-tools unixodbc-dev
apt-get update
apt-get install mssql-tools
echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bash_profile
echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bashrc
source ~/.bashrc