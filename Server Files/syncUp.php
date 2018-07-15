<?php

/* Updating the remote database table "[username]VocabTable" with dirty rows from local database*/

$servername = "localhost";
$username = "id3423136_301232792";
$password = ***;
$dbname = "id3423136_multilingo";

$allLangsString = "fr de es it en";
$allLangs = explode(" ", $allLangsString);

$user = $_POST["username"];
$jsonString = $_POST["jsonString"];
$dirtyRows = json_decode($jsonString, true);
	
$response = array();
$response["allLangs"] = $allLangs;
$response["success"] = false; 
$response["tableCreated"] = false;

$con = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
// set the PDO error mode to exception
$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

//create table if it doesn't exist
$tableName = "`$user"."VocabTable`";
$sqlCreateTable = "CREATE TABLE IF NOT EXISTS $tableName ( source VARCHAR(100) NOT NULL PRIMARY KEY";
foreach ($allLangs as $col) {
  $sqlCreateTable .= ', '.$col.' VARCHAR(100) NULL';
}
$sqlCreateTable .= ', status TINYINT NOT NULL);';
try {
	$stmt = $con->prepare($sqlCreateTable);
	if($stmt->execute()){
		$response["tableCreated"] = true;
	}
} 
catch(PDOException $e) {
	trigger_error('Wrong SQL: ' . $sqlCreateTable . ' Error: ' . $e->getMessage(), E_USER_ERROR);
}

// prepare sql
function toString($columnsArray){
	/* turns ["source", "fr"] into '`source`, `fr`' */
  $res = "";
  foreach ($columnsArray as $item) {
    $res .= "`".$item."`,";
  }
  $res .= "`status`";
  return $res;
}

foreach ($dirtyRows as $dirtyRow) {
	$cols = toString(array_keys($dirtyRow));
	$values = array_values($dirtyRow);
    $values[] = 1; // appending 'status' value 1
    
	$sql = "REPLACE INTO $tableName ($cols) VALUES (";

	foreach ($values as $item) {
		$sql .= '?, ';
	}
	$sql[-2] = ")";

	try {
		$stmt = $con->prepare($sql);
		if($stmt->execute($values)){
			$response["success"] = true;
		}
	} 
	catch(PDOException $e) {
		trigger_error('Wrong SQL: ' . $sql . ' Error: ' . $e->getMessage(), E_USER_ERROR);
	}
}

echo json_encode($response);

?>
