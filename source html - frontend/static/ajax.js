window.onload = () => {
    var myCookie = getCookie("username");
    var username = document.getElementsByClassName("username")[0];
    if (myCookie == null) {
        username.innerHTML = "Anonymous";
    }
    else {
        username.innerHTML = myCookie;
        document.getElementById("bonificoID").value = myCookie;
    }
    setTimeout(() => {
        $.ajax({
            url: 'conto.php',
            data: {"cod": myCookie, "cod1":myCookie},
            method: 'GET',
            dataType: 'text',
            success: function(data) {
                var saldo = JSON.parse(data);
                console.log(saldo);
                document.getElementById("saldo_attuale").innerHTML = saldo.uno;
                document.getElementById("saldo_cotabile").innerHTML = saldo.due;
            }
        });

        xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementsByClassName("table")[0].innerHTML = this.responseText;
            }
        };
        xmlhttp.open("GET","table.php?q="+myCookie,true);
        xmlhttp.send();

        xmlhttp2 = new XMLHttpRequest();
        xmlhttp2.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementsByClassName("table")[1].innerHTML = this.responseText;
            }
        };
        xmlhttp2.open("GET","table2.php?q="+myCookie,true);
        xmlhttp2.send();
    }, 500);

    var dateda = document.getElementsByClassName("dateda")[0].children[0];
    var datea = document.getElementsByClassName("datea")[0].children[0];
    var ibancheck = document.getElementById('bonifico').children[1]; // INPUT DEL CODICE UTENTE

    dateda.addEventListener('change', () => {
        xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementsByClassName("table")[0].innerHTML = this.responseText;
            }
        };
        
        xmlhttp.open("GET","table.php?q="+myCookie+"&dataDA="+dateda.value+(datea.value ? "&dataA="+datea.value : ""),true);
        xmlhttp.send();
    });
    datea.addEventListener('change', () => {
        xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementsByClassName("table")[0].innerHTML = this.responseText;
            }
        };
        xmlhttp.open("GET","table.php?q="+myCookie+"&dataA="+datea.value+(dateda.value ? "&dataDA="+dateda.value : ""),true);
        xmlhttp.send();
    });
    ibancheck.addEventListener('change', () => {
        $.ajax({
            url: 'iban.php',
            data: {"cod": ibancheck.value},
            method: 'GET',
            dataType: 'text',
            success: function(data) {
                var check = JSON.parse(data);
                console.log(check);
                if(!check.uno) {
                    document.getElementsByClassName("alert")[0].style.bottom = "15px";
                    setTimeout(() => {
                        document.getElementsByClassName("alert")[0].style.bottom = "-100px";
                    }, 3000);
                }
            }
        });
    });
    var close = document.getElementsByClassName("closebtn");
    var i;

    for (i = 0; i < close.length; i++) {
        close[i].onclick = function(){
            var div = this.parentElement;
            div.style.bottom = "-100px";
        }
    }
}
