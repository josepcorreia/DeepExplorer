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

    protected function GetWordId($conn, $word, $pos)
    {
        $query =   "SELECT idPalavra 
                    FROM Palavra 
                    WHERE palavra = '".$word."' 
                    and classe = '".$pos."' 
                    LIMIT 1;";
    
        $result = $conn->query($query);

        $rs = $result->fetchArray(SQLITE3_ASSOC);

        return $rs["idPalavra"];
    }

    protected function GetDepFromWord2($conn, $idWord2, $dep, $prop,  $measure, $limit) {
        $query= "SELECT Palavra.palavra, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra1,".$measure.",frequencia
                        FROM (SELECT * 
                             FROM Coocorrencia 
                             WHERE frequencia > 1)
                        WHERE idPalavra2 = ".$idWord2."
                        and tipoDep = '".$dep."'
                        and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra1 = Palavra.idPalavra;";
            
        $result = $conn->query($query);

        return $result;
    }
   
    protected function GetDepFromWord1($conn, $idWord1, $dep, $prop,  $measure, $limit) {
        $query= "SELECT Palavra.palavra, Coo.".$measure.", Coo.frequencia
                 FROM ( SELECT idPalavra2,".$measure.",frequencia
                        FROM (SELECT * 
                             FROM Coocorrencia 
                             WHERE frequencia > 1)
                        WHERE idPalavra1 =".$idWord1."
                        and tipoDep = '".$dep."'
                        and nomeProp = '".$prop."'
                        ORDER BY ".$measure." DESC
                        LIMIT ".$limit. ") as Coo
                Inner join Palavra on Coo.idPalavra2 = Palavra.idPalavra;";

        $result = $conn->query($query);
      
        return $result;
    }

    protected function GetResult($depProps, $conn, $word, $pos, $measure , $limit){
        
        $idWord = $this->GetWordId($conn, $word, $pos);

        $depPropsKeys = array_keys($depProps);

        $outp = "";
        foreach ($depPropsKeys as $depProp){
            $split = split(" ",$depProp);
            
            $dep = $split[0];
            $prop = $split[1];

            $depType = $depProps[$depProp];
            
            //tem que se mudar porque nao sei se assim vai funcionar
            switch ($depType) {
                case 'PRE_GOVERNED':
                    $result = $this->GetDepFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit);
                break;
                case 'PRE_GOVERNOR':
                    $result = $this->GetDepFromWord2($conn, $idWord, $dep, $prop, $measure ,$limit);
                break;
                case 'POST_GOVERNED':
                    $result = $this->GetDepFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit);
                break;
                case 'POST_GOVERNOR':
                    $result = $this->GetDepFromWord2($conn, $idWord, $dep, $prop, $measure ,$limit);
                break;                
            }

            $outp_aux = ""; 

            $depProp = $dep."_".$prop;
            
            
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
        $this->depProps = $deps;
    }

    public function GetDeps($conn, $word, $pos, $measure, $limit) {
        $result = $this->GetResult($this->depProps, $conn, $word, $pos,  $measure, $limit);
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
