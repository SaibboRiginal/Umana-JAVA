<?php 
$which_db = "remoto";
//$which_db = "locale";
$json = file_get_contents("setting.json");
$data = json_decode($json, true);

function check_auth() {
    /*
        Function: Check if sessions is logged in. Returns true if connected, false if NOT connected
    */
    if (isset($_SESSION['username']) and $_SESSION['loggedin'] == true or isset($_COOKIE["loggedin"])) {
        return true; // Logged IN
    } else {
        return false; // Logged OUT 
    }
}

function check_login() {
    /*
        Function: Check if login form was sign in and sign out
    */
    if ($_SERVER['REQUEST_METHOD'] == 'POST') { // Login or Logout
        if (isset($_POST['login'])) {
            logC("Login");
            login();
            unset($_POST['login']);
        }
        else if (isset($_POST['logout'])) {
            logC("Logout");
            logout();
            unset($_POST['logout']);
        }
        else if (isset($_POST['register'])) {
            logC("Register");
            register();
            unset($_POST['register']);
        }
    }
}

function check_AJAX() {
    /*
    
    */
    global $data;
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        if (isset($_POST['bonifico'])) {
            logC("bonifico");
            $dest = trim($_POST['dest']); // Gets username from form
            $importo = trim($_POST['importo']); // Gets password from form
            $da = trim($_POST['da']); // Gets password from form

            $idda = query("SELECT ".$data['tables']['conto']['name'].".".$data['tables']['conto']['pk']
                ." FROM ".$data['tables']['credenzialiutente']['name']
                .", ".$data['tables']['anagrafica']['name']
                .", ".$data['tables']['conto']['name']
                ." WHERE "
                .$data['tables']['credenzialiutente']['pk']."='".$da."'"
                ." AND ".$data['tables']['credenzialiutente']['pk1']."=".$data['tables']['anagrafica']['pk']
                ." AND ".$data['tables']['anagrafica']['cols'][2]."=".$data['tables']['conto']['pk']
                )[$data['tables']['conto']['pk']][0];
                
            $iddest = query("SELECT ".$data['tables']['conto']['name'].".".$data['tables']['conto']['pk']
                ." FROM ".$data['tables']['credenzialiutente']['name']
                .", ".$data['tables']['anagrafica']['name']
                .", ".$data['tables']['conto']['name']
                ." WHERE "
                .$data['tables']['credenzialiutente']['pk']."='".$dest."'"
                ." AND ".$data['tables']['credenzialiutente']['pk1']."=".$data['tables']['anagrafica']['pk']
                ." AND ".$data['tables']['anagrafica']['cols'][2]."=".$data['tables']['conto']['pk']
                )[$data['tables']['conto']['pk']][0];

            query("INSERT INTO ".$data['tables']['movimento']['name']
                ." (".$data['tables']['movimento']['cols'][1]
                .", ".$data['tables']['movimento']['cols'][2]
                .", ".$data['tables']['movimento']['cols'][3]
                .", ".$data['tables']['movimento']['cols'][4].") "
                ." VALUES ('".$idda."'"
                .", '".$iddest."'"
                .", '".$importo."'"
                .", '".date("Y-m-d")."')"
            );
            unset($_POST['bonifico']);
            header("Location:/client_mypage.php");
        } else if (isset($_POST['delete'])) {
            logC("delete");

            //query();

            unset($_POST['delete']);
            header("Location:/");
        } else if (isset($_POST['estratto'])) {
            logC("estratto");
            $usr = $_COOKIE['username']; // Gets username from form

            $id = query("SELECT ".$data['tables']['conto']['name'].".".$data['tables']['conto']['pk']
                ." FROM ".$data['tables']['credenzialiutente']['name']
                .", ".$data['tables']['anagrafica']['name']
                .", ".$data['tables']['conto']['name']
                ." WHERE "
                .$data['tables']['credenzialiutente']['pk']."='".$usr."'"
                ." AND ".$data['tables']['credenzialiutente']['pk1']."=".$data['tables']['anagrafica']['pk']
                ." AND ".$data['tables']['anagrafica']['cols'][2]."=".$data['tables']['conto']['pk']
                )[$data['tables']['conto']['pk']][0];

            $res = query("SELECT DISTINCT ".$data['tables']['fileEstrattoConto']['name'].".".$data['tables']['fileEstrattoConto']['cols'][0]
                .", ".$data['tables']['fileEstrattoConto']['name'].".".$data['tables']['fileEstrattoConto']['cols'][1]
                .", ".$data['tables']['fileEstrattoConto']['name'].".".$data['tables']['fileEstrattoConto']['cols'][2]
                .", ".$data['tables']['fileEstrattoConto']['name'].".".$data['tables']['fileEstrattoConto']['cols'][3]
                ." FROM ".$data['tables']['credenzialiutente']['name']
                .", ".$data['tables']['anagrafica']['name']
                .", ".$data['tables']['conto']['name']
                .", ".$data['tables']['fileEstrattoConto']['name']
                ." WHERE ".$data['tables']['fileEstrattoConto']['name']."."
                .$data['tables']['fileEstrattoConto']['cols'][0]."='".$id."'"
                );

            $blob = $res[$data['tables']['fileEstrattoConto']['cols'][1]][0];
            $date = $res[$data['tables']['fileEstrattoConto']['cols'][2]][0];
            $title = $res[$data['tables']['fileEstrattoConto']['cols'][3]][0];

            file_put_contents($title.".txt", $blob);

            unset($_POST['estratto']);
            //header("Location:/client_mypage.php");
        }
    }
}

