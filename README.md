Application web de gestion de collection de livres personnels avec suivi de lecture.

##  Installation

### Prérequis
- Java 21
- Node.js 20+
- PostgreSQL

### 1. Base de données
```sql
CREATE DATABASE bookmanagement;
```

### 2. Backend (Spring Boot)
```bash
cd bookmanagement
mvn clean install
mvn spring-boot:run
```

### 3. Frontend (Angular)
```bash
cd bookmanagementFront
npm install
npx ng serve
```

## Utilisation

1. **Accéder à l'app** : http://localhost:4200
2. **S'inscrire** avec email/mot de passe
3. **Se connecter** à son compte
4. **Ajouter des livres** à sa collection
5. **Suivre sa lecture** : marquer pages lues, voir progression
6. **Consulter statistiques** de lecture

## Configuration

Modifier `application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmanagement
spring.datasource.username=postgres
spring.datasource.password=postgreSQL
```

## Fonctionnalités

- Gestion collection de livres privée
- Suivi progression de lecture (pages/pourcentage)
- Historique sessions de lecture
- Statistiques personnelles
- Notes sur les livres
