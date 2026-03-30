<head>
    <!-- Title of page -->
    <?php echo "<title>".basename(__FILE__, '.php')."</title>";?> 
    <!-- Essential imports -->
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="static/script.js"></script>
    <script type="text/javascript" src="static/particles.js"></script>
    <link rel="stylesheet" type="text/css" href="static/style.css">
      
    <!-- Importing custom CSS -->
    <?php    
        error_reporting(0); // DOESNT DISPLAY PHP ERRORS    
    ?>
</head>
<body>
    <center>
        <h1 id="denied">ACCESS DENIED</h1>
        <hr id="linedenied">
        <h5 id="subdenied">You're not logged in</h5>
    </center>
</body>