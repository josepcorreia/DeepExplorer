#!/bin/bash

#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor path_to_db path_to_corpus
#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures path_to_db


#/ffs/tmp/string/CORPUS/
corpus='/ffs/tmp/string/CORPUS'
#corpus='/Users/josecorreia/Projects/CetemPublico'

#/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB/
db='/tmp/deepString_tmp/'
#db='/Users/josecorreia/Projects/DB/'

db_final='/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB_F/'
#db_final='/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB_SF/'
#db_final='/Users/josecorreia/Projects/DB/'



mkdir logDeepExtractor
cd DeepExplorer/DeepExtractor

ant main > ../../logDeepExtractor/compile.txt

for dir in $corpus/Parte*; do
    echo "$dir"

	base=$(basename "$dir")
	fname=$base'.txt'
  
   	#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor path_to_db path_to_corpus
   	java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor $db $dir > ../../logDeepExtractor/$fname

   	mkdir $db_final/$base
   	cp $db/db_deep.db $db_final/$base
done

#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures path_to_db
java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures $db > ../../logDeepExtractor/medidas.txt

mkdir $db_final/medidas
cp $db/db_deep.db $db_final/medidas