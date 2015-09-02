<?php
ini_set('display_errors', 1);
require_once("DepClass.php");

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


$word = $_POST['word'];
$pos = $_POST['pos'];
$Measure = $_POST['measure'];
//limit
//minimo de ocorrncias

if($Measure == 'Frequência'){
	$Measure = 'frequencia';
}

/*
$word = "carro";
$pos = "NOUN";
$Measure = "Dice";

/*
$word = "ser";
$pos = "VERB";
$Measure = "Dice";
*/

/*
$word = "bonito";
$pos = "ADJ";
$Measure = "Dice";
*/

$depInstance = new DepClass($pos);
$deps = $depInstance->GetAllDependencies($conn,$word,$Measure,10);

echo(json_encode($deps, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE ));

$conn->close();

?>
