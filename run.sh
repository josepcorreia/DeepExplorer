#!/bin/bash

#/ffs/tmp/string/CORPUS/

db='/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB/'
corpus='/ffs/tmp/string/CORPUS'

max=9
for i in `seq 4 $max`
do
   	dir_aux="Parte0$i";
   	dir=$corpus/$dir_aux;
   	txt="$dir_aux.txt"
   	java -cp "DeepExtractor/dist/DeepExtractor.jar:lib/*" pt.inescid.l2f.DeepExtractor $db $corpus > $txt
   	
done


#max=20
#for i in `seq 10 $max`
#do
#    dir_aux="/Parte$i";
#   	dir=$corpus$dir_aux;
#	txt="$dir_aux.txt"
#   	java -cp "DeepExtractor/dist/DeepExtractor.jar:lib/*" pt.inescid.l2f.DeepExtractor $db $corpus > $txt

#done


