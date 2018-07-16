<?php
    $con = mysqli_connect("localhost", "id3423136_301232792", *****, "id3423136_multilingo");
    
    $user = $_POST["LoggedInUser"];
    $tableName = "`$user"."VocabTable`";
    $q = mysqli_query($con, "SELECT * FROM $tableName WHERE status = 0");
    $rows = array();
    
    while($r = mysqli_fetch_assoc($q)) {
        $rows[] = $r;
    }
    echo json_encode($rows);
?>
