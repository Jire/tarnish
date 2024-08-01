@echo off
title Tarnish Server
java -XX:-OmitStackTraceInFastThrow --enable-preview -XX:+UseZGC -Xmx8g -Xms4g -jar build\libs\os-royale-server-all.jar
pause