#!/bin/bash

verbose=false
execute=false
nextIsPath=false

function write {
    if $verbose
    then
        echo $1
    fi
}

path="."

if [ $# -gt 0 ]
then
    for (( i=1; $i<=$#; i=$i+1 ))
    do
        if $nextIsPath
        then
            path=${!i}
            nextIsPath=false
        else
            if [ "${!i}" == "-v" ]
            then
                verbose=true
            elif [ "${!i}" == "-e" ]
            then
                execute=true
            elif [ "${!i}" == "-p" ]
            then
                nextIsPath=true
            elif [ "${!i}" == "-c" ]
            then
                clear
            elif [ "${!i}" == "-h" ]
            then
                echo "jBookLibrary make bash script"
                echo "  -c clear screen"
                echo "  -v show executed commands"
                echo "  -e run game after compile"
                echo "  -p {PATH} set path where JAR will be moved"
                exit
            else
                echo "Unknow parameter ${!i}"
                exit;
            fi
        fi
    done
fi

if $nextIsPath
then
    echo "You must send path with -p argument"
    exit
fi

if [ `ls -R1 | grep class | wc -l` -gt 0 ]
then
    write "> rm -r src/**.class"
    rm -r src/**.class
fi

if [ "${path:${#path}-1:1}" == "/" ]
then
    path=${path:0:${#path}-1}
fi

write "> javac -cp src/ src/jBookLibrary.java"
javac -cp src/ src/jBookLibrary.java

if $verbose
then
    write "> jar cfmv jBookLibrary.jar Manifest.txt -C src/ ."
    jar cfmv jBookLibrary.jar Manifest.txt -C src/ .
else
    jar cfm jBookLibrary.jar Manifest.txt -C src/ .
fi

write "> chmod u+x jBookLibrary.jar"
chmod u+x jBookLibrary.jar

if [ "$path" != "." ]
then
    realpath=`realpath -s "$path/"`

    write "> mv jBookLibrary.jar $realpath/jBookLibrary.jar"
    mv jBookLibrary.jar "$path/jBookLibrary.jar"
else
    realpath=$path
fi

write "> rm -r src/**.class"
rm -r src/**.class

if $execute
then
    write "> java -jar $realpath/jBookLibrary.jar"
    java -jar "$path/jBookLibrary.jar"
fi
