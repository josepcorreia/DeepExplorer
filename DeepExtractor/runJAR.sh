#!/bin/bash

#localicação do corpus (/ffs/tmp/string/CORPUS/)
#corpus='/ffs/tmp/string/CORPUS'
corpus='/Users/josecorreia/Projects/CetemPublico'

#localização da base de dados durante a extração
#db='/tmp/deepString_tmp/'
db='/Users/josecorreia/Projects/DB/'

#localização da base de dados após a extração
#db_final='/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB_F/'
#db_final='/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB_SF/'
db_final='/Users/josecorreia/Projects/DB_/'

#o script deve-se encpntrar na mesma directoria que o jar/

mkdir logDeepExtractor

for dir in $corpus/Parte*; do
    echo "$dir"

	base=$(basename "$dir")
	fname=$base'.txt'
  
   	#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor path_to_db path_to_corpus
   	java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor $db $dir > logDeepExtractor/$fname

   	mkdir $db_final/$base
   	cp $db/db_deep.db $db_final/$base
done

#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures path_to_db
java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures $db > logDeepExtractor/medidas.txt

mkdir $db_final/medidas
cp $db/db_deep.db $db_final/medidas