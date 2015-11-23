#!/bin/bash

#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor path_to_db path_to_corpus
java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor /Users/josecorreia/Projects/DB/ /Users/josecorreia/Projects/Parte1

#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures path_to_db
java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures /Users/josecorreia/Projects/DB/
