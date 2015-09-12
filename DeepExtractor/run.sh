#!/bin/bash

#/ffs/tmp/string/CORPUS/
corpus='/ffs/tmp/string/CORPUS'
#corpus='/Users/josecorreia/Projects/CetemPublico'

#/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB/
db = '/afs/l2f.inesc-id.pt/projects/tools/string/deepString/DB/'
#db='/Users/josecorreia/Projects/DB/'


mkdir logDeepExtractor
cd DeepExplorer/DeepExtractor

ant main > ../../logDeepExtractor/compile.txt

for dir in $corpus/Parte*; do
    echo "$dir"

	base=$(basename "$dir")
	fname=$base'.txt'
  
   	ant runInesc -Ddb=$db -Dcorpus_dir=$dir > ../../logDeepExtractor/$fname
done

ant runMeasuresInesc -Ddb=$db > ../../logDeepExtractor/medidas.txt

