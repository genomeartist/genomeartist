#!/bin/bash

mkdir ./Output

tar xvzf *.tar.gz -C ./Output/
gunzip *fasta.gz -c > ./Drosophila_genes.fasta
awk -F "=|:" '/^>/{s="./Output/"$3".fasta"} {print > s}' *.fasta
for f in ./Output/*.fasta; do mv $f ${f%.fasta}_gene.fasta; done
for f in ./Output/*.raw
do
	if [ ! -f ${f%.raw}"_gene.fasta" ] 
	then
		rm $f
	fi
done
