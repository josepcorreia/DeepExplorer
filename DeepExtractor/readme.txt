DeepExtrator: responsável pela extração de co-ocorrências a partir de um corpus processado pela cadeia STRING e pelo armazenamento destas na base de dados.

DeepMeasures: responsável pelo cálculo das diferentes medidas de associação para cada co-ocorrência. 

	

Para compilar e correr:
	Com o ant:
		-compilar:
			ant main

		-correr:
			DeepExtrator -> extração das co-ocorrências:
				java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor path_to_db path_to_corpus

			DeepMeasures-> cálculo das medidas:
				java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures path_to_db

	Com o jar:
		-compilar:
			ant compress

		-correr: 
			DeepExtrator -> extração das co-ocorrências:
				#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepExtractor path_to_db path_to_corpus

			DeepMeasures-> cálculo das medidas:
				#java -cp jar/DeepExtractor.jar:lib/* pt.inescid.l2f.DeepMeasures path_to_db


Para executar a extração total de um corpus, deve criar-se inicialmente a base de dados com o nome "db_deep.db". No caso das máquinas XIP, a base de dados não deve estar localizada no afs mas sim no /tmp.   

Após a criação da base de dados, deve-se correr o script "runJAR", onde está indicado qual a localização da base de dados (durante a extração e a localização final) e qual a localização do corpus já processado pela STRING. 
Uma vez que o corpus se encontra dividido por partes, o script executa o DeepExtractor para cada uma destas. No fim de cada extração, a base de dados que armazena todas as co-ocorrências até aqui, é copiada para a localização final.
Assim, no caso de haver algum problema a meio da extração, existe sempre um backup da base de dados para cada parte já executada.