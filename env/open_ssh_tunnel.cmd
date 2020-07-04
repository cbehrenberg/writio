@echo off

rem %1 = ip
rem %2 = pem file

ssh -i %2 writioadmin@%1 -L 48080:127.0.0.1:48080
