<?php
require_once("DepStrategy.php");

class DepClass
{
    private $pos = NULL;
    private $depProps = NULL;
    private $depTitles = NULL;

    public function __construct($pos){
        $this->pos = $pos;
        $this->depProps = $this->GetDependenciesFromFile($pos);
        $this->depTitles = $this->GetDependenciesTitleFromFile($pos);
    }

    public function GetAllDependencies($conn, $word, $measure, $limit, $minfreq) {
        $result = $this->GetResult($this->depProps, $conn, $word, $this->pos,  $measure, $limit, $minfreq);
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
                    return $result;
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
            $deps[$split[0]] = $split[1];
        }
        return $deps;
    }

    protected function GetDependenciesTitleFromFile($pos)
    {
        $file = fopen('resources/dep_prop.txt','r');
        while ($line = fgets($file)) {
            $line = trim(preg_replace('/\n/', '', $line));
            $split = explode(" ",$line);

            if( $split[0] ===  '##') {
                if($split[1] === $pos) {
                    $result = $this->GetDependenciesTitleByClass($file);
                    fclose($file);
                    return $result;
                }
            }
        }
        
    }
     protected function GetDependenciesTitleByClass($file){
        $depTitles = array();

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
            $depTitles[$split[0]] = $split[2];
        }
        return $depTitles;
    }

    private function GetWordId($conn, $word, $pos)
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

    protected function GetResult($depProps, $conn, $word, $pos, $measure , $limit, $minfreq){
        $outp = array();

        $idWord = $this->GetWordId($conn, $word, $pos);
        $depPropsKeys = array_keys($depProps);

        foreach ($depPropsKeys as $depProp){
            $depType = $depProps[$depProp];
            $depPropName = $this->depTitles[$depProp];

            $split = explode(" ",$depProp);
            $dep = $split[0];
            $prop = $split[1];
            $depProp = $dep."_".$prop;

            $depStrategy = new DepStrategy($dep);
            $result = $depStrategy->GetDepData($conn, $idWord, $dep, $prop, $measure ,$limit, $depType, $minfreq);

            $words_array = array();
            while($rs = $result->fetchArray(SQLITE3_ASSOC)) {
                $result_array = array();

                $result_array["word"] = $rs["palavra"];
                $result_array["measure"] = $this->roundMeasure($measure,$rs[$measure]);
                $result_array["frequency"] = $rs["frequencia"];
                $result_array["duallog"] = round($this->logarithmBase2($rs["frequencia"]));
                array_push($words_array,$result_array);
            }

            if(count($words_array) > 0){
                $depProp_array = array();
                $depProp_array["name"] = $depPropName;
                $depProp_array["data"] = $words_array;

                $outp[$depType][$depProp] = $depProp_array;
            }
        }
        return $outp;
    }
}
