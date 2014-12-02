#!/bin/bash
find "netflix" -type f | grep ".xml" | while read line
do
  path=`dirname $line`
  grep "range/0-" $line | cut -d "\"" -f2 | sort | uniq > $path"/urls.txt"
done
find "netflix" -type d -links 2 | java -jar Fingerprinter.jar
