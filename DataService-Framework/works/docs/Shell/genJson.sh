#!/bin/bash

IFS=" "

while true :
do
    printf "{"
    cat config.cnf | while read line
    do
        arr=($line)
        printf "\"${arr[0]}\":\""
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
            printf "J$(($RANDOM % 10))"
            ;;
            *)
            printf "J$RANDOM"
            ;;
        esac
        printf "\","
    done
    printf "\"x\":\"x\"}\n"
    sleep 5s
done