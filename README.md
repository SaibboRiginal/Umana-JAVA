# Umana JAVA — Progetto di corso (Tesi) — Showcase storico

Questo repository raccoglie il progetto finale (tesi) che ho realizzato durante il corso. È una versione storica del lavoro: la pubblico soprattutto come esempio da inserire nel mio CV. Il codice non è più mantenuto.

## Descrizione
- Scopo: dimostrare un semplice sistema ATM e un modulo di monitoraggio.
- Linguaggi: Java (core), PHP (frontend), HTML/CSS/JS (asset statici).
- Stato: progetto d'archivio, non mantenuto attivamente.

## Struttura del repository
- `media/` — immagini e file di configurazione JSON (es. `media/json/setting.json`)
- `source html - frontend/` — frontend PHP e risorse statiche (es. `index.php`, `client_mypage.php`, `static/`)
- `source java - fontend/` — codice Java e binari (classi principali in `src/`, per esempio `src/sai/banking/LaunchATM.java` e `src/sai/monitor/LaunchMonitor.java`)

## Avvio rapido

### Frontend PHP (serve PHP)
Apri un terminale nella cartella del repository e avvia il server integrato:

```bash
cd "source html - frontend"
php -S localhost:8000
```

Poi apri http://localhost:8000 nel browser.

### Java (consigliato: IDE come IntelliJ o Eclipse)
Importa `source java - fontend/src` come progetto Java ed esegui le classi principali:
- `sai.banking.LaunchATM`
- `sai.monitor.LaunchMonitor`

Da riga di comando (adatta i percorsi per Windows):

```bash
cd "source java - fontend"
javac -d bin src/sai/banking/*.java src/sai/monitor/*.java
java -cp bin sai.banking.LaunchATM
```