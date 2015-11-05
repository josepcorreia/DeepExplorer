<?php

class DepQueries
{
    private $conn = NULL;

    public function __construct($conn){
        $this->conn = $conn;
    }

    protected function GetDepDataFromWord2($idWord2, $dep, $prop,  $measure, $limit, $minfreq) {

        $query = "SELECT Palavra.palavra, Palavra.classe, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra1,".$measure.",frequencia
                        FROM Coocorrencia    
                        WHERE idPalavra2 = ".$idWord2."
                          and frequencia >=  ".$minfreq."
                          and tipoDep = '".$dep."'
                          and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra1 = Palavra.idPalavra;";
    //var_dump($query);                
       $result = $this->conn->query($query) or trigger_error($conn->error."[$sql]");

        return $result;
    }

    protected function GetDepDataFromWord1($idWord1, $dep, $prop,  $measure, $limit, $minfreq) {

        $query= "SELECT Palavra.palavra, Palavra.classe, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra2,".$measure.",frequencia
                        FROM Coocorrencia    
                        WHERE idPalavra1 = ".$idWord1."
                          and frequencia >= ".$minfreq."
                          and tipoDep = '".$dep."'
                          and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra2 = Palavra.idPalavra;";
        //var_dump($query);
        $result = $this->conn->query($query) or trigger_error($conn->error."[$sql]");

        return $result;
    }

    public function GetConcordance($idWord1,$idWord2, $dep, $prop, $limit) {
        $query =   "SELECT Frase.numeroFrase, Frase.nomeFicheiro, Frase.frase
                    FROM (Select numeroFrase, nomeFicheiro 
                            from Exemplifica 
                            WHERE idPalavra1 = ".$idWord1."
                            and idPalavra2 = ".$idWord2."
                            and nomeProp = '".$prop."'
                            and tipoDep = '".$dep."'
                            LIMIT ".$limit. ") as ex
                    Inner join Frase on (ex.numeroFrase = Frase.numeroFrase and ex.nomeFIcheiro = Frase.nomeFicheiro);";
        //var_dump($query); 
        $result = $this->conn->query($query);

        return $result;
    }

    public function GetWordId($word, $pos)
    {
        $query =   "SELECT idPalavra
                    FROM Palavra
                    WHERE palavra = '".$word."'
                    and classe = '".$pos."'
                    LIMIT 1;";

        $result = $this->conn->query($query);

        $rs = $result->fetchArray(SQLITE3_ASSOC);

        if(count($rs["idPalavra"]) > 0){
            return $rs["idPalavra"];
        } 
        else{
            $this->conn->close();
            echo '{"wordNotExist": "true"}';
            exit();  
            
        } 
    }

    public function GetDepData($idWord, $dep, $prop, $measure, $limit, $depType, $minfreq)
    {
        if (strpos($depType,'GOVERNED') !== false) {
            return $this->GetDepDataFromWord1($idWord, $dep, $prop, $measure ,$limit, $minfreq);
        }
        else{
            if (strpos($depType,'GOVERNOR') !== false) {
                return $this->GetDepDataFromWord2($idWord, $dep, $prop, $measure ,$limit, $minfreq);
            } 
            else {
                if (strpos($depType,'EXTRA') !== false) {
                return $this->GetDepDataFromWord2($idWord, $dep, $prop, $measure ,1, $minfreq);
                }
            }
        }
    }
}

?>