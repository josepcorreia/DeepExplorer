# DeepExplorer

The analysis of the co-occurrence patterns between words allows to understand the use (and meaning) of words that are associated with different relationships. The quantification of these standards is a powerful tool in modern lexicography as well as in the construction of basic linguistic resources for the processing of natural language, or learning a language.

The aim of this project is to develop a tool that, based on the STRING natural language processing chain, allows one to explore co-occurrence data obtained from Portuguese texts. The association measures used are the Mutual Information, the Dice coefficient, the LogDice, the Log-likelihood ratio, the Pearson Chi-square and the Significance measure.

The presented solution consists in the extraction of co-occurrences (JAVA) and a web interface. The extraction occurs from a processed corpus by STRING, that finds and stores the co-occurrences in a database (SQLite) . Then, for each co-occurrence stored are calculated the different association measures. The web application provides to users an interface that allows to exploit these co-occurrence patterns. 

The developed project allows the quick access to collected co-occurrences in \textit{corpora} produced by STRING, taking advantage of the rich lexical resources in the chain, as well as its sophisticated syntactic and semantic analysis in order to produce results that the above systems don't allow.
