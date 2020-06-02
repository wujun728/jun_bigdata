#!/bin/bash

IFS=" "

while true :
do
    cat config.cnf | while read line
    do
        arr=($line)
        case ${arr[1]} in
            "date")
            printf "`date +'%Y-%m-%d'`"
            ;;
            "datetime")
            printf "`date +'%Y-%m-%d %H:%M:%S'`"
            ;;
            "int")
            printf "$RANDOM"
            ;;
            "type")
            printf "T$(($RANDOM % 10))"
            ;;
            *)
            printf "T$RANDOM"
            ;;
        esac
        printf ","
    done
    printf "\n"
    sleep 5s
done