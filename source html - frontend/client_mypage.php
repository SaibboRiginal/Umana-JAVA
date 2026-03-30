<?php 
    include("shell.php"); // Importing base functions
    session_start(); // something

    check_login(); // Checks login form
    if (check_auth()):
        check_AJAX();
?>
<html>
    <head>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script type="text/javascript" src="static/script.js"></script>
        <script type="text/javascript" src="static/ajax.js"></script>
        <link rel="stylesheet" type="text/css" href="static/style.css">
        <title>SaiCredit</title>
    </head>

    <body id="mypage">
        <section class="header">
            <div class="menu">
                <div class="logo">
                    <a href="/">
                        <img alt="logo" src="media/logo.png">
                    </a>
                </div>
                <form method="post">
                    <input type="submit" value="logout" name="logout" class="logout"></input>
                </form>
            </div>
        </section>
        <section class="main">
            <div class="mypage">
                <div class="info">
                    <div class="info-content">
                        <h1 class="username">
                            Username
                        </h1>
                        <h3 class="userdata">
                            Saldo Attuale :
                        </h3>
                        <h3 id="saldo_attuale" class="uservalue">
                            0.00
                        </h3>
                        <h3 class="money">
                            €
                        </h3>
                        <h3 class="userdata">
                            Saldo Contabile :
                        </h3>
                        <h3 id="saldo_cotabile" class="uservalue">
                            0.00
                        </h3>
                        <h3 class="money">
                            €
                        </h3>
                    </div>
                </div>
                <div class="panel-wrapper">
                    <div class="panel">
                        <h2 class="title">
                            Bonifico
                        </h2> 
                        <div class="form">
                            <form id="bonifico" method="post">
                                <label class="text" for="dest">Destinatario</label>
                                <input type="text" name="dest">
                                
                                <label class="text" for="importo">Importo</label>
                                <input type="text" name="importo">

                                <input id="bonificoID" type="text" value="" name="da" style="display:none;">
                                
                                <input type="submit" value="Spedisci" name="bonifico">
                            </form>
                        </div>
                    </div>
                    <div class="panel">
                        <h2 class="title">
                            Opzioni
                        </h2> 
                        <div class="list">
                            <form id="estratto" method="post">
                                <input type="submit" value="Download estratto conto" name="estratto">
                            </form>
                            <form id="delete" method="post">
                                <input type="submit" value="Cancella account" name="delete">
                            </form>
                        </div>
                    </div>
                    <div class="panel">
                        <h2 class="title">
                            Movimenti
                        </h2> 
                        <div class="dateselect">
                            <div class="dateda">
                                Da:
                                <input type="date" name="inzio_date">
                            </div>
                            <div class="datea">
                                A:
                                <input type="date" name="fine_date">
                            </div>
                        </div>
                        <table class="infos">
                            <tr>
                                <th>Data</th>
                                <th>Accredito</th>
                                <th>Addebito</th>
                            </tr>
                        </table>
                        <div class="table">

                        </div>
                    </div>
                    <div class="panel">
                        <h2 class="title">
                            Lista Boninfico
                        </h2> 
                        <table class="infos">
                            <tr>
                                <th>Convalidazione</th>
                                <th>Accredito</th>
                                <th>Addebito</th>
                            </tr>
                        </table>
                        <div class="table">

                        </div>
                    </div>
                </div>
            </div>
        </section>
        <div class="alert">
            <span class="closebtn">&times;</span>  
            <strong>Danger!</strong> Codice IBAN non corretto
        </div>
        <div class="alert success">
            <span class="closebtn">&times;</span>  
            <strong>Success!</strong> Indicates a successful or positive action.
        </div>
        <div class="alert info">
            <span class="closebtn">&times;</span>  
            <strong>Info!</strong> Indicates a neutral informative change or action.
        </div>
        <div class="alert warning">
            <span class="closebtn">&times;</span>  
            <strong>Warning!</strong> Indicates a warning that might need attention.
        </div>
    </body>
</html>
<?php 
    else: // If not logged in display this
        include("denied.php");
    endif;
?>