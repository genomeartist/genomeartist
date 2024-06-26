#!/bin/bash
export newDir=$1

if [ -n "${newDir}" ]; then
    echo "newDir is set"
    
    if [ -d "${newDir}" ];
    then
        isNewDir=0;
    else
        mkdir ${newDir}
        isNewDir=1;
        echo "newDir created"
    fi

    cd ${newDir}
    if [ $isNewDir -eq 1 ];
    then
        ln -s ../cleaner .
        ln -s ../client .
        ln -s ../data_hashing_utility .
        ln -s ../cacheClean.sh .
        ln -s ../cachePreloadGenomes.sh .
        ln -s ../run_expansion_generation .
        ln -s ../server .

        ln -s ../genomeARTIST.jar .

        ln -s ../lib .
        ln -s ../report .
        ln -s ../scripts .
        ln -s ../temp .

        touch paramsIndexFisiere.txt
        cp ../config.txt .
        cp ../paramsInterface.txt .
        cp ../paramsServer.txt .

        mkdir resources
        mkdir resources/gene
        mkdir resources/outputs
        mkdir resources/raw
        cp ../resources/expansion_table.bin ./resources
    fi
fi

chmod ug+x ./cleaner
chmod ug+x ./client
chmod ug+x ./data_hashing_utility
chmod ug+x ./run_expansion_generation
chmod ug+x ./server

#Preload cache
#chmod ug+x ./cachePreloadGenomes.sh
#chmod ug+x ./cacheClean.sh
#./cachePreloadGenomes.sh

#Start GA
java -jar genomeARTIST.jar
