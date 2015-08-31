CREATE TABLE Corpus (nome varchar (45) NOT NULL, fonte varchar (45) DEFAULT NULL, ano varchar (45) DEFAULT NULL, genero
varchar (45) DEFAULT NULL, "update" tinyint (1) NOT NULL, PRIMARY KEY (nome));

CREATE TABLE Dependencia (tipoDep varchar (45) NOT NULL, PRIMARY KEY (tipoDep));

CREATE TABLE Propriedade (nomeProp varchar (45) NOT NULL, tipoDep varchar (20) NOT NULL, PRIMARY KEY (nomeProp, tipoDep), CONSTRAINT fk_Propriedade_Dependencia1 FOREIGN KEY (tipoDep) REFERENCES Dependencia (tipoDep) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE Palavra (idPalavra INTEGER PRIMARY KEY ASC, palavra varchar (50) NOT NULL, classe varchar (25) NOT NULL, categoria varchar (25) DEFAULT NULL);

CREATE TABLE Frase (numeroFrase int (11) NOT NULL, nomeFicheiro varchar (45) NOT NULL, frase varchar (1000) NOT NULL, nomeCorpus varchar (20) NOT NULL, PRIMARY KEY (numeroFrase, nomeFicheiro), CONSTRAINT fk_Frase_Corpus1 FOREIGN KEY (nomeCorpus) REFERENCES Corpus (nome) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE Pertence (idPalavra INTEGER (20) NOT NULL, nomeCorpus varchar (45) NOT NULL, nomeProp varchar (45) NOT NULL, tipoDep varchar (45) NOT NULL, frequencia bigint (20) NOT NULL, PRIMARY KEY (idPalavra, nomeCorpus, nomeProp, tipoDep), CONSTRAINT fk_Pertence_Corpus1 FOREIGN KEY (nomeCorpus) REFERENCES Corpus (nome) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT fk_Pertence_Palavra1 FOREIGN KEY (idPalavra) REFERENCES Palavra (idPalavra) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT fk_Pertence_Propriedade1 FOREIGN KEY (nomeProp, tipoDep) REFERENCES Propriedade (nomeProp, tipoDep) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE Coocorrencia (idPalavra1 INTEGER (20) NOT NULL, idPalavra2 INTEGER (20) NOT NULL, nomeProp varchar (45) NOT NULL, tipoDep varchar (45) NOT NULL, nomeCorpus varchar (45) NOT NULL, frequencia bigint (20) NOT NULL, PMI double DEFAULT NULL, Dice double DEFAULT NULL, LogDice double DEFAULT NULL, ChiPearson double DEFAULT NULL, LogLikelihood double DEFAULT NULL, Significance double DEFAULT NULL, PRIMARY KEY (idPalavra1, idPalavra2, nomeProp, tipoDep, nomeCorpus), CONSTRAINT "fk_Co-ocorrencia_Corpus1" FOREIGN KEY (nomeCorpus) REFERENCES Corpus (nome) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT "fk_Co-ocorrencia_Propriedade1" FOREIGN KEY (nomeProp, tipoDep) REFERENCES Propriedade (nomeProp, tipoDep) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT fk_Coocorrencia_Palavra1 FOREIGN KEY (idPalavra1) REFERENCES Palavra (idPalavra) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT fk_Coocorrencia_Palavra2 FOREIGN KEY (idPalavra2) REFERENCES Palavra (idPalavra) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE Exemplifica (numeroFrase int (11) NOT NULL, nomeFicheiro varchar (45) NOT NULL, idPalavra1 bigint (20) NOT NULL, idPalavra2 bigint (20) NOT NULL, nomeProp varchar (45) NOT NULL, tipoDep varchar (45) NOT NULL, nomeCorpus varchar (45) NOT NULL, PRIMARY KEY (numeroFrase, nomeFicheiro, idPalavra1, idPalavra2, nomeProp, tipoDep, nomeCorpus), CONSTRAINT fk_Exemlifica_Coocorrencia1 FOREIGN KEY (idPalavra1, idPalavra2, nomeProp, tipoDep, nomeCorpus) REFERENCES Coocorrencia (idPalavra1, idPalavra2, nomeProp, tipoDep, nomeCorpus) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT fk_Exemlifica_Frase1 FOREIGN KEY (numeroFrase, nomeFicheiro) REFERENCES Frase (numeroFrase, nomeFicheiro) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE Ficheiro (nome varchar (45) NOT NULL, nomeCorpus varchar (45) NOT NULL, PRIMARY KEY (nome, nomeCorpus), CONSTRAINT fk_Ficheiro_Corpus1 FOREIGN KEY (nomeCorpus) REFERENCES Corpus (nome) ON DELETE NO ACTION ON UPDATE NO ACTION);