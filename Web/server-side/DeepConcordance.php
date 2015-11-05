<?php
require_once("DeepQueries.php");

class DeepConcordance
{
    private $DepQueries = NULL;

    public function __construct($conn){
        $this->DepQueries =  new DepQueries($conn);
    }

    function GetResult($word1, $pos1, $word2, $pos2 ,$dep , $prop, $limit){     
    	$outp;

        $id1= $this->DepQueries->GetWordId($word1, $pos1);
    	$id2= $this->DepQueries->GetWordId($word2, $pos2);	

        $result = $this->DepQueries->GetConcordance($id1,$id2, $dep, $prop, $limit);

       	$sentences_array = array();

        while($rs = $result->fetchArray(SQLITE3_ASSOC)) {
            $result_array = array();

            $result_array["file"] = $rs['nomeFicheiro'];
            $result_array["number"] = $rs['numeroFrase'];
            $result_array["sentence"] = $rs['frase'];
      
            array_push($sentences_array,$result_array);
            }
        $outp['sentences'] = $sentences_array;

        return $outp;
    }

}
?>
