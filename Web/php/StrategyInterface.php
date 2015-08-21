<?php
class StrategyContext {
    private $strategy = NULL;

    public function __construct($pos) {
        switch ($pos) {
            case 'NOUN':
                $this->strategy = new StrategyNoun($pos);
            break;
            case 'VERB':                
                $this->strategy = new StrategyVerb($pos);
            break;
            case 'ADJ':
                $this->strategy = new StrategyAdj($pos);
            break;
            case 'ADV':
                $this->strategy = new StrategyAdv($pos);
            break;
        }
    }
    public function GetAllDependencies($conn, $word, $pos,  $measure, $limit) {
      return $this->strategy->GetDeps($conn, $word, $pos,  $measure, $limit);
    }
}

abstract class DeepStrategyInterface {
    abstract public function GetDeps($conn, $word, $pos,  $measure, $limit);

    protected function GetDependenciesFromFile($pos)
    {
        $file = fopen('resources/dep_prop.txt','r');
            while ($line = fgets($file)) {
                $line = trim(preg_replace('/\n/', '', $line));
                $split = split(" ",$line);

                if( $split[0] ===  '##') {
                    if($split[1] === $pos) {
                        return $this->GetDependenciesByClass($file);  
                    }
                }            
            }
        fclose($file);
    }
    protected function GetDependenciesByClass($file){
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
        $outp = array();
        $pre_governor = $pre_governed = array();
        $post_governor = $post_governed = array();
        
        $idWord = $this->GetWordId($conn, $word, $pos);

        $depPropsKeys = array_keys($depProps);

        foreach ($depPropsKeys as $depProp){
            $depProp_array = array();

            $split = split(" ",$depProp);
            
            $dep = $split[0];
            $prop = $split[1];

            $depType = $depProps[$depProp];
            
            if($prop === "SEM_PROP" ||$prop === "VERB_NOUN" || $prop === "VERB_ADJ"){
                //no caso do SUBJ, CDIR,CINDIR e COMPL
                $result = $this->GetDepFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit);
            }
            else{ 
                if (strpos($depType,'GOVERNED') !== false) {
                    $result = $this->GetDepFromWord1($conn, $idWord, $dep, $prop, $measure ,$limit);
                }
                else{
                    if (strpos($depType,'GOVERNOR') !== false) {
                        $result = $this->GetDepFromWord2($conn, $idWord, $dep, $prop, $measure ,$limit);
                    }
                }
            }

            $depProp = $dep."_".$prop;

            $words_array = array();

            while($rs = $result->fetchArray(SQLITE3_ASSOC)) {
                $word_array = array();

                $word_array["word"] = $rs["palavra"];
                $word_array["measure"] = $rs[$measure];
                $word_array["frequency"] = $rs["frequencia"];
            
                array_push($words_array,$word_array); 
            }

            if(count($words_array) > 0){
                //$depProp_array[$depProp] = $words_array; 

                switch ($depType) {
                    case 'PRE_GOVERNED':
                        $pre_governed[$depProp] = $words_array;
                    break;
                    case 'PRE_GOVERNOR':                
                        $pre_governor[$depProp] = $words_array;
                    break;
                    case 'POST_GOVERNED':
                        $post_governed[$depProp] = $words_array;
                    break;
                    case 'POST_GOVERNOR':                
                        $post_governor[$depProp] = $words_array;
                    break;
                }
            }
       }
        if(count($pre_governed) > 0){
            $outp['PRE_GOVERNED']= $pre_governed;
        }

        if(count($pre_governor) > 0){
            $outp['PRE_GOVERNOR']= $pre_governor;
        }

        if(count($post_governed) > 0){
            $outp['POST_GOVERNED']= $post_governed;
        }
        if(count($post_governor) > 0){
            $outp['POST_GOVERNOR']= $post_governor;
        }

        return $outp; 
    }
}

class StrategyNoun extends DeepStrategyInterface {
    private $pos = NULL;
    private $depProps = NULL;

    public function __construct($pos){
        $this->pos = $pos;
        $this->depProps = $this->GetDependenciesFromFile($pos);
    }

    public function GetDeps($conn, $word, $pos, $measure, $limit) {
        $result = $this->GetResult($this->depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;
    }
}

class StrategyVerb extends DeepStrategyInterface {
    private $pos;
    private $depProps = NULL;

    public function __construct($pos){
        $this->pos = $pos;
        $this->depProps = $this->GetDependenciesFromFile($pos);
    }
    
    public function GetDeps($conn, $word, $pos, $measure, $limit) {
        
        $result = $this->GetResult($this->depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;
    }
}

class StrategyAdj extends DeepStrategyInterface {
    private $pos;
    private $depProps = NULL;

    public function __construct($pos){
        $this->pos = $pos;
        $this->depProps = $this->GetDependenciesFromFile($pos);
    }
    
    public function GetDeps($conn, $word, $pos,  $measure, $limit) {

        $result = $this->GetResult($this->depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;   
    
    }
}

class StrategyAdv extends DeepStrategyInterface {
    private $pos;
    private $depProps = NULL;

    public function __construct($pos){
        $this->pos = $pos;
        $this->depProps = $this->GetDependenciesFromFile($pos);
    }
    
    public function GetDeps($conn, $word, $pos,  $measure, $limit) {
       
        $result = $this->GetResult($this->depProps, $conn, $word, $pos,  $measure, $limit);
        return $result;

    }
}

?>
