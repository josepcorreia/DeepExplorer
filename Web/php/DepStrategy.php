<?php
class DepStrategy {
    private $strategy = NULL;

    public function __construct($dep) {
        switch ($dep) {
            case 'MOD':
                $this->strategy = new MOD_Strategy();
                break;
            case 'SUBJ':
                $this->strategy = new SUBJ_Strategy();
                break;
            case 'CDIR':
                $this->strategy = new CDIR_Strategy();
                break;
            case 'CINDIR':
                $this->strategy = new CINDIR_Strategy();
                break;
            case 'COMPL':
                $this->strategy = new COMPL_Strategy();
                break;
            case 'QUANTD':
                $this->strategy = new QUANTD_Strategy();
                break;
            case 'CLASSD':
                $this->strategy = new CLASSD_Strategy();
                break;
        }
    }
    public function GetDepData($conn, $idWord, $dep, $prop, $measure ,$limit, $depType, $minfreq) {
        return $this->strategy->GetDepData($conn, $idWord, $dep, $prop, $measure ,$limit, $depType, $minfreq);
    }
}
abstract class DepInterface
{
    abstract function GetDepData($conn, $idWord, $dep, $prop, $measure ,$limit, $depType, $minfreq);

    protected function GetDepDataFromWord2($conn, $idWord2, $dep, $prop,  $measure, $limit, $minfreq) {
      /*  $query= "SELECT Palavra.palavra, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra1,".$measure.",frequencia
                        FROM (SELECT *
                             FROM Coocorrencia
                             WHERE frequencia > 1)
                        WHERE idPalavra2 = ".$idWord2."
                        and tipoDep = '".$dep."'
                        and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra1 = Palavra.idPalavra;";*/

        $query = "SELECT Palavra.palavra, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra1,".$measure.",frequencia
                        FROM Coocorrencia    
                        WHERE idPalavra2 = ".$idWord2."
                          and frequencia >=  ".$minfreq."
                          and tipoDep = '".$dep."'
                          and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra1 = Palavra.idPalavra;";

        $result = $conn->query($query);

        return $result;
    }

    protected function GetDepDataFromWord1($conn, $idWord1, $dep, $prop,  $measure, $limit, $minfreq) {
        /*$query= "SELECT Palavra.palavra, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra2,".$measure.",frequencia
                        FROM (SELECT *
                             FROM Coocorrencia
                             WHERE frequencia > 1)
                        WHERE idPalavra1 =".$idWord1."
                        and tipoDep = '".$dep."'
                        and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra2 = Palavra.idPalavra;";*/

        $query= "SELECT Palavra.palavra, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra2,".$measure.",frequencia
                        FROM Coocorrencia    
                        WHERE idPalavra1 = ".$idWord1."
                          and frequencia >= ".$minfreq."
                          and tipoDep = '".$dep."'
                          and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra2 = Palavra.idPalavra;";

        $result = $conn->query($query);

        return $result;
    }
}

class MOD_Strategy extends DepInterface
{
    function GetDepData($conn, $idWord, $dep, $prop, $measure, $limit, $depType, $minfreq)
    {
        if (strpos($depType,'GOVERNED') !== false) {
            return parent::GetDepDataFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);
        }
        else{
            if (strpos($depType,'GOVERNOR') !== false) {
                return parent::GetDepDataFromWord2($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);
            }
        }
    }
}
class SUBJ_Strategy extends DepInterface
{
    function GetDepData($conn, $idWord, $dep, $prop, $measure, $limit, $depType, $minfreq)
    {
        return parent::GetDepdataFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);

    }
}
class CDIR_Strategy extends DepInterface
{
    function GetDepData($conn, $idWord, $dep, $prop, $measure, $limit, $depType,$minfreq)
    {
        return parent::GetDepDataFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);
    }
}
class CINDIR_Strategy extends DepInterface
{
    function GetDepData($conn, $idWord, $dep, $prop, $measure, $limit, $depType, $minfreq)
    {
        return parent::GetDepDataFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);
    }
}
class COMPL_Strategy extends DepInterface
{
    function GetDepData($conn, $idWord, $dep, $prop, $measure, $limit, $depType, $minfreq)
    {
        return parent::GetDepDataFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);
    }
}
class QUANTD_Strategy extends DepInterface
{
    function GetDepData($conn, $idWord, $dep, $prop, $measure, $limit, $depType, $minfreq)
    {
        return parent::GetDepDataFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);
    }
}
class CLASSD_Strategy extends DepInterface
{
    function GetDepData($conn, $idWord, $dep, $prop, $measure, $limit, $depType, $minfreq)
    {
        return parent::GetDepDataFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit, $minfreq);
    }
}
?>