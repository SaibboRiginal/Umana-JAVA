const test = () => {
    var values = [];
    var elements = document.getElementsByTagName("input");
    for (i = 0; i < elements.length; i++) {
        values.push({
            key: elements[i].name,
            value: elements[i].value
        });
    }
    for (i = 0; i < values.length; i++) {
        console.log("Key : " + values[i].key + " Values : " + values[i].value);
    }
}

const showEntrance = (bool) => {
    var className = "showEntrance"
    var login = document.getElementById("logon");
    var register = document.getElementById("register");
    if (bool) {
        if (register.classList.contains(className))
            register.classList.remove(className);
        login.classList.toggle(className);
    } else {
        if (login.classList.contains(className))
            login.classList.remove(className);
        register.classList.toggle(className);
        window.scrollTo(0, window.innerHeight);
    }
}

const getCookie = (name) => {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}

