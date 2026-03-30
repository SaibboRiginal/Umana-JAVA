<?php 
    include("shell.php"); // Importing base functions
    session_start(); // something

    check_login(); // Checks login form
?>
<html>
    <head>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script type="text/javascript" src="static/script.js"></script>
        <script type="text/javascript" src="static/particles.js"></script>
        <link rel="stylesheet" type="text/css" href="static/style.css">
        <title>SaiCredit</title>
    </head>

    <body id="index">
        <section class="main">
            <div class="start">
                <div class="effects">
                    <!-- PARTICLE EFFECTS -->
                    <div class="particles"></div>
                </div>
    
                <!-- BACKGROUND IMAGE .start -->
                <div class="welcome">
                    <p>
                        <span class="u">welcome</span><br>
                        <span>to</span><br>
                        <span class="important">SaiCredit</span>
                    </p>
                </div>
                <div class="buttons">
                    <input type="button" value="Accedi" onclick="showEntrance(true)">
                    <input type="submit" value="Registrati" onclick="showEntrance(false)">
                </div>

                <!-- SWTICH TRA LOGIN E CREA CONTO -->
                <div id="logon">
                    <form method="post">
                        <span class="exit" onclick="showEntrance(true)"></span>
                        <label class="text" for="codcliente">Codice Cliente</label>
                        <input type="text" name="codcliente">
                        
                        <label class="text" for="password">Password</label>
                        <input type="password" type="password" name="password">
                        
                        <input type="submit" value="login" name="login">
                    </form>
                </div>
                <div id="register">
                    <form method="post">
                        <!-- Codice cliente autogenerato??
                        <label class="text" for="codcliente">Codice Cliente</label>
                        <input type="text" name="codcliente">
                        -->
                        <span class="exit" onclick="showEntrance(false)"></span>

                        <label class="text" for="email">Email</label>
                        <input type="text" name="email">

                        <label class="text" for="cell">Numero di telefono</label>
                        <input type="text" name="cell">

                        <label class="text" for="via">Via</label>
                        <input type="text" name="via">

                        <label class="text" for="cap">Cap</label>
                        <input type="text" name="cap">
                        
                        <label class="text" for="password">Password</label>
                        <input type="password" name="password">

                        <label class="text" for="confermapassword">Conferma Password</label>
                        <input type="password" name="confermapassword">
                        
                        <input type="submit" value="register">
                    </form>
                </div>
            </div>
        </section>
    </body>
</html>