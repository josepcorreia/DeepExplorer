<?php
class StrategyContext {
    private $strategy = NULL;


    public function __construct($pos) {
        switch ($pos) {
            case "NOUN":
                $this->strategy = new StrategyNoun();
            break;
            case "VERB":
                $this->strategy = new StrategyVerb();
            break;
            case "ADJ":
                $this->strategy = new StrategyAdj();
            break;
            case "ADV":
                $this->strategy = new StrategyAdv();
            break;
        }
    }
    public function getAllDependencies($conn, $word, $pos,  $measure, $limit) {
      return $this->strategy->getDeps($conn, $word, $pos,  $measure, $limit);
    }
}

abstract class DeepStrategyInterface {
    abstract public function getDeps($conn, $word, $pos,  $measure, $limit);

    protected function getDepGovernor($conn, $word, $pos, $dep,$prop,  $measure, $limit) {
        $query= "  SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.".$measure."
            FROM Coocorrencia
            Inner join Palavra on Coocorrencia.idPalavra2 = Palavra.idPalavra
            WHERE Coocorrencia.idPalavra1 = (SELECT idPalavra
                                                          from Palavra
                                                          where palavra= '$word' and
                                                                classe= '$pos' LIMIT 1)
                                                        and Coocorrencia.tipoDep = '$dep'
                                                        and Coocorrencia.nomeProp = '$prop'
            ORDER BY Coocorrencia.".$measure." DESC
            LIMIT $limit";

        $result = $conn->query($query);

        return $result;
    }

   
    protected function getDepDependent($conn, $word, $pos, $dep,$prop,  $measure, $limit) {
        $query= "  SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.".$measure."
            FROM Coocorrencia
            Inner join Palavra on Coocorrencia.idPalavra1 = Palavra.idPalavra
            WHERE Coocorrencia.idPalavra2 = (SELECT idPalavra
                                                          from Palavra
                                                          where palavra= '$word' and
                                                                classe= '$pos' LIMIT 1)
                                                        and Coocorrencia.tipoDep = '$dep'
                                                        and Coocorrencia.nomeProp = '$prop'
            ORDER BY Coocorrencia.".$measure." DESC
            LIMIT $limit";

        $result = $conn->query($query);

        return $result;
    }

    protected function getResult($depProps, $conn, $word, $pos, $measure , $limit){
        $outp = "";
        foreach ($depProps as $depProp){
            $split = split(" ",$depProp);
            $dep = $split[0];
            $prop = $split[1];
            $depProp = $dep."_".$prop;

            $depPos_aux = split("_",$prop);

            if(sizeof($depPos_aux)==3){
                $depPos[0]=$depPos_aux[1];
                $depPos[1]=$depPos_aux[2];
            } else{
                if(sizeof($depPos_aux)==2){
                    $depPos = $depPos_aux;
                }
            }

            if($pos == $depPos[0]){
                    $result = $this->getDepGovernor($conn, $word, $pos, $dep, $prop, $measure ,$limit);
            }else{
               if($pos == $depPos[1]){
                  $result = $this->getDepDependent($conn, $word, $pos, $dep, $prop, $measure ,$limit);
                }
            }

            $outp_aux = "";
            while($rs = $result->fetchArray(SQLITE3_ASSOC)) {
                if ($outp_aux != ""){
                    $outp_aux .= ",";
                }
                $outp_aux .= '{"word":"'  . $rs["palavra"] . '",';
                $outp_aux .= '"measure":"'. $rs["$measure"] . '"}';
            }
            if ($outp != "") {$outp .= ",";}else{$outp .= "{";}
                $outp .= '"'.$depProp.'":['.$outp_aux.']';
        }
        $outp .= "}";
        return $outp; 
    
    }
}

class StrategyNoun extends DeepStrategyInterface {

    public function getDeps($conn, $word, $pos, $measure, $limit) {
        //dependencias que existem no sistema
        $depProps = array("MOD PRE_NOUN_ADV","MOD PRE_NOUN_ADJ","MOD POST_NOUN_PP","MOD POST_NOUN_ADJ");
   
        $result = $this->getResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;
    }

}

class StrategyVerb extends DeepStrategyInterface {

    public function getDeps($conn, $word, $pos, $measure, $limit) {
        //dependencias que existem no sistema
        $depProps = array("SUBJ SEM_PROP", "CDIR SEM_PROP", "COMPLEMENTOS SEM_PROP", "MOD VERB_ADV");
echo "AQUIUIII";
        $result = $this->getResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;
    }
}

class StrategyAdj extends DeepStrategyInterface {
    public function getDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $depProps = array("MOD PRE_ADJ_ADJ","MOD PRE_ADJ_ADV","MOD PRE_NOUN_ADJ","MOD POST_NOUN_ADJ", "MOD POST_ADJ_PP");

        $result = $this->getResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;   
    
    }
}

class StrategyAdv extends DeepStrategyInterface {

    public function getDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $depProps = array("MOD PRE_NOUN_ADV","MOD VERB_ADV","MOD PRE_ADJ_ADV","MOD ADV_ADV","MOD TOP_ADV");
       
        $result = $this->getResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;

    }
}

?>
