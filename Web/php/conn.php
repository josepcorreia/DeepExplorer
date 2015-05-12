<?php
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

//$data = json_decode($_POST['myData']);
//$data = json_decode(file_get_contents("php://input"));
$word = $_POST['word'];
$pos = $_POST['pos'];
//$word = "ser";
//$pos = "VERB";


function getDepQuery($conn, $word, $pos, $dep, $limit) { 
    
$result = $conn->query("SELECT Palavra.palavra, Coocorrencia.nomeProp, Coocorrencia.frequencia 
						from Coocorrencia
						Inner join Palavra on Coocorrencia.idPalavra2 = Palavra.idPalavra
						where Coocorrencia.idPalavra1 = (SELECT idPalavra 
														  from Palavra 
														  where palavra= '$word' and 
														  		classe= '$pos' LIMIT 1) 
														and Coocorrencia.tipoDep = '$dep'
						order by Coocorrencia.frequencia desc
						LIMIT 25");

return $result;
}



function getAllDependencies($conn, $word, $pos){
	//dependencias que existem no sistema
	$dependencies = array("SUBJ", "CDIR", "MOD", "ATTRIB");
	$outp = "";
	foreach ($dependencies as $dep){ 
		//por defenição fica agora 25, depois mudar
		$result = getDepQuery($conn, $word, $pos, $dep, 25);
		$outp_aux = "";
		while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
   			 if ($outp_aux != "") {$outp_aux .= ",";}
				$outp_aux .= '{"word":"'  . $rs["palavra"] . '",';
 				$outp_aux .= '"prop":"'  . $rs["nomeProp"] . '",';
    			$outp_aux .= '"frequency":"'. $rs["frequencia"] . '"}'; 
		}
		if ($outp != "") {$outp .= ",";}else{$outp .= "{";}
		$outp .= '"'.$dep.'":['.$outp_aux.']';
	}
	$outp .= "}";
	return $outp;
}


$deps = '{"DEPS":'.getAllDependencies($conn, $word, $pos).'}';

echo($deps);

$conn->close();


?>