function getDouble ($number) {
    return number_format((float)$number, 2, '.', '');
}

function register () {
    /*
        Function: Login function, checks if username and password are correct in the DB and remembers if cheked the loggin.
    */
    global $data;
    $a = trim($_POST['email']); // Gets username from form
    $b = trim($_POST['cell']); // Gets password from form
    $c = trim($_POST['via']); // Gets username from form
    $d = trim($_POST['cap']); // Gets password from form
    $e = trim($_POST['pass']); // Gets username from form
    $f = trim($_POST['passcheck']); // Gets password from form
 
    if (isset($a) && isset($b) && isset($c) && isset($d) && isset($e) && isset($f) && $e != $f){
        query("INSERT INTO ".$data['tables']['anagrafica']['name']
            ." (".$data['tables']['anagrafica']['cols'][1]
            .", ".$data['tables']['anagrafica']['cols'][2]
            .", ".$data['tables']['anagrafica']['cols'][3]
            .", ".$data['tables']['anagrafica']['cols'][4].") "
            ." VALUES ('".$a."'"
            .", '".$b."'"
            .", '".$c."'"
            .", '".$c."')"
        );
        // DA FINEREEEEEEEEEEEEEE
        LogC("Loggato");
        $_SESSION["loggedin"] = true;
        $_SESSION["username"] = $username;
        $_POST['login'] = null;
        setcookie("loggedin",true,time()+ (10 * 365 * 24 * 60 * 60));  
        setcookie("username",$username,time()+ (10 * 365 * 24 * 60 * 60));
        header("Location:/client_mypage.php");
    }
}

function login () {
    /*
        Function: Login function, checks if username and password are correct in the DB and remembers if cheked the loggin.
    */
    global $data;
    $username = trim($_POST['codcliente']); // Gets username from form
    $password = trim($_POST['password']); // Gets password from form
 
    if (isset($username) && isset($password)){
        $search = query("SELECT ".$data['tables']['credenzialiutente']['pk']
            ." FROM ".$data['tables']['credenzialiutente']['name']." WHERE "
            .$data['tables']['credenzialiutente']['pk']."='".$username."' "
            ."AND ".$data['tables']['credenzialiutente']['cols'][0]."='".$password."'"
        );
        if ($search) { // Search in the DB the username if exist
            LogC("Loggato");
            $_SESSION["loggedin"] = true;
            $_SESSION["username"] = $username;
            $_POST['login'] = null;
            setcookie("loggedin",true,time()+ (10 * 365 * 24 * 60 * 60));  
            setcookie("username",$username,time()+ (10 * 365 * 24 * 60 * 60));
            header("Location:/client_mypage.php");
        }
    }
}

