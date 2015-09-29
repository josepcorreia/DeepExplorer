<?php
ini_set('display_errors', 1);
require_once("DepQueries.php");

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$dir = "/Users/josecorreia/Projects/DB_site" ;
//$dir = "/Users/josecorreia/Projects/DB" ;
//$dir = "/home/josepcorreia/Projects/DB_site";
$filename = "db_deep.db";

// Create connection
$conn = new SQLite3($dir."/".$filename);

//pode ser preciso
//sqlite_busy_timeout($dbhandle, 100000); // set timeout to 100 seconds


$word1 = $_POST['word1'];
$pos1 = $_POST['pos1'];
$word2 = $_POST['word2'];
$pos2 = $_POST['pos2'];
$dep = $_POST['dep'];
$prop =  $_POST['prop'];

/*
$word1 = "volante";
$pos1 = "NOUN";
$word2 = "carro";
$pos2 = "NOUN";
$dep = "MOD";
$prop =  "POST_NOUN_NOUN";
*/



function GetResult($word1, $pos1, $word2, $pos2 ,$dep , $prop, $conn){     
	$outp;

	$DepQueries =  new DepQueries($conn);
    $id1= $DepQueries->GetWordId($word1, $pos1);
	$id2= $DepQueries->GetWordId($word2, $pos2);	

    $result = $DepQueries->GetConcordance($id1,$id2, $dep, $prop);

   	$sentences_array = array();

    while($rs = $result->fetchArray(SQLITE3_ASSOC)) {
        $result_array = array();

        $result_array["file"] = $rs['Frase.nomeFicheiro'];
        $result_array["number"] = $rs['Frase.numeroFrase'];
        $result_array["sentence"] = $rs['Frase.frase'];
  
        array_push($sentences_array,$result_array);
        }
    $outp['sentences'] = $sentences_array;

    return $outp;
    }

echo(json_encode(GetResult($word1, $pos1, $word2, $pos2 ,$dep , $prop, $conn), JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE ));

$conn->close();

?>
