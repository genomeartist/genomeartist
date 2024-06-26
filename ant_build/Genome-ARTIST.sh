#!/bin/bash

chmod ug+x ./cleaner
chmod ug+x ./client
chmod ug+x ./data_hashing_utility
chmod ug+x ./run_expansion_generation
chmod ug+x ./server

#Preload cache
chmod ug+x ./cachePreloadGenomes.sh
chmod ug+x ./cacheClean.sh
./cachePreloadGenomes.sh

#Start GA
java -jar genomeARTIST.jar
