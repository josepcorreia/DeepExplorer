<?php
class StrategyContext {
    private $strategy = NULL;

    public function __construct($pos) {
        switch ($pos) {
            case 'NOUN':
                $deps = $this->GetDependenciesFromFile($pos);
                $this->strategy = new StrategyNoun($deps);
            break;
            case 'VERB':
                $deps = $this->GetDependenciesFromFile($pos);
                //$this->strategy = new StrategyVerb();
            break;
            case 'ADJ':
                $deps = $this->GetDependenciesFromFile($pos);
                //$this->strategy = new StrategyAdj();
            break;
            case 'ADV':
                $deps = $this->GetDependenciesFromFile($pos);
                //$this->strategy = new StrategyAdv();
            break;
        }
    }
    public function GetDependenciesFromFile($pos)
    {
        $file = fopen('resources/dep_prop.txt','r');
            while ($line = fgets($file)) {
                $line = trim(preg_replace('/\n/', '', $line));
                $split = split(" ",$line);

                if( $split[0] ===  '##') {
                    if($split[1] === $pos) {
                        return $this->GetDependenciesByClass($file, $pos);  
                    }
                }            
            }
        fclose($file);
    }
    private function GetDependenciesByClass($file,$pos){
        $deps = array();

        while ($line = fgets($file)) {
            $line = trim(preg_replace('/\n/', '', $line));
            
            //caso da linha em brnaco do ficheiro
            if($line === ""){
                continue;
            } 

            $test = split(" ",$line);
            if( $test[0] ===  '##'){             
              break;  
            }
            $split = split(",",$line);

            //add to array
            $deps[$split[0]] = $split[1];
        }
        return $deps;
    }

    public function GetAllDependencies($conn, $word, $pos,  $measure, $limit) {
      return $this->strategy->GetDeps($conn, $word, $pos,  $measure, $limit);
    }
}

abstract class DeepStrategyInterface {
    abstract public function GetDeps($conn, $word, $pos,  $measure, $limit);

    protected function GetDepGovernor($conn, $word, $pos, $dep,$prop,  $measure, $limit) {
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

   
    protected function GetDepDependent($conn, $word, $pos, $dep,$prop,  $measure, $limit) {
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

    protected function GetResult($depProps, $conn, $word, $pos, $measure , $limit){
        //agora $depProps e um hashmap, tem que se extrair as propriedades para 
        //depois sim ser possivel obter os resultados

        //array_keys() 

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

            if($pos === $depPos[0]){
                    $result = $this->GetDepGovernor($conn, $word, $pos, $dep, $prop, $measure ,$limit);
            }else{
               if($pos === $depPos[1]){
                  $result = $this->GetDepDependent($conn, $word, $pos, $dep, $prop, $measure ,$limit);
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
    private $depProps = NULL;

    public function __construct($deps) {
        $this->depsProps = $deps;
    }

    public function GetDeps($conn, $word, $pos, $measure, $limit) {

        $result = $this->GetResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;
    }

}

class StrategyVerb extends DeepStrategyInterface {

    public function GetDeps($conn, $word, $pos, $measure, $limit) {
        //dependencias que existem no sistema
        $depProps = array("SUBJ SEM_PROP", "CDIR SEM_PROP", "COMPLEMENTOS SEM_PROP", "MOD VERB_ADV");

        $result = $this->GetResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;
    }
}

class StrategyAdj extends DeepStrategyInterface {
    public function GetDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $depProps = array("MOD PRE_ADJ_ADJ","MOD PRE_ADJ_ADV","MOD PRE_NOUN_ADJ","MOD POST_NOUN_ADJ", "MOD POST_ADJ_PP");

        $result = $this->GetResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;   
    
    }
}

class StrategyAdv extends DeepStrategyInterface {

    public function GetDeps($conn, $word, $pos,  $measure, $limit) {
       //dependencias que existem no sistema
        $depProps = array("MOD PRE_NOUN_ADV","MOD VERB_ADV","MOD PRE_ADJ_ADV","MOD ADV_ADV","MOD TOP_ADV");
       
        $result = $this->GetResult($depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;

    }
}

?>