function logout () {
    /*
        Function: Logout function.
    */
    $_SESSION["loggedin"] = false;
    $_SESSION["username"] = 'Anonymous'; 

    unset($_COOKIE["loggedin"]); // Unsets Remember
    unset($_COOKIE["username"]);
    setcookie("loggedin", null, -1, '/');
    setcookie("username", null, -1, '/');
    header("Location:/");
}

function logC_DB($text) {
    /*
        Function: Logs onto browser console
    */
    $log = date('[H:i:s m-d-Y]', time());
    echo '<script>console.log("[DATABASE] '.$log.$text.'");</script>';
}

function logC($text) {
    /*
        Function: Logs onto browser console
    */
    $log = date('[H:i:s m-d-Y]', time());
    echo '<script>console.log("[SERVER] '.$log.$text.'");</script>';
}

//error_reporting(0); // DOESNT DISPLAY PHP ERRORS
define('DB_SERVER', $data['database'][$which_db]['url']);
define('DB_USERNAME', $data['database'][$which_db]['username']);
define('DB_PASSWORD', $data['database'][$which_db]['password']);
define('DB_NAME', $data['database'][$which_db]['db_name']);

function db_connect() {
    /*
        Function: Connects to the DB TABLE
    */
    $cred = array( // Have to move credentials in a secret place
        'host' => DB_SERVER,
        'user' => DB_USERNAME,
        'pass' => DB_PASSWORD, 
        'db' => DB_NAME
    );
    if (class_exists ('mysqli')) { // Check if mysql extension/class exist (PHP5+ required)
        $sql = new mysqli($cred['host'], $cred['user'], $cred['pass'], $cred['db']); // Creats the DB connection
        if ($sql->connect_error) { // Check if connection is sucsesful 
            //logC_DB("not connected ".$sql->connect_error);
        } else {
            //logC_DB("connected to DB");
        }
    } else {
        $sql = null;
    }
    return $sql; // Returns the DB connection
}

function query($src) {
    /*
        Function: Views a ROW given a VALUE
    */
    $DB = db_connect(); // Opens DB connection
    $cols = []; $new = [];
    if (strpos($src, "SELECT") !== false || strpos($src, "select") !== false) {
        // nothing
    } else if (strpos($src, "INSERT") !== false || strpos($src, "insert") !== false) {
        $result = $DB->query($src); // Send query
        mysqli_close($DB);
        return null;
    } 

    if (strpos($src, "distinct") !== false) {
        $new = explode("distinct ", $src)[1];
    } else if (strpos($src, "DISTINCT") !== false) {
        $new = explode("DISTINCT ", $src)[1];
    } else if (strpos($src, "select") !== false) {
        $new = explode("select ", $src)[1];
    } else if (strpos($src, "SELECT") !== false) {
        $new = explode("SELECT ", $src)[1];
    }

    if (strpos($src, "from") !== false) {
        $new = explode(" from", $new)[0];
    } else if (strpos($src, "FROM") !== false) {
        $new = explode(" FROM", $new)[0];
    }
    
    $cols = explode(", ", $new);
    $res = array();
    foreach ($cols as $c) {
        if (strpos($src, ".") !== false) {
            $res += array(explode(".", $c)[1] => []);
        } else {
            $res += array($c => []);
        }
    }

    $result = $DB->query($src); // Send query
    if ($result->num_rows > 0) { // Views ALL ROWS with the VALUE
        while($row = $result->fetch_assoc()) {
            $count = 0; $len = count($row); $txt = 'DB : ';
            foreach ($row as $key=>$value) {
                if ($count == $len - 1) {
                    $txt .= $key . " => " . $value;
                    array_push($res[$key], $value);
                } else {
                    $txt .= $key . " => " . $value . ", ";
                    array_push($res[$key], $value);
                } $count += 1;
            }
            //logC_DB($txt);
        }
        mysqli_close($DB);
        return $res; // Returs all the result of the query
    } else {
        //logC_DB("0 results");
        mysqli_close($DB);
        return null;
    }
}
?>