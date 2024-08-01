@echo off
title Tarnish Server
java -XX:-OmitStackTraceInFastThrow --enable-preview -XX:+UseZGC -Xmx8g -Xms4g -jar build\libs\game-server-all.jar
pause