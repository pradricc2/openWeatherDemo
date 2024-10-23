# openWeatherDemo

## Descrizione del Progetto
openWeatherDemo è un'applicazione basata su Spring Boot che integra i dati meteo forniti dall'API di OpenWeather. L'obiettivo è raccogliere, elaborare e fornire previsioni meteo accurate. Il progetto utilizza Spring Batch per l'elaborazione dei dati e Apache Camel per l'integrazione con servizi esterni.

## Funzionalità Principali
- **Raccolta Dati Meteo**: L'applicazione recupera dati meteo in tempo reale da OpenWeather.
- **Elaborazione dei Dati**: Utilizza Spring Batch per elaborare i dati meteo e fornire previsioni accurate.
- **Persistenza Dati**: I dati vengono memorizzati in due database, PostgreSQL e MySQL, utilizzando JPA per la gestione della persistenza.
- **API REST**: Fornisce un'API REST per ottenere i dati meteo elaborati.

## Struttura del Progetto
- **`pom.xml`**: Definisce le dipendenze del progetto, come Spring Boot, Apache Camel, JPA, PostgreSQL e MySQL.
- **`src/main/java`**: Contiene il codice sorgente dell'applicazione.
  - **`DemoApplication.java`**: Classe principale per l'avvio dell'applicazione.
  - **Config**: Contiene le classi di configurazione per PostgreSQL, MySQL e Spring Batch.
  - **Controller**: `WeatherController` gestisce le richieste HTTP per ottenere i dati meteo.
  - **DAO**: Contiene i repository per interagire con i database PostgreSQL e MySQL.
  - **Service**: `WeatherService` gestisce la logica di business dell'applicazione.
  - **Route**: `OpenWeatherRoute` definisce l'integrazione con l'API di OpenWeather tramite Apache Camel.
  - **Batch**: Configurazioni e listener per l'elaborazione batch dei dati meteo.

## Tecnologie Utilizzate
- **Java 17**: Linguaggio di programmazione principale.
- **Spring Boot**: Framework per facilitare lo sviluppo dell'applicazione.
- **Spring Batch**: Utilizzato per l'elaborazione dei dati meteo.
- **Apache Camel**: Utilizzato per l'integrazione con l'API di OpenWeather.
- **PostgreSQL & MySQL**: Database per memorizzare i dati meteo.
- **Maven**: Strumento di build per gestire le dipendenze.

## Configurazione
1. **Prerequisiti**:
   - Java 17
   - Maven
   - PostgreSQL e MySQL
2. **Clonare il Repository**:
   ```sh
   git clone <URL-del-repository>
   ```
3. **Configurare il Database**:
   - Aggiornare il file `application.properties` con le credenziali dei database.
4. **Compilare e Avviare l'Applicazione**:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## Utilizzo
L'applicazione espone un'API REST per ottenere i dati meteo. Puoi accedere ai dati tramite il seguente endpoint:
- `GET /api/weather`: Ottiene i dati meteo attuali e le previsioni.

