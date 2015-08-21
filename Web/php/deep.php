<?php
require_once("StrategyInterface.php");

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

//$dir = "/Users/josecorreia/Projects/DB_site" ;
$dir = "/home/josepcorreia/Projects/DB_site";
$filename = "db_deep.db";

// Create connection
$conn = new SQLite3($dir."/".$filename);
//$conn->set_charset('utf8');

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$word = $_POST['word'];
$pos = $_POST['pos'];
$Measure = $_POST['measure'];

if($Measure == 'Frequência'){
	$Measure = 'frequencia';
}

/*
$word = "carro";
$pos = "NOUN";
$Measure = "Dice";
*/
/*
$word = "ser";
$pos = "VERB";
$Measure = "Dice";
*/
$strategyContext = new StrategyContext($pos);

$deps = $strategyContext->GetAllDependencies($conn,$word,$pos,$Measure,10);

echo(json_encode($deps, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE ));

$conn->close();

?>
