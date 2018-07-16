<?php
    // When syncDown is completed, we need to set the status of all rows to 1.
    
    $con = mysqli_connect("localhost", "id3423136_301232792", *****, "id3423136_multilingo");
    
    $user = $_POST["LoggedInUser"];
    $tableName = "`$user"."VocabTable`";
    
    $statement = mysqli_prepare($con, "UPDATE $tableName SET status=1");
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);     
?>
