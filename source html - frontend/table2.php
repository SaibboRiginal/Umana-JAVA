<?php 
include("shell.php"); // Importing base functions
session_start(); // something

$q = intval($_GET['q']);

// cambiare query
$m = "SELECT ".$data['tables']['convalidabonifici']['name'].".".$data['tables']['convalidabonifici']['cols'][1]
        .", ".$data['tables']['convalidabonifici']['name'].".".$data['tables']['convalidabonifici']['cols'][2]
        .", ".$data['tables']['convalidabonifici']['name'].".".$data['tables']['convalidabonifici']['cols'][3]
        .", ".$data['tables']['convalidabonifici']['name'].".".$data['tables']['convalidabonifici']['cols'][6]
        .", ".$data['tables']['conto']['name'].".".$data['tables']['conto']['pk']
        ." FROM ".$data['tables']['credenzialiutente']['name']
        .", ".$data['tables']['anagrafica']['name']
        .", ".$data['tables']['conto']['name']
        .", ".$data['tables']['convalidabonifici']['name']
        ." WHERE "
        .$data['tables']['credenzialiutente']['name'].".".$data['tables']['credenzialiutente']['pk']."='".$q."'"
        ." AND ".$data['tables']['credenzialiutente']['name'].".".$data['tables']['credenzialiutente']['pk1']."=".$data['tables']['anagrafica']['name'].".".$data['tables']['anagrafica']['pk']
        ." AND ".$data['tables']['anagrafica']['name'].".".$data['tables']['anagrafica']['cols'][2]."=".$data['tables']['conto']['name'].".".$data['tables']['conto']['pk']
        ." AND (".$data['tables']['convalidabonifici']['name'].".".$data['tables']['convalidabonifici']['cols'][1]."=".$data['tables']['conto']['name'].".".$data['tables']['conto']['pk']
        ." OR ".$data['tables']['convalidabonifici']['name'].".".$data['tables']['convalidabonifici']['cols'][2]."=".$data['tables']['conto']['name'].".".$data['tables']['conto']['pk'].")";

$table = query($m);

$id = $table[$data['tables']['conto']['pk']][4];
?>
<table id="table2">                   
    <?php for($i = 0; $i < sizeof($table[$data['tables']['convalidabonifici']['cols'][1]]); $i++): ?> 
    <tr>
        <?php if(!isset($table[$data['tables']['convalidabonifici']['cols'][6]][$i]) || trim($table[$data['tables']['convalidabonifici']['cols'][6]][$i]) === ''): ?>
            <td><?php echo "Da Convalidare"; ?></td>
        <?php else: ?> 
            <td><?php echo $table[$data['tables']['convalidabonifici']['cols'][6]][$i]; ?></td>
        <?php endif; ?> 

        <?php if(trim($table[$data['tables']['convalidabonifici']['cols'][1]][$i]) !== $id): ?>
            <td><?php echo getDouble(floatval($table[$data['tables']['convalidabonifici']['cols'][3]][$i])) . " €"; ?></td> 
            <td><?php echo "0.00 €"; ?></td> 
        <?php else: ?> 
            <td><?php echo "0.00 €"; ?></td>
            <td><?php echo getDouble(floatval($table[$data['tables']['convalidabonifici']['cols'][3]][$i])) . " €"; ?></td> 
        <?php endif; ?> 
    </tr>       
    <?php endfor; ?> 
</table>