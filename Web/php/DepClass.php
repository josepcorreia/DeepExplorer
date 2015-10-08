<?php
require_once("DepQueries.php");

class DepClass
{
    private $DepQueries = NULL;
    private $pos = NULL;
    private $depProps = NULL;
    private $depPropsNames = NULL;
    private $outp = array();

    public function __construct($pos, $conn){
        $this->pos = $pos;
        $this->GetDependenciesFromFile($pos);
        $this->DepQueries =  new DepQueries($conn);
    }

    public function GetAllDependencies($word, $measure, $limit, $minfreq) {
        $result = $this->GetResult($word, $this->pos,  $measure, $limit, $minfreq);
        return $result;
    }

    protected function GetDependenciesFromFile($pos)
    {
        $file = fopen('resources/dep_prop.txt','r');
        while ($line = fgets($file)) {
            $line = trim(preg_replace('/\n/', '', $line));
            $split = explode(" ",$line);

            if( $split[0] ===  '##') {
                if($split[1] === $pos) {
                    $result = $this->GetDependenciesByClass($file);
                    fclose($file);
                    return;
                }
            }
        }
        
    }

     protected function GetDependenciesByClass($file){
        $deps = array();

        while ($line = fgets($file)) {
            $line = trim(preg_replace('/\n/', '', $line));

            //caso da linha em brnaco do ficheiro
            if($line === ""){
                continue;
            }

            $test = explode(" ",$line);
            if( $test[0] ===  '##'){
                break;
            }
            $split = explode(",",$line);

            //add to array
            $this->depProps[$split[1]][$split[0]] = $split[2];
            $this->depPropsNames[$split[1]][$split[0]] = $split[3];
            $this->outp[$split[1]] = null;
        }
        return;
    }

     private function logarithmBase2($freq)
    {
        return (log10($freq) / log10(2));
    }

    private function roundMeasure($measure, $number)
    {
        switch ($measure) {
            case 'Dice':
                return round($number,3);
                break;
            case 'LogDice':
                return round($number,1);
                break;
            case 'PMI':
                return round($number,2);
                break;
            case 'ChiPearson':
                return round($number,1);
                break;
            case 'LogLikelihood':
                return round($number,1);
                break;
            case 'Significance':
                return round($number,2);
                break;
            case 'frequencia':
                return $number;
                break;
        }
    }

    protected function GetResult($word, $pos, $measure , $limit, $minfreq){
        

        $idWord = $this->DepQueries->GetWordId($word, $pos);
        
        $depPropRoles = array_keys($this->depProps);

        foreach ($depPropRoles as $depPropRole){
            $depPropsKeys = array_keys($this->depProps[$depPropRole]);

            foreach ($depPropsKeys as $dep_prop){
                $split = explode(" ",$dep_prop);
                $dep = $split[0];
                $prop = $split[1];
                $depProp = $dep."_".$prop;
                
                $depPropName = $this->depPropsNames[$depPropRole][$dep_prop];
                $depType = $this->depProps[$depPropRole][$dep_prop];
                
                $result = $this->DepQueries->GetDepData($idWord, $dep, $prop, $measure ,$limit, $depType, $minfreq);
       
                $words_array = array();
                while($rs = $result->fetchArray(SQLITE3_ASSOC)) {
                    $result_array = array();

                    $result_array["word"] = $rs["palavra"];
                    $result_array["word_pos"] = $rs["classe"];
                    $result_array["measure"] = $this->roundMeasure($measure,$rs[$measure]);
                    $result_array["frequency"] = $rs["frequencia"];
                    $result_array["duallog"] = round($this->logarithmBase2($rs["frequencia"]));
                    array_push($words_array,$result_array);
                }

                if(count($words_array) > 0){
                    $depProp_array = array();
                    $depProp_array["name"] = $depPropName;
                    $depProp_array["dep"] = $dep;
                    $depProp_array["prop"] = $prop;
                    $depProp_array["dep_type"] = $depType;
                    $depProp_array["data"] = $words_array;

                    $this->outp[$depPropRole][$depProp] = $depProp_array;
                }
            }
        }
        return $this->outp;
    }
}
