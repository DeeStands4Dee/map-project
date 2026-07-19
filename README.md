\# MAP - Regression Tree Project



\## Descrizione del Progetto



Il progetto MAP implementa un sistema client-server per la scoperta di alberi di regressione da dati memorizzati in un database MySQL. Il sistema è composto da due progetti:



\- \*\*map\*\*: progetto base con client testuale

\- \*\*map-ext\*\*: progetto con estensione GUI JavaFX



\## Differenze rispetto al progetto originale



\- I dati vengono caricati da un database MySQL tramite JDBC invece che da file .dat

\- Architettura client-server tramite socket

\- \*\*map-ext\*\*: aggiunta interfaccia grafica JavaFX con visualizzazione delle regole dell'albero



\---



\## Requisiti



\- JDK 11 o superiore

\- MySQL Server 8.0

\- JavaFX SDK 21 (solo per map-ext)

\- MySQL Connector/J 8.0.17



\---



\## Configurazione Database



Eseguire in MySQL Workbench:



CREATE DATABASE MapDB;

CREATE USER 'MapUser'@'localhost' IDENTIFIED BY 'map';

GRANT ALL PRIVILEGES ON MapDB.\* TO 'MapUser'@'localhost';

FLUSH PRIVILEGES;



CREATE TABLE MapDB.provaC(

&#x20;   X varchar(10),

&#x20;   Y float(5,2),

&#x20;   C float(5,2)

);



\---



\## Avvio del Sistema (map - client testuale)



\### Avvio Server

cd tree/jar

java -jar server.jar



\### Avvio Client

cd tree/jar

java -jar client.jar localhost 8080



\---



\## Avvio del Sistema (map-ext - GUI JavaFX)



\### Avvio Server

cd tree-ext/jar

java -jar server.jar



\### Avvio Client GUI

cd tree-ext/jar

java --module-path "<percorso-javafx>/lib" --add-modules javafx.controls,javafx.fxml -jar client.jar



\---



\## Guida Utente - Client Testuale (map)



1\. Avviare il server

2\. Avviare il client

3\. Scegliere 1 per apprendere un nuovo albero o 2 per caricarne uno esistente

4\. Inserire il nome della tabella (es. provaC)

5\. Navigare l'albero inserendo il numero della scelta

6\. Il sistema mostra la classe predetta



\---



\## Guida Utente - Client GUI (map-ext)



1\. Avviare il server

2\. Avviare il client GUI

3\. Inserire indirizzo e porta del server (default: localhost:8080)

4\. Cliccare Connetti

5\. Inserire il nome della tabella (es. provaC)

6\. Cliccare Apprendi albero o Carica albero

7\. Le regole dell'albero appaiono nel pannello destro

8\. Cliccare Avvia Predizione e seguire le istruzioni

9\. Selezionare la scelta dalla ComboBox e cliccare Scegli

10\. Il risultato appare in basso

11\. Cliccare Reset per una nuova predizione

12\. Cliccare Disconnetti per chiudere la connessione



\---



\## Casi di Test



\### Test 1 - Connessione al Server

\- Input: indirizzo localhost, porta 8080

\- Atteso: Connesso a localhost:8080



\### Test 2 - Apprendimento albero (provaC)

\- Input: tabella provaC, scelta 1

\- Atteso: Albero appreso con successo! e regole visibili



\### Test 3 - Predizione X=A, Y<=2.0

\- Input: scelta 0 (X=A), scelta 0 (Y<=2.0)

\- Atteso: Classe predetta: 1.0



\### Test 4 - Predizione X=A, Y>2.0

\- Input: scelta 0 (X=A), scelta 1 (Y>2.0)

\- Atteso: Classe predetta: 1.5



\### Test 5 - Predizione X=B

\- Input: scelta 1 (X=B)

\- Atteso: Classe predetta: 10.0



\### Test 6 - Caricamento albero da file

\- Input: tabella provaC, scelta 2

\- Atteso: Albero caricato con successo!



\### Test 7 - Apprendimento albero (servo)

\- Input: tabella servo, scelta 1

\- Atteso: Albero appreso con successo! con regole più complesse



\### Test 8 - Ridimensionamento GUI

\- Input: ridimensiona la finestra

\- Atteso: tutti gli elementi si adattano correttamente



\---



\## Struttura del Progetto



map/

├── tree/          # Progetto map (client testuale)

│   ├── src/       # Sorgenti

│   ├── bin/       # Classi compilate

│   ├── lib/       # Librerie (MySQL connector)

│   ├── jar/       # JAR eseguibili e script

│   ├── doc/       # Javadoc

│   └── uml/       # Diagramma UML

└── tree-ext/      # Progetto map-ext (GUI JavaFX)

&#x20;   ├── src/       # Sorgenti

&#x20;   ├── bin/       # Classi compilate

&#x20;   ├── lib/       # Librerie

&#x20;   ├── jar/       # JAR eseguibili e script

&#x20;   ├── doc/       # Javadoc

&#x20;   └── uml/       # Diagramma UML

