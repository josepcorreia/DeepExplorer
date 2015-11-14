<?php
ini_set('display_errors', 1);
require_once("DeepWord.php");
require_once("DeepConcordance.php");

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$dir = "/Users/josecorreia/Projects/DB_site" ;
$filename = "db_deep.db";

// Create connection
$conn = new SQLite3($dir."/".$filename);

ini_set('max_execution_time', 300);

$request_type = $_POST['request_type']; 

if($request_type === 'word'){
	$word = $_POST['word'];
	$pos = $_POST['pos'];
	$Measure = $_POST['measure'];
	$limit =  $_POST['limit'];
	$minfreq = $_POST['minfreq'];

	if($Measure == 'Frequency'){
		$Measure = 'frequencia';
	}

	/*
	$word = "carro";
	$pos = "NOUN";
	$Measure = "Dice";
	$limit =  10;
	$minfreq = 2;
	*/

	$deepWordInstance = new DeepWord($pos, $conn);
	$deps = $deepWordInstance->GetAllDependencies($word,$Measure,$limit, $minfreq);

	echo(json_encode($deps, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE ));

} else if($request_type === 'concordance'){
	$word1 = $_POST['word1'];
	$pos1 = $_POST['pos1'];
	$word2 = $_POST['word2'];
	$pos2 = $_POST['pos2'];
	$dep = $_POST['dep'];
	$prop =  $_POST['prop'];
	$freq = $_POST['freq'];

	
	/*$word1 = "volante";
	$pos1 = "NOUN";
	$word2 = "carro";
	$pos2 = "NOUN";
	$dep = "MOD";
	$prop =  "POST_NOUN_NOUN";
	$freq=10;
	*/

	//max number of setences that exemplifies a coocorrence 
	if($freq >10 ){
    	$freq=10;
	}

	$deepConcordanceInstance = new DeepConcordance($conn);
	echo(json_encode($deepConcordanceInstance->GetResult($word1, $pos1, $word2, $pos2 ,$dep , $prop, $freq), JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE ));
}

$conn->close();

?>
