#!/bin/bash

BUFFER=buffer.txt
AWK_SCRIPT=./extract_annotation.awk
OUTPUT_FOLDER=output

#sterg folderul cu rezultatele anterioare
rm -R -f $OUTPUT_FOLDER;
mkdir $OUTPUT_FOLDER;

#fac prelucrarea fisierelor raw
for file in $(ls *.fa)
do
	chromosome=$(cat $file | head -n 1 | cut -d " " -f 1 | tr -d "> ");
	echo "Generate RAW for $chromosome";
	cat $file  | grep -v ">" | tr -d " \n" > "$OUTPUT_FOLDER/${chromosome}.raw"
done

#fac prelucrarea daturilor
cat *.dat | $AWK_SCRIPT > $BUFFER

#fac fisierele de gene
for arm in $(cat $BUFFER | cut -d "," -f 1 | sort -u)
do
	echo "Generate GENE for $arm";
	cat $BUFFER | grep "^$arm" | cut -d "," -f 2 > "$OUTPUT_FOLDER/${arm}_gene.fasta";
done

#cleanup
rm -R -f $BUFFER
