CREATE INDEX p1_index ON Coocorrencia (idPalavra1, nomeProp, tipoDep, nomeCorpus, frequencia);
CREATE INDEX p2_index ON Coocorrencia (idPalavra2, nomeProp, tipoDep, nomeCorpus, frequencia);

CREATE INDEX dep_index ON Exemplifica (idPalavra1, idPalavra2, nomeProp, tipoDep, nomeCorpus);