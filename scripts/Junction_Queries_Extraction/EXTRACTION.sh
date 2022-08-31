#!/bin/bash

echo "TE Reference length"
read Qlength
 
cat *.csv > GA.csv

#Extrag coloane de care am nevoie.
awk -F "," 'NR >=2 {print$1,$2,$4,$5,$8,$9}' GA.csv > GA.txt

#Sortez si export unicele.
awk -F "," '{print $2}' GA.csv | sort | uniq -u > GA.sort.txt

#Parcurg fisierul cu toate rezultatele (GA.txt).
cat GA.txt | while read query genomic gstart gend qstart qend
do

#Parcurg fisierul cu unicele.
cat GA.sort.txt | while read uniq
do

#Unde gaseste unica in toate rezultatele printeaza coloanele in fisiere .uniq.txt.
if [[ $genomic == $uniq ]]
then
echo $query $genomic $gstart $gend $qstart $qend > $genomic.$gstart.1IR.txt
sed -i "/$genomic/d" ./GA.txt

fi
done
done

mv GA.txt 2IR.txt
cat *.1IR.txt > 1IR.txt
rm -r *.1IR.txt

#Aleg daca extrag upstream sau downstream pt cele cu 2IR.
cat 2IR.txt | while read IR2query IR2genomic IR2gstart IR2gend IR2qstart IR2qend
do
if [[ $IR2query == *"5" ]]
then
echo $IR2genomic $IR2gstart $IR2gend $IR2qstart $IR2qend > $IR2genomic.$IR2gstart.2IR.UPSTREAM.txt
elif [[ $IR2query == *"3" ]]
then
echo $IR2genomic $IR2gstart $IR2gend $IR2qstart $IR2qend > $IR2genomic.$IR2gstart.2IR.DOWNSTREAM.txt
fi
done

cat *.2IR.UPSTREAM.txt > 2IR_UPSTREAM.txt
cat *.2IR.DOWNSTREAM.txt > 2IR_DOWNSTREAM.txt

rm -r *.2IR.UPSTREAM.txt *.2IR.DOWNSTREAM.txt

mkdir ./IR5_UPSTREAM/
mkdir ./IR3_DOWNSTREAM/

cp *.fasta 2IR_UPSTREAM.txt UPSTREAM_EXTRACTION.sh IR5_UPSTREAM/
cp *.fasta 2IR_DOWNSTREAM.txt DOWNSTREAM_EXTRACTION.sh IR3_DOWNSTREAM/

cd IR5_UPSTREAM/
echo "UPSTREAM EXTRACTION"
export Qlength
./UPSTREAM_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
shopt -s globstar | cat **/*.flanking > JunctionSeq_IR5_Upstream.fasta | shopt -u globstar
sed -i -e "s/-ins/\-ins_IR5./g" JunctionSeq_IR5_Upstream.fasta
sed -i -e "s/contig_/ctg_/g" JunctionSeq_IR5_Upstream.fasta

cd ..
cd IR3_DOWNSTREAM/
echo "DOWNSTREAM EXTRACTION"
export Qlength
./DOWNSTREAM_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
shopt -s globstar | cat **/*.flanking > JunctionSeq_IR3_Downstream.fasta | shopt -u globstar
sed -i -e "s/-ins/\-ins_IR3./g" JunctionSeq_IR3_Downstream.fasta
sed -i -e "s/contig_/ctg_/g" JunctionSeq_IR3_Downstream.fasta
cd ..

mkdir ./IR5+3/
mv IR5_UPSTREAM IR3_DOWNSTREAM IR5+3/
cd IR5+3/
shopt -s globstar | cat **/JunctionSeq* > JunctionSeq_IR5+3.fasta | shopt -u globstar
cd ..
#########################################################################AI VERIFICAT PANA AICI. FUNCTIONEAZA.

#PENTRU UN SINGUR IR EXTRAG UP SI DOWNSTREAM.
cat 1IR.txt | while read IR1query IR1genomic IR1gstart IR1gend IR1qstart IR1qend
do
if [[ $IR1query == *"5" ]]
then
echo $IR1genomic $IR1gstart $IR1gend $IR1qstart $IR1qend > $IR1genomic.$IR1gstart.1IR.UPSTREAM.txt
elif [[ $IR1query == *"3" ]]
then
echo $IR1genomic $IR1gstart $IR1gend $IR1qstart $IR1qend > $IR1genomic.$IR1gstart.1IR.DOWNSTREAM.txt
fi
done

cat *.1IR.UPSTREAM.txt > 1IR_UPSTREAM.txt
cat *.1IR.DOWNSTREAM.txt > 1IR_DOWNSTREAM.txt

rm -r *.1IR.UPSTREAM.txt *.1IR.DOWNSTREAM.txt

mkdir ./IR5_only_UPSTREAM/
mkdir ./IR3_only_DOWNSTREAM/
mkdir ./IR3_only_UPSTREAM_TE+GENOMIC/
mkdir ./IR5_only_DOWNSTREAM_TE+GENOMIC/

cp *.fasta 1IR_UPSTREAM.txt UPSTREAM_EXTRACTION.sh IR5_only_UPSTREAM/
cp *.fasta 1IR_UPSTREAM.txt DOWNSTREAM_TE+GENOMIC_EXTRACTION.sh IR5_only_DOWNSTREAM_TE+GENOMIC/
cp *.fasta 1IR_DOWNSTREAM.txt DOWNSTREAM_EXTRACTION.sh IR3_only_DOWNSTREAM/
cp *.fasta 1IR_DOWNSTREAM.txt UPSTREAM_TE+GENOMIC_EXTRACTION.sh IR3_only_UPSTREAM_TE+GENOMIC/

cd IR5_only_UPSTREAM/
echo "Single IR5' UPSTREAM EXTRACTION"
export Qlength
./UPSTREAM_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
shopt -s globstar | cat **/*.flanking > JunctionSeq_onlyIR5_Upstream.fasta | shopt -u globstar
sed -i -e "s/-ins/\-ins_only_IR5./g" JunctionSeq_onlyIR5_Upstream.fasta
sed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR5_Upstream.fasta
cd ..

cd IR5_only_DOWNSTREAM_TE+GENOMIC/
echo "Single IR5' DOWNSTREAM (RefTE + Genomic) EXTRACTION"
export Qlength
./DOWNSTREAM_TE+GENOMIC_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
shopt -s globstar | cat **/*.flanking > JunctionSeq_onlyIR5_TG_Downstream.fasta | shopt -u globstar
sed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR5_TG_Downstream.fasta
cd ..

mkdir ./IR5_only/
mv IR5_only_UPSTREAM IR5_only_DOWNSTREAM_TE+GENOMIC IR5_only/
cd IR5_only/
shopt -s globstar | cat **/JunctionSeq* > JunctionSeq_only_IR5.fasta | shopt -u globstar
cd ..

cd IR3_only_DOWNSTREAM/
echo "Single IR3' DOWNSTREAM EXTRACTION"
export Qlength
./DOWNSTREAM_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
shopt -s globstar | cat **/*.flanking > JunctionSeq_onlyIR3_Downstream.fasta | shopt -u globstar
sed -i -e "s/-ins/\-ins_only_IR3./g" JunctionSeq_onlyIR3_Downstream.fasta
sed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR3_Downstream.fasta
cd ..

cd IR3_only_UPSTREAM_TE+GENOMIC/
echo "Single IR3' UPSTREAM (RefTE + Genomic) EXTRACTION"
export Qlength
./UPSTREAM_TE+GENOMIC_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
shopt -s globstar | cat **/*.flanking > JunctionSeq_onlyIR3_TG_Upstream.fasta | shopt -u globstar
sed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR3_TG_Upstream.fasta
cd ..

mkdir ./IR3_only/
mv IR3_only_DOWNSTREAM IR3_only_UPSTREAM_TE+GENOMIC IR3_only/
cd IR3_only/
shopt -s globstar | cat **/JunctionSeq* > JunctionSeq_only_IR3.fasta | shopt -u globstar
cd ..

shopt -s globstar | cat **/JunctionSeq* > JunctionQueries.fasta | shopt -u globstar
rm -r *.txt GA.csv
