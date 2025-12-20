## Plan: Note “solo append” per interventi (Tecnico/Admin), InMemory + DB

Obiettivo: aggiungere un sistema di note collegate a una `Segnalazione` (intervento). Le note sono **append-only** (niente edit/cancel), **scrivibili dal Tecnico assegnato** e **visibili a Tecnico + Admin (SYSADMIN)**. Deve funzionare sia con persistenza **in-memory** che **DB** tramite il pattern DAO già presente nel progetto.

### Mini-contratto
- Input: `idSegnalazione`, `testoNota`
- Output:
  - `aggiungiNota(...)` salva una nuova nota (uuid) e ritorna la nota o `void`
  - `listaNote(idSegnalazione)` ritorna note ordinate per data
- Errori:
  - testo vuoto/solo spazi → rifiutato
  - user non autorizzato → rifiutato
  - segnalazione inesistente → rifiutato

### Steps
1. **Modello dati: entità append-only**
   - Aggiornare `model.NotaSegnalazione` in `src/main/java/org/ing/ispw/unifix/model/NotaSegnalazione.java`.
   - Campi consigliati:
     - `String id` (uuid)
     - `String idSegnalazione`
     - `String autoreEmail`
     - `String testo`
     - `java.time.Instant dataCreazione` (o `LocalDateTime`, ma `Instant` è più neutro)
   - Niente reference diretta a `Segnalazione` (semplifica DB).

2. **DAO: contratto dedicato append-only**
   - Creare `dao.NotaSegnalazioneDao` con metodi minimi:
     - `void store(NotaSegnalazione nota)` (append)
     - `List<NotaSegnalazione> loadAllBySegnalazione(String idSegnalazione)`
     - (opzionale) `List<NotaSegnalazione> loadAll()` per vista Admin globale
   - Evitare `update/delete`: se l’interfaccia madre li impone, implementare e lanciare `UnsupportedOperationException`.

3. **InMemory implementation**
   - Aggiungere `dao.memory.InMemoryNotaSegnalazioneDao`.
   - Salvare note per chiave `id` (uuid stringa) e fornire `loadAllBySegnalazione(...)` filtrando e **ordinando per `dataCreazione` ASC**.

4. **DB implementation (JDBC)**
   - Aggiungere `dao.db.NotaSegnalazioneDbDao` (o naming coerente col repo).
   - Tabella proposta: `nota_segnalazione`
     - `id VARCHAR(36) PRIMARY KEY`
     - `id_segnalazione VARCHAR(?) NOT NULL`
     - `autore_email VARCHAR(255) NOT NULL`
     - `testo TEXT NOT NULL`
     - `data_creazione TIMESTAMP NOT NULL`
   - Query:
     - `INSERT INTO nota_segnalazione (...) VALUES (...)`
     - `SELECT ... FROM nota_segnalazione WHERE id_segnalazione=? ORDER BY data_creazione ASC`

5. **Wiring in DaoFactory**
   - Estendere `DaoFactory` con `getNotaSegnalazioneDao()`.
   - Implementare in `InMemoryDaoFactory` e nella factory DB (se presente; altrimenti crearla) così lo switch InMemory/DB rimane trasparente.

6. **Controller applicativo: validazione + autorizzazione**
   - Creare `controllerapplicativo.GestioneNoteController` con:
     - `aggiungiNota(String idSegnalazione, String testo)`:
       - `testo = testo.trim();` se vuoto → errore
       - carica segnalazione; se null → errore
       - autorizza: current user è `TECNICO` e `segnalazione.getTecnico().getEmail().equals(currentUser.getEmail())` **oppure** `SYSADMIN`
       - crea `NotaSegnalazione` con `uuid.randomUUID().toString()` e `Instant.now()` e fa `store`
     - `visualizzaNote(String idSegnalazione)` con stessa policy di lettura.

7. **UI: integrazione minima**
   - Tecnico (`ControllerGraficoHomeTecnico`): nel popup dettaglio segnalazione aggiungere bottone “Note” → mostra elenco note + campo testo per aggiungerne.
   - Admin (`ControllerGraficoDashboardSegnalazioniAdmin`): nel dettaglio segnalazione mostra anche le note associate.

### Chiarimento su “le segnalazioni hanno sempre un tecnico assegnato”
Nel **codice attuale** non è garantito al 100%:
- In `model.Segnalazione` il campo `private Tecnico tecnico;` può essere `null`.
- In `VisualizzaSegnalazioniTecnicoController` c’è `segnalazione.getTecnico().getEmail()` senza check: se esiste anche una sola segnalazione senza tecnico assegnato, qui può esplodere con `NullPointerException`.
- Anche `ControllerGraficoDashboardSegnalazioniAdmin` in label fa `segnalazione.getTecnico().getNome()` senza check.

Quindi:
- **Se il sistema a livello di business assicura che ogni segnalazione viene sempre assegnata a un tecnico prima di essere visibile**, allora ok, ma oggi il codice non lo “enforce-a” nel model/DAO.
- Se non è una garanzia assoluta, conviene aggiungere guard-rail (check null + gestione “Nessuno”) oppure imporre l’assegnazione in fase di creazione/accettazione.

### Decisioni richieste (già definite)
- ID nota: **uuid**
- Note vuote: **vietate** (trim + length check)

