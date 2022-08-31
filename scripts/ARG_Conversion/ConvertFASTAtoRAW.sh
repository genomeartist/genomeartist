#!/bin/bash

#It is recommended that the assembly (the contigs list) to respect the following format: 
#>contig1_name 
#ACGTAGCTAGCTA
#>contig2_name
#ATGCTAGCTGTAG


mkdir ./raw_files

awk -F ">| " '/^>/{s="./raw_files/"$2".fasta"} {print > s}' *.fasta

for i in ./raw_files/*.fasta; do cat $i | grep -v ">" | tr -d "\n" | tr -d " " > ${i%.fasta}.raw; done

cd raw_files/
rm -r *.fasta



