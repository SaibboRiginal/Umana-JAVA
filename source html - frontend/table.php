<?php 
include("shell.php"); // Importing base functions
session_start(); // something

$q = intval($_GET['q']);
$a = ""; $b = "";

$q = "SELECT ".$data['tables']['movimento']['name'].".".$data['tables']['movimento']['cols'][1]
        .", ".$data['tables']['movimento']['name'].".".$data['tables']['movimento']['cols'][2]
        .", ".$data['tables']['movimento']['name'].".".$data['tables']['movimento']['cols'][3]
        .", ".$data['tables']['movimento']['name'].".".$data['tables']['movimento']['cols'][4]
        ." FROM ".$data['tables']['credenzialiutente']['name']
        .", ".$data['tables']['anagrafica']['name']
        .", ".$data['tables']['conto']['name']
        .", ".$data['tables']['movimento']['name']
        ." WHERE "
        .$data['tables']['credenzialiutente']['pk']."='".$q."'"
        ." AND ".$data['tables']['credenzialiutente']['pk1']."=".$data['tables']['anagrafica']['pk']
        ." AND ".$data['tables']['anagrafica']['cols'][2]."=".$data['tables']['conto']['pk']
        ." AND (".$data['tables']['movimento']['cols'][1]."=".$data['tables']['anagrafica']['pk']
        ." OR ".$data['tables']['movimento']['cols'][2]."=".$data['tables']['anagrafica']['pk'].")";

if(isset($_GET['dataDA'])) {
    $q .= " AND ".$data['tables']['movimento']['cols'][4].">='".$_GET['dataDA']."'";
}
if(isset($_GET['dataA'])) {
    $q .= " AND ".$data['tables']['movimento']['cols'][4]."<='".$_GET['dataA']."'";
}        

$table = query($q);
?>
<table id="table">                   
    <?php for($i = 0; $i < sizeof($table[$data['tables']['movimento']['cols'][1]]); $i++): ?> 
    <tr>
        <td><?php echo $table[$data['tables']['movimento']['cols'][4]][$i]; ?></td>

        <?php if(!isset($table[$data['tables']['movimento']['cols'][1]][$i]) || trim($table[$data['tables']['movimento']['cols'][1]][$i]) === '0'): ?>
            <td><?php echo getDouble(floatval($table[$data['tables']['movimento']['cols'][3]][$i])) . " €"; ?></td> 
            <td><?php echo "0.00 €"; ?></td> 
        <?php else: ?> 
            <td><?php echo "0.00 €"; ?></td>
            <td><?php echo getDouble(floatval($table[$data['tables']['movimento']['cols'][3]][$i])) . " €"; ?></td> 
        <?php endif; ?> 
    </tr>       
    <?php endfor; ?> 
</table>