#!/usr/bin/env bash

# place this script in a compiled GA in *rootfolder*/resources/gene/
echo "Name of the transposable element:"
read r1


echo "Coordinates of 5' IR (Start End):"
read ar1 ar2
echo "5'IR ; none ; $ar1 ; $ar2 ; none"  > $r1.gene


echo "Coordinates of 3' IR (Start End):"
read ar3 ar4
echo "3'IR ; none ; $ar3 ; $ar4 ; none"  >> $r1.gene

echo "Do you want to annotate more features ? (Y/N)"
read r14

i=0
while [ $r14 = "Y" ]
do
	i=`expr $i + 1`
	echo "Feature number $i (Name  Start  End  Orientation)"
	read r2 r3 r4 r5
	echo "$r2 ; none ; $r3 ; $r4 ; $r5"  >> $r1.gene
	echo "Do you want to annotate more features ? (Y/N)"
	read r14
done

echo "none ; none ; $ar4 ; $ar4; none" >> $r1.gene
lines=$(wc -l $r1.gene | awk '{ print $1 }')
cd ../..  
sed -i "/$r1/s/0/$lines/" paramsIndexFisiere.txt
	exit
