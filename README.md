# Documentazione Progetto UniFix

## Panoramica generale del progetto

**UniFix** è un'applicazione software sviluppata come progetto per il corso di ISPW (Ingegneria del Software e Progettazione Web). Il progetto è stato realizzato da **David Julian Belfiori**.

Lo scopo principale di UniFix è fornire un sistema centralizzato per la **gestione e la segnalazione di guasti** all'interno delle aule universitarie o scolastiche. L'applicazione è progettata per essere utilizzata da tre diversi tipi di utenti: **Docenti**, **Tecnici** e **Amministratori di Sistema (SysAdmin)**, ognuno con funzionalità e permessi specifici.

L'applicazione è un software desktop monolitico costruito in Java, che offre due interfacce utente: una **GUI (Graphical User Interface)** basata su JavaFX e una **CLI (Command-Line Interface)** testuale.

---

## Descrizione del problema

UniFix affronta l'inefficienza nella gestione dei problemi tecnici nelle aule. Prima di questo sistema, la segnalazione di un proiettore rotto, un problema audio o un cavo HDMI difettoso poteva essere un processo lento e non tracciato.

Il progetto risolve questo problema fornendo una piattaforma unica dove:

* **I Docenti** possono facilmente effettuare il login, selezionare l'aula e il tipo di problema (eventualmente aggiungendo una descrizione), per assicurare che il guasto sia preso in carico rapidamente.
* **I Tecnici** possono visualizzare un elenco chiaro degli interventi loro assegnati, aggiornare lo stato di una segnalazione (es. "in lavorazione", "chiusa") e gestire le priorità in modo efficiente.
* **L'Ufficio Tecnico (SysAdmin)** può monitorare lo stato di tutte le segnalazioni, assegnare interventi ai tecnici disponibili e gestire l'anagrafica delle aule (ad esempio importandole da file CSV).

---

## Architettura generale

L'applicazione segue una chiara architettura multi-strato, che implementa diversi pattern noti, tra cui **Model-View-Controller (MVC)** e **Data Access Object (DAO)**.

1.  **View (Livello di Presentazione)**
    L'applicazione supporta due tipi di viste, che condividono la stessa logica di business sottostante:
    * **GUI (JavaFX)**: Implementata tramite file `.fxml` (in `src/main/resources/org/ing/ispw/unifix/`) che definiscono la struttura, e classi `ControllerGrafico` (nel pacchetto `...controllergrafico`) che gestiscono l'interazione dell'utente.
    * **CLI (Console)**: Implementata nel pacchetto `...cli` (es. `StartHomeViewCLI`, `LoginCli`), offre un'interfaccia testuale per le funzionalità di base.

2.  **Controller (Livello di Logica Applicativa)**
    Questo livello è il cuore dell'applicazione ed è separato dalla vista. È contenuto nel pacchetto `...controllerapplicativo`:
    * Gestisce la logica di business (es. `LoginController`, `InviaSegnalazioneController`).
    * Non ha dipendenze dirette da JavaFX o dalla console, garantendo la separazione dei compiti.
    * Utilizza i **Bean** (pacchetto `...bean`) come DTO (Data Transfer Objects) per ricevere dati dalla View (es. `LoginBean`).

3.  **Model (Livello dei Dati)**
    Contiene le entità del dominio (pacchetto `...model`):
    * `User.java`: Classe base per tutti gli utenti.
    * `Docente.java`, `Tecnico.java`, `Sysadmin.java`: Classi specializzate che estendono `User`.
    * `Segnalazione.java`: L'entità centrale che rappresenta un guasto segnalato.
    * `Aula.java`: Rappresenta un'aula.

4.  **Data Access Layer (Livello di Accesso ai Dati)**
    L'accesso ai dati è astratto utilizzando il pattern **DAO** e una **Abstract Factory**.
    * `...dao.DaoFactory`: Una classe astratta (factory) che definisce i metodi per ottenere i vari DAO (es. `getUserDao()`, `getSegnalazioneDao()`).
    * `...dao.UserDao`, `...dao.SegnalazioneDao`: Interfacce che definiscono le operazioni CRUD.
    * `...dao.jdbc`: Implementazione concreta dei DAO che utilizza **JDBC** per connettersi a un database relazionale (MariaDB).
    * `...dao.memory`: Un'implementazione "in-memory" dei DAO, utile per testare l'applicazione senza un database reale.

L'applicazione seleziona quale factory (JDBC o In-Memory) utilizzare all'avvio, tramite una configurazione nel `Driver.java`.

---

## Tecnologie utilizzate

Come definito nel file `pom.xml`, le tecnologie principali includono:

* **Linguaggio**: Java 23
* **Build System**: Apache Maven
* **Interfaccia Grafica (GUI)**: OpenJFX (JavaFX)
* **Componenti UI Aggiuntivi**:
    * `org.controlsfx:controlsfx`
    * `com.dlsc.formsfx:formsfx-core`
    * `net.synedra:validatorfx`
    * `org.kordamp.ikonli:ikonli-javafx` (per le icone)
    * `org.kordamp.bootstrapfx:bootstrapfx-core` (per stili simili a Bootstrap)
* **Database**: MariaDB (tramite il driver `mariadb-java-client`)
* **Test**: JUnit 5

---

