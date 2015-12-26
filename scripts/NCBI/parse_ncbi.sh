#!/bin/bash

SEQUENCE_SCRIPT=./extract_sequence.awk
ANNOTATION_SCRIPT=./extract_annotation.awk
OUTPUT_FOLDER=output

#sterg folderul cu rezultatele anterioare
rm -R -f $OUTPUT_FOLDER;
mkdir $OUTPUT_FOLDER;

#fac prelucrarea fisierelor Genbank
for file in $(ls *.gbk)
do
	# Numele fisierului
	echo "Processing $file";

	# Numarul chromozomului
	chromosome_number=$(cat $file | head -n 10 | grep "^DEFINITION" | sed 's/chromosome/#/g' | sed 's/,/#/g' | cut -d "#" -f 2 | tr -d " \t\n");
	if [ "$chromosome_number" = "" ]; then
		chromosome_number="genome"
	fi

	# Procesez secventa
	cat $file | $SEQUENCE_SCRIPT > "$OUTPUT_FOLDER/${chromosome_number}.raw";

	# Procesez anotatiile
	cat $file | $ANNOTATION_SCRIPT | cut -d "," -f 2 > "$OUTPUT_FOLDER/${chromosome_number}_gene.fasta";
done

echo "Output created successfully"
