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

    //"The first argument of a binary dependency corresponds to  the governor while the second to a dependent of this governor."
    //retorna as palavras que se relacionam com a palavra gorvernadora da dependencia
    protected function getDepGovernor($conn, $word, $pos, $dep,$prop,  $measure, $limit) {
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

    //retorna as palavras governadoras da palavra dependece
    protected function getDepDependent($conn, $word, $pos, $dep,$prop,  $measure, $limit) {
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
}
 
class StrategyNoun extends DeepStrategyInterface {

    public function getDeps($conn, $word, $pos,  $measure, $limit) {
        //dependencias que existem no sistema
        $depProps = array("MOD PRE_NOUN_ADV","MOD PRE_NOUN_ADJ","MOD POST_NOUN_PP","MOD POST_NOUN_ADJ");
        $outp = "";
        foreach ($depProps as $depProp){ 
            $split = split(" ",$depProp);
            $dep = $split[0];
            $prop = $split[1];
            $depProp = $dep."_".$prop;

            $result = $this->getDepQuery($conn, $word, $pos, $dep, $prop, $measure ,$limit);
           
            $outp_aux = "";
            while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
                if ($outp_aux != "") {$outp_aux .= ",";}
                $outp_aux .= '{"word":"'  . $rs["palavra"] . '",';
                $outp_aux .= '"measure":"'. $rs["$measure"] . '"}'; 
            }
            if ($outp != ""){
                $outp .= ",";
            }
            else{
                $outp .= "{";
            }
                $outp .= '"'.$depProp.'":['.$outp_aux.']';
        }
        $outp .= "}";
        return $outp;
    }
    
}

class StrategyVerb extends DeepStrategyInterface {

    public function getDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $depProps = array("SUBJ SEM_PROP", "CDIR SEM_PROP", "COMPLEMENTOS SEM_PROP", "MOD VERB_ADV");
        $outp = "";
        
        foreach ($depProps as $depProp){ 
            $split = split(" ",$depProp);
            $dep = $split[0];
            $prop = $split[1];
            $depProp = $dep."_".$prop;

        //por defenição fica agora 25, depois mudar
        
        $result = $this->getDepQuery($conn, $word, $pos, $dep, $prop, $measure ,25);
        $outp_aux = "";
        while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
             if ($outp_aux != "") {$outp_aux .= ",";}
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

class StrategyAdj extends DeepStrategyInterface {

    public function getDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $depProps = array("MOD PRE_ADJ_ADJ","MOD PRE_ADJ_ADV","MOD PRE_NOUN_ADJ","MOD POST_NOUN_ADJ", "MOD POST_ADJ_PP");
        $outp = "";
        
        foreach ($depProps as $depProp){ 
            $split = split(" ",$depProp);
            $dep = $split[0];
            $prop = $split[1];
            $depProp = $dep."_".$prop;

        //por defenição fica agora 25, depois mudar
        
        $result = $this->getDepQuery($conn, $word, $pos, $dep, $prop, $measure ,25);
        $outp_aux = "";
        while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
             if ($outp_aux != "") {$outp_aux .= ",";}
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

class StrategyAdv extends DeepStrategyInterface {
   
    public function getDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $depProps = array("MOD PRE_NOUN_ADV","MOD VERB_ADV","MOD PRE_ADJ_ADV","MOD ADV_ADV","MOD TOP_ADV");
        $outp = "";
        
        foreach ($depProps as $depProp){ 
            $split = split(" ",$depProp);
            $dep = $split[0];
            $prop = $split[1];
            $depProp = $dep."_".$prop;

        //por defenição fica agora 25, depois mudar
        
        $result = $this->getDepQuery($conn, $word, $pos, $dep, $prop, $measure ,25);
        $outp_aux = "";
        while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
             if ($outp_aux != "") {$outp_aux .= ",";}
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

?>