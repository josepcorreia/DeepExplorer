<?php
require_once("StrategyInterface.php"); 
//use Abraham\TwitterOAuth\TwitterOAuth;

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$servername = "127.0.0.1";
$username = "jcorreia";
$password = "deepexplorer";
// Create connection
$conn = new mysqli($servername, $username, $password, 'db_deep');
$conn->set_charset('utf8');

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

$strategyContext = new StrategyContext($pos);

$deps = '{"DEPS":'.$strategyContext->getAllDependencies($conn,$word,$pos,$Measure,25).'}';

echo($deps);

$conn->close();

?>