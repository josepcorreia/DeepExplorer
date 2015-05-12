select Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.tipoDep, Coocorrencia.frequencia 
from Coocorrencia
Inner join Palavra on Coocorrencia.idPalavra2 = Palavra.idPalavra
where Coocorrencia.idPalavra1 = (Select idPalavra from Palavra where palavra="ser" and classe="VERB" LIMIT 1) and Coocorrencia.tipoDep = "MOD"
order by Coocorrencia.frequencia desc
limit 25