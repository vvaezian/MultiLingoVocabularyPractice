
<?php
    require("password.php");
    $con = mysqli_connect("localhost", "id3423136_301232792", "[MY-PASSWORD]", "id3423136_multilingo");
    
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM Users WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $colUserID, $colUsername, $colPassword);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        if (password_verify($password, $colPassword)) {
            $response["success"] = true;  
            $response["username"] = $colUsername;
          #  $response["age"] = $colAge;
        }
    }
    echo json_encode($response);
?>
