#!/bin/bash
if [ `ls -1 | grep class | wc -l` -gt 0 ]
then
    echo "> rm *.class"
    rm *.class
fi

if [ `ls -1 | grep jar | wc -l` -gt 0 ]
then
    echo "> rm *.jar"
    rm *.jar
fi

if [ $# -gt 0 ]
then
    path=$1
else
    path="."
fi

if [ "${path:${#path}-1:1}" == "/" ]
then
    path=${path:0:${#path}-1}
fi

echo "> javac jBookLibrary.java"
javac jBookLibrary.java

echo "> jar cfe jBookLibrary.jar jBookLibrary *.class resources/*"
jar cfe jBookLibrary.jar jBookLibrary *.class resources/*

if [ "$path" != "." ]
then
    echo "> mv jBookLibrary.jar \"$path/jBookLibrary.jar\""
    mv jBookLibrary.jar "$path/jBookLibrary.jar"
fi

echo "> rm *.class"
rm *.class
