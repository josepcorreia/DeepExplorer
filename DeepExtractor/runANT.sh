#!/bin/bash

#localicação do corpus (/ffs/tmp/string/CORPUS/)
corpus='/ffs/tmp/string/CORPUS'

#localização da base de dados durante a extração
db='/tmp/deepString_tmp/'

#localização da base de dados após a extração
db_final='/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB_F/'
#db_final='/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB_SF/'

#o script deve-se encpntrar na mesma directoria que o DeepExplorer/

mkdir logDeepExtractor
cd DeepExplorer/DeepExtractor

ant main > ../../logDeepExtractor/compile.txt

for dir in $corpus/Parte*; do
    echo "$dir"

	base=$(basename "$dir")
	fname=$base'.txt'
  
   	ant runInesc -Ddb=$db -Dcorpus_dir=$dir > ../../logDeepExtractor/$fname

   	mkdir $db_final/$base
   	cp $db/db_deep.db $db_final/$base
done

ant runMeasuresInesc -Ddb=$db > ../../logDeepExtractor/medidas.txt
mkdir $db_final/medidas
cp $db/db_deep.db $db_final/medidas
