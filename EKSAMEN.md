# Eksamensoppgave for Avansert Java (PGR203)

* **Emnekode**: PGR203
* **Emnenavn**: Avansert Java
* **Vurderingskombinasjon**: Funksjonalitet og kodekvalitet i fungerende program
* **Utleveringsfrist**: 4. september
* **Innleveringsfrist**: 18. november
* **Filformat**: Github classroom-lenke og ZIP-fil fra Github med Java-kildekode og README.md-fil.

### Innlevering

* Oppgaven **skal** løses i Github vha Github Classroom med link fra Canvas. Repository på github **skal** være private
* Oppgaven **skal** leveres i *Wiseflow* som en ZIP-fil og link til Github Classroom
* Oppgaven **skal** løses parvis. Dere kan velge om dere vil beholde par fra siste innlevering eller finne nye partnere. Ønsker du å levere alene eller i gruppe på tre må dette avklares med foreleser innen siste forelesning
* Innleveringen skal deles med en annen gruppe for gjensidig tilbakemelding. Tilbakemelding skal gis i form av Github issues
* README.md på Github **skal** linke til Travis-CI som skal kjøre enhetstester uten feil. README-filen skal også inneholde link til gitt tilbakemelding til annet team, et UML-diagram samt beskrivelse av hva kandiditene ønskes skal vurderes i evalueringen av innleveringen
* Koden **skal** lese database settings fra en fil som heter `task-manager.properties` og ser ut som følger:

```properties
dataSource.url=...
dataSource.username=...
dataSource.password=...
```

### Oppgave

Mappeoppgaven for PGR203 er å lage en backend server for å håndtere prosjektstyring med tildeling og status på oppgaver. Serveren skal la bruker kunne opprette oppgaver, redigere oppgavene, tildele oppgaver til prosjektdeltagere og endre oppgavestatus.

Programmet skal utvikles på en måte som demonstrerer programmeringsferdigheter slik det vises i undervisningen. Spesielt skal all funksjonalitet ha automatiske tester og være fri for grunnleggende sikkerhetssvakheter. Programmet skal demonstre at kandiatene mestrer Sockets og JDBC bibliotekene i Java.

Programmet skal leveres i form av et maven prosjekt som kan bygge en `executable jar` og lagre data i en PostgreSQL-database. Oppgaven skal leveres i grupper på to.

Dere skal levere en webserver som skal kunne brukes i nettleseren for å administrer oppgaver i et prosjektstyringssystem.

Funksjonaliteten bør inkludere:
* Opprett ny prosjektoppgave
* Liste opp prosjektoppgaver, inkludert status og tildelte prosjektmedlemmer
* Tildel oppgave til prosjektmedlemmer
* Endre oppgavestatus
* Filtrere oppgaver på tilordnet en prosjektmedlem
* Legge til nye prosjektmedlemmer
* Legge til nye status-kategorier

Prosjektet må følge god programmeringsskikk. Dette er viktige krav og feil på et enkelt punkt kan gi en hel karakter i trekk.
* Koden skal utvikles på Git, med Maven og kjøre tester på Travis-CI
* Koden skal leverer med god testdekning
* Det skal ikke forekomme SQL Injection feil
* Databasepassord skal være konfigurert og der det er mulig for meg å se det skal databasepassord være sterke
* README-fil må dokumentere hvordan man bygger, konfigurerer og kjører løsningen
* README-fil må dokumentere designet på løsningen
* Koden skal følge god kodestandard som beskrevet underveis i kurset. Se sjekkliste fra siste forelesning for endelig kodestandard

Brukervennlighet *er **ikke*** et vurderingskriterie for karakteren.

## Vedlegg: Eksempel på frontend kode

### `index.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>KristianiaProject</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h1>Project view</h1>

<div id="projectMembers"></div>

<div>
    <a href="newWorker.html">Add new project member</a>
</div>
</body>
<script>
fetch("/projectMembers")
    .then(function(response) {
        return response.text();
    }).then(function(text) {
        document.getElementById("projectMembers").innerHTML = text;
    });
</script>
</html>
```

### `newWorker.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add project member | KristianiaProject</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h1>Add project member</h1>

<form method="POST" action="/members">
    <label>Member name: <input type="text" name="full_name" /></label>
    <button>Add</button>
</form>

<div>
    <a href=".">Return to front page</a>
</div>
</body>
</html>
```

### `style.css`

```css
body {
    background-color: lime;
}
```

## Vedlegg: Sjekkliste for innlevering

* [x] Dere har registrert link til GitHub repository i Wiseflow
* [x] Koden er sjekket inn på github.com/Westerdals-repository
* [x] GitHub repository er private, men delt med gruppen dere gjør hverandrevurdering på
* [x] Dere har mottatt minst 2 positive og 2 korrektive GitHub issues i github repository fra en annen gruppe
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [x] `README.md` inneholder en korrekt link til Travis CI
* [x] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det 
* [x] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [x] `README.md` inneholder link til en diagram som viser datamodellen
* [x] Dere har gitt minst 2 positive og 2 korrektive GitHub issues til en annen gruppe og inkluderer link til disse fra README.md

### Koden

* [x] `mvn package` bygger en executable jar-fil
* [x] Koden inneholder et godt sett med tester
* [x] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til å liste ut data fra databasen
* [x] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `task-manager.properties` for å connecte til databasen
* [x] Programmet bruker Flywaydb for å sette opp databaseskjema
* [x] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart
* [x] `.gitignore` inneholder nødvendige filer

### Funksjonalitet

* [x] Programmet kan liste prosjektdeltagere fra databasen
* [x] Programmet lar bruker opprette nye prosjektdeltagere i databasen
* [x] Programmet kan opprette og liste prosjektoppgaver fra databasen


## Vedlegg: Mulighet for ekstrapoeng

* [x] Avansert datamodell (mer enn 3 tabeller)
* [x] Avansert funksjonalitet (redigering av prosjektmedlemmer, statuskategorier, prosjekter)
* [ ] Implementasjon av cookies for å konstruere sesjoner
* [x] UML diagram som dokumenterer datamodell og/eller arkitektur (presentert i README.md)
* [x] Rammeverk rundt Http-håndtering (en god HttpMessage class med HttpRequest og HttpResponse subtyper) som gjenspeiler RFC7230
* [x] Korrekt håndtering av norske tegn i HTTP
* [x] God bruk av DAO-pattern
* [ ] Link til video med god demonstrasjon av ping-pong programmering
* [ ] Annet
