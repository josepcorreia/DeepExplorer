<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

//$data = json_decode($_POST['myData']);
//$data = json_decode(file_get_contents("php://input"));

$word = $_POST['word'];
$pos = $_POST['pos'];

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
//echo "Connected successfully";

$result = $conn->query("SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.frequencia 
						from Coocorrencia
						Inner join Palavra on Coocorrencia.idPalavra2 = Palavra.idPalavra
						where Coocorrencia.idPalavra1 = (SELECT idPalavra from Palavra where palavra= '$word' and classe= '$pos' LIMIT 1) and Coocorrencia.tipoDep = 'MOD'
						order by Coocorrencia.frequencia desc
						LIMIT 25");

$outp = "";

while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
    if ($outp != "") {$outp .= ",";}
		$outp .= '{"word":"'  . $rs["palavra"] . '",';
 		$outp .= '"prop":"'  . $rs["nomeProp"] . '",';
    	$outp .= '"frequency":"'. $rs["frequencia"] . '"}'; 
}

$outp ='{"Mod":['.$outp.']}';

echo($outp);

$conn->close();


?>