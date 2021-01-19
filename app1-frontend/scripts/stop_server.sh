#!/bin/bash
isExistApp=`pgrep -f 'java -jar'`
if [[ -n $isExistApp ]]; then
pkill -f 'java -jar'
fi
