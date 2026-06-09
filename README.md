# TP Java avec MySQL

Ce dépôt contient le TP4 : connexion et manipulation d'une base de données MySQL (ajout, modification, suppression, consultation) depuis un programme Java simple.

Capture d'exemple (console) :

<img width="461" height="222" alt="image" src="https://github.com/user-attachments/assets/8fc88817-7d14-4e3c-94f7-2b9084129f5d" />

Prérequis
- Java JDK installé
- IntelliJ IDEA (ou un autre IDE)
- Laragon (ou autre serveur MySQL) démarré et accessible

Création de la base et de la table (exécuter dans phpMyAdmin ou via un client SQL) :

```sql
CREATE DATABASE IF NOT EXISTS tp4;
USE tp4;

CREATE TABLE IF NOT EXISTS employe (
		matricule INT PRIMARY KEY,
		nom VARCHAR(50) NOT NULL,
		prenom VARCHAR(50) NOT NULL,
		date_naissance VARCHAR(50) NOT NULL,
		ville VARCHAR(50) NOT NULL,
		salaire INT NOT NULL
);
```

Comment lancer le TP

- Avec IntelliJ (recommandé) :
	1. Ouvrir le projet `Test` (le dossier contenant `pom.xml`).
	2. Si IntelliJ propose d'« Importer les changements Maven », cliquer sur **Import Changes** ou ouvrir la fenêtre **Maven** (View → Tool Windows → Maven) et cliquer sur **Reload All Maven Projects**.
	3. Vérifier que Laragon/MySQL est démarré et que la base `tp4` existe.
	4. Ouvrir `src/Main.java` et exécuter la classe `Main`.

- Sans Maven (ajouter le driver manuellement) :
	1. Télécharger `mysql-connector-j` (jar) et l'ajouter aux **Dependencies** du module (Project Structure → Modules → Dependencies → + JARs or directories).
	2. Exécuter `src/Main.java` depuis l'IDE.

