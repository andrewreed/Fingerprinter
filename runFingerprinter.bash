#!/bin/bash
find "netflix" -type f | grep ".xml" | while read line
do
  path=`dirname $line`
  grep "range/0-" $line | sort | uniq | cut -d "\"" -f2 > $path"/urls.txt"
done
find "netflix" -type d -links 2 | java -jar Fingerprinter.jar
