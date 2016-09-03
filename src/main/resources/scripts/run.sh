#!/usr/bin/env bash

# Arguments:
# 1 = origin folder to listen for create events
# 2 = destination folder where to store files organised by subfolders year/month/day
# 3 = log file name

nohup java -jar toolsbox.jar $1 $2 > $3.log 2>&1 &

nohup java -jar ToolBox.jar /home/marc/archbot/origin/ /home/marc/archbot/dest /home/marc/archbot/failed/ > archbot.log 2>&1 &