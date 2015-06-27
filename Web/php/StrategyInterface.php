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

interface DeepStrategyInterface {
    public function getDeps($conn, $word, $pos,  $measure, $limit);
}
 
class StrategyNoun implements DeepStrategyInterface {
    private function getDepQuery($conn, $word, $pos, $dep,$prop,  $measure, $limit) {
        $query1= "  SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.".$measure."
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

        $result = $conn->query($query1);

        return $result;
    }

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

class StrategyVerb implements DeepStrategyInterface {
    
    private function getDepQuery($conn, $word, $pos, $dep, $prop, $measure, $limit) {
        $query1= "  SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.".$measure."
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

        $result = $conn->query($query1);

        return $result;
    }

    public function getDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $dependencies = array("SUBJ", "CDIR", "MOD", "COMPLEMENTOS");
        $outp = "";
        foreach ($dependencies as $dep){ 
        //por defenição fica agora 25, depois mudar
        
        $result = $this->getDepQuery($conn, $word, $pos, $dep, $prop, $measure ,25);
        $outp_aux = "";
        while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
             if ($outp_aux != "") {$outp_aux .= ",";}
                $outp_aux .= '{"word":"'  . $rs["palavra"] . '",';
                $outp_aux .= '"prop":"'  . $rs["nomeProp"] . '",';
                $outp_aux .= '"measure":"'. $rs["$measure"] . '"}'; 
        }
        if ($outp != "") {$outp .= ",";}else{$outp .= "{";}
            $outp .= '"'.$dep.'":['.$outp_aux.']';
        }
        $outp .= "}";
        return $outp;
    }
}

class StrategyAdj implements DeepStrategyInterface {
    
    private function getDepQuery($conn, $word, $pos, $dep, $measure, $limit) {
        $query1= "  SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.".$measure."
            FROM Coocorrencia
            Inner join Palavra on Coocorrencia.idPalavra2 = Palavra.idPalavra
            WHERE Coocorrencia.idPalavra1 = (SELECT idPalavra
                                                          from Palavra
                                                          where palavra= '$word' and 
                                                                classe= '$pos' LIMIT 1) 
                                                        and Coocorrencia.tipoDep = '$dep'
            ORDER BY Coocorrencia.".$measure." DESC
            LIMIT $limit";

        $result = $conn->query($query1);

        return $result;
    }

    public function getDeps($conn, $word, $pos,  $measure, $limit) {
        //dependencias que existem no sistema
        $dependencies = array("MOD");
        $outp = "";
        foreach ($dependencies as $dep){ 
        //por defenição fica agora 25, depois mudar
        
        $result = $this->getDepQuery($conn, $word, $pos, $dep, $measure ,$limit);
        $outp_aux = "";
        while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
             if ($outp_aux != "") {$outp_aux .= ",";}
                $outp_aux .= '{"word":"'  . $rs["palavra"] . '",';
                $outp_aux .= '"prop":"'  . $rs["nomeProp"] . '",';
                $outp_aux .= '"measure":"'. $rs["$measure"] . '"}'; 
        }
        if ($outp != "") {$outp .= ",";}else{$outp .= "{";}
            $outp .= '"'.$dep.'":['.$outp_aux.']';
        }
        $outp .= "}";
        return $outp;
    }
}

class StrategyAdv implements DeepStrategyInterface {
   
    private function getDepQuery($conn, $word, $pos, $dep, $measure, $limit) {
        $query1= "  SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.".$measure."
            FROM Coocorrencia
            Inner join Palavra on Coocorrencia.idPalavra2 = Palavra.idPalavra
            WHERE Coocorrencia.idPalavra1 = (SELECT idPalavra
                                                          from Palavra
                                                          where palavra= '$word' and 
                                                                classe= '$pos' LIMIT 1) 
                                                        and Coocorrencia.tipoDep = '$dep'
            ORDER BY Coocorrencia.".$measure." DESC
            LIMIT $limit";

        $result = $conn->query($query1);

        return $result;
    }

    public function getDeps($conn, $word, $pos,  $measure, $limit) {
        //dependencias que existem no sistema
        $dependencies = array("SUBJ", "CDIR", "MOD", "COMPLEMENTOS");
        $outp = "";
        foreach ($dependencies as $dep){ 
        //por defenição fica agora 25, depois mudar
        
        $result = $this->getDepQuery($conn, $word, $pos, $dep, $measure ,$limit);
        $outp_aux = "";
        while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
             if ($outp_aux != "") {$outp_aux .= ",";}
                $outp_aux .= '{"word":"'  . $rs["palavra"] . '",';
                $outp_aux .= '"prop":"'  . $rs["nomeProp"] . '",';
                $outp_aux .= '"measure":"'. $rs["$measure"] . '"}'; 
        }
        if ($outp != "") {$outp .= ",";}else{$outp .= "{";}
            $outp .= '"'.$dep.'":['.$outp_aux.']';
        }
        $outp .= "}";
        return $outp;
    }
}

?>