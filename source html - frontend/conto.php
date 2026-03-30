<?php
include("shell.php"); // Importing base functions
session_start(); // something

$cod = $_GET['cod'];
$cod1 = $_GET['cod1'];

$return = (object) [];

$query = query("SELECT ".$data['tables']['conto']['name'].".".$data['tables']['conto']['cols'][1]
            ." FROM ".$data['tables']['credenzialiutente']['name']
            .", ".$data['tables']['anagrafica']['name']
            .", ".$data['tables']['conto']['name']
            ." WHERE "
            .$data['tables']['credenzialiutente']['pk']."='".$cod."'"
            ." AND ".$data['tables']['credenzialiutente']['pk1']."=".$data['tables']['anagrafica']['pk']
            ." AND ".$data['tables']['anagrafica']['cols'][2]."=".$data['tables']['conto']['pk']
            )[$data['tables']['conto']['cols'][1]][0];

$query1 = query("SELECT ".$data['tables']['conto']['name'].".".$data['tables']['conto']['cols'][2]
            ." FROM ".$data['tables']['credenzialiutente']['name']
            .", ".$data['tables']['anagrafica']['name']
            .", ".$data['tables']['conto']['name']
            ." WHERE "
            .$data['tables']['credenzialiutente']['pk']."='".$cod1."'"
            ." AND ".$data['tables']['credenzialiutente']['pk1']."=".$data['tables']['anagrafica']['pk']
            ." AND ".$data['tables']['anagrafica']['cols'][2]."=".$data['tables']['conto']['pk']
            )[$data['tables']['conto']['cols'][2]][0];

$return->uno = getDouble(floatval($query));
$return->due = getDouble(floatval($query1));

// https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
// at this point you got all the data into an array
// so you can return this to the client (in ajax request)
header('Content-type: application/json');
echo json_encode($return);

?>