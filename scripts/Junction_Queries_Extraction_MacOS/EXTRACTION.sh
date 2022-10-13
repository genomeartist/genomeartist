#!/bin/bash

brew install gnu-sed
brew install gawk

echo "TE reference length"
read Qlength

echo "Length of a putative insertion/autoinsertion (in the TE sequence of interest) that may increase the TE sequence length compared to the TE reference length:"
read extensie

TEextensie=$( expr $Qlength + $extensie )

cat *.csv > GA.csv

#Extrag coloane de care am nevoie.
gawk -F "," 'NR >=2 {print$1,$2,$4,$5,$8,$9}' GA.csv > GA.txt

#Parcurg fisierul cu toate rezultatele (GA.txt).
cat GA.txt | while read query GD gstart gend qstart qend
do
#Orientare II, 5'.
if [[ $gstart -gt $gend ]] && [[ $query == *"5" ]]
then
echo $query $GD $gstart $gend $qstart $qend > $GD.$gstart.$gend.IR5.orientare2.txt
#Orientare II, 3'.
elif [[ $gstart -gt $gend ]] && [[ $query == *"3" ]]
then
echo $query $GD $gstart $gend $qstart $qend > $GD.$gstart.$gend.IR3.orientare2.txt

#Orientare I, 5'.
elif [[ $gstart -lt $gend ]] && [[ $query == *"5" ]]
then
echo $query $GD $gstart $gend $qstart $qend > $GD.$gstart.$gend.IR5.orientare1.txt
#Orientare I, 3'.
elif [[ $gstart -lt $gend ]] && [[ $query == *"3" ]]
then
echo $query $GD $gstart $gend $qstart $qend > $GD.$gstart.$gend.IR3.orientare1.txt
fi
done

cat *.IR5.orientare2.txt > IR5.orientare2.txt
cat *.IR3.orientare2.txt > IR3.orientare2.txt
cat *.IR5.orientare1.txt > IR5.orientare1.txt
cat *.IR3.orientare1.txt > IR3.orientare1.txt
rm -r *.IR5.orientare2.txt *.IR3.orientare2.txt *.IR5.orientare1.txt *.IR3.orientare1.txt
cat *.orientare1.txt > orientare1.txt
cat *.orientare2.txt > orientare2.txt

#Parcurg orientarea I.
cat IR5.orientare1.txt | while read query5 GD5 gstart5 gend5 qstart5 qend5
do 
cat IR3.orientare1.txt | while read query3 GD3 gstart3 gend3 qstart3 qend3
do 
dif=$( expr $gend3 - $gstart5 )
#Compar ca sa pot afla cazul in care sunt.
if [[ ! $query5 == $query3 ]] && [[ $GD5 == $GD3 ]] && [[ $dif -le $TEextensie ]] && [[ $dif -gt 0 ]] 
then 
echo $query5 $GD5 $gstart5 $gend5 $qstart5 $qend5 >> 5.O1.2IR.txt
echo $query3 $GD3 $gstart3 $gend3 $qstart3 $qend3 >> 3.O1.2IR.txt
fi

done 
done
cat *.O1.2IR.txt > IR5"'"+3"'".O1.txt
rm -r *.2IR.txt

comm -3 <(sort orientare1.txt) <(sort IR5"'"+3"'".O1.txt) > 1IR.O1.txt


#Parcurg orientarea II.
cat IR5.orientare2.txt | while read query5 GD5 gstart5 gend5 qstart5 qend5
do 
cat IR3.orientare2.txt | while read query3 GD3 gstart3 gend3 qstart3 qend3
do 
dif=$( expr $gstart5 - $gend3 )
#Compar ca sa pot afla cazul in care sunt.
if [[ ! $query5 == $query3 ]] && [[ $GD5 == $GD3 ]] && [[ $dif -le $TEextensie ]] && [[ $dif -gt 0 ]] 
then 
echo $query5 $GD5 $gstart5 $gend5 $qstart5 $qend5 >> 5.O2.2IR.txt
echo $query3 $GD3 $gstart3 $gend3 $qstart3 $qend3 >> 3.O2.2IR.txt
fi

done 
done
cat *.O2.2IR.txt > IR5"'"+3"'".O2.txt
rm -r *.2IR.txt

comm -3 <(sort orientare2.txt) <(sort IR5"'"+3"'".O2.txt) > 1IR.O2.txt


cat 1IR.O1.txt 1IR.O2.txt > 1IR.txt
cat IR5"'"+3"'".O1.txt IR5"'"+3"'".O2.txt > 2IR.txt
rm -r *1.txt *2.txt

#Aleg daca extrag upstream sau downstream pt cele cu 2IR.
cat 2IR.txt | while read IR2query IR2GD IR2gstart IR2gend IR2qstart IR2qend
do
if [[ $IR2query == *"5" ]]
then
echo $IR2GD $IR2gstart $IR2gend $IR2qstart $IR2qend > $IR2GD.$IR2gstart.2IR.UPSTREAM.txt
elif [[ $IR2query == *"3" ]]
then
echo $IR2GD $IR2gstart $IR2gend $IR2qstart $IR2qend > $IR2GD.$IR2gstart.2IR.DOWNSTREAM.txt
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
cat **/*.flanking > JunctionSeq_IR5_Upstream.fasta
gsed -i -e "s/-ins/\-ins_IR5./g" JunctionSeq_IR5_Upstream.fasta
gsed -i -e "s/contig_/ctg_/g" JunctionSeq_IR5_Upstream.fasta

cd ..
cd IR3_DOWNSTREAM/
echo "DOWNSTREAM EXTRACTION"
export Qlength
./DOWNSTREAM_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
cat **/*.flanking > JunctionSeq_IR3_Downstream.fasta
gsed -i -e "s/-ins/\-ins_IR3./g" JunctionSeq_IR3_Downstream.fasta
gsed -i -e "s/contig_/ctg_/g" JunctionSeq_IR3_Downstream.fasta
cd ..

mkdir ./IR5+3/
mv IR5_UPSTREAM IR3_DOWNSTREAM IR5+3/
cd IR5+3/
cat **/JunctionSeq* > JunctionSeq_IR5+3.fasta
cd ..
#########################################################################AI VERIFICAT PANA AICI. FUNCTIONEAZA.

#PENTRU UN SINGUR IR EXTRAG UP SI DOWNSTREAM.
cat 1IR.txt | while read IR1query IR1GD IR1gstart IR1gend IR1qstart IR1qend
do
if [[ $IR1query == *"5" ]]
then
echo $IR1GD $IR1gstart $IR1gend $IR1qstart $IR1qend > $IR1GD.$IR1gstart.1IR.UPSTREAM.txt
elif [[ $IR1query == *"3" ]]
then
echo $IR1GD $IR1gstart $IR1gend $IR1qstart $IR1qend > $IR1GD.$IR1gstart.1IR.DOWNSTREAM.txt
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
cat **/*.flanking > JunctionSeq_onlyIR5_Upstream.fasta
gsed -i -e "s/-ins/\-ins_only_IR5./g" JunctionSeq_onlyIR5_Upstream.fasta
gsed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR5_Upstream.fasta
cd ..

cd IR5_only_DOWNSTREAM_TE+GENOMIC/
echo "Single IR5' DOWNSTREAM (RefTE + Genomic) EXTRACTION"
export Qlength
./DOWNSTREAM_TE+GENOMIC_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
cat **/*.flanking > JunctionSeq_onlyIR5_TG_Downstream.fasta
gsed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR5_TG_Downstream.fasta
cd ..

mkdir ./IR5_only/
mv IR5_only_UPSTREAM IR5_only_DOWNSTREAM_TE+GENOMIC IR5_only/
cd IR5_only/
cat **/JunctionSeq* > JunctionSeq_only_IR5.fasta
cd ..

cd IR3_only_DOWNSTREAM/
echo "Single IR3' DOWNSTREAM EXTRACTION"
export Qlength
./DOWNSTREAM_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
cat **/*.flanking > JunctionSeq_onlyIR3_Downstream.fasta
gsed -i -e "s/-ins/\-ins_only_IR3./g" JunctionSeq_onlyIR3_Downstream.fasta
gsed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR3_Downstream.fasta
cd ..

cd IR3_only_UPSTREAM_TE+GENOMIC/
echo "Single IR3' UPSTREAM (RefTE + Genomic) EXTRACTION"
export Qlength
./UPSTREAM_TE+GENOMIC_EXTRACTION.sh 2> /dev/null
rm -r *.fasta *.txt *.sh
cat **/*.flanking > JunctionSeq_onlyIR3_TG_Upstream.fasta
gsed -i -e "s/contig_/ctg_/g" JunctionSeq_onlyIR3_TG_Upstream.fasta
cd ..

mkdir ./IR3_only/
mv IR3_only_DOWNSTREAM IR3_only_UPSTREAM_TE+GENOMIC IR3_only/
cd IR3_only/
cat **/JunctionSeq* > JunctionSeq_only_IR3.fasta
cd ..

cat **/JunctionSeq* > JunctionQueries.fasta
rm -r *.txt GA.csv
