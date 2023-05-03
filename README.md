# SafetyNetAlerts API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

Il s'agit d'une API Rest développée en Java avec Spring Boot 3.

Cette API permet:
* en cas d'incendie de fournir des informations sur les personnes présentes dans le bâtiment en feu, tels que leurs numéros de téléphone. 
* en cas d’alerte ouragan de fournir les emails des habitants de la région.
* en cas d'inondation de fournir des informations spécifiques (âges et leurs antécédents médicaux: traitements, allergies, etc.) sur les personnes dans la zone.

Les données sont enregistrées dans un fichier JSON.

## Pour commencer

Faire un fork du projet ou le cloner.

### Pré-requis

* Java				: jdk 17.0.7
* Spring Boot		: 3.0.6
* Maven				: 3.8.7
* Jsoniter			: 0.9.19
* SpringDoc OpenApi	: 2.0.4
* Maven-Site		: 3.12.1
* JaCoCo			: 0.8.7

### Créer un fichier JAR

_Executez la commande_ ``mvn package``

## Démarrer l'API

Remplacer "cheminDuProjet" par le chemin du projet sur votre ordinateur.

_Executez la commande_ ``java -jar /cheminDuProjet/target/SafetyNet-Alerts-0.0.1-SNAPSHOT.jar``

## Tester l'API

Dans un navigateur internet ou Postman copier et coller les adresses 'http:...' ci-dessous.

_exemples :_
* Liste les informations de John Boyd: http://localhost:8080/person?firstName=John&lastName=Boyd
* Liste la date de naissance et les données médicale de John Boyd: http://localhost:8080/medicalRecord?firstName=John&lastName=Boyd
* Liste des casernes de pompiers avec le numéro de caserne 1: http://localhost:8080/firestation/1
* Liste des personnes dépendantes de la caserne de pompiers 2 http://localhost:8080/firestation?stationNumber=2
* Liste des enfants et des autres membres de la famille à une adresse: http://localhost:8080/childAlert?address=1509%20Culver%20St
* Liste des numéros de téléphones des personnes dépendantes de la caserne de pompiers 1: http://localhost:8080/phoneAlert?firestation=1
* Liste des courriels des habitants d'une ville: http://localhost:8080/communityEmail?city=Culver

Pour voir toutes les possiblités de l'API utiliser la documentation.

## Ouvrir la documentation

Dans un navigateur internet copier et coller l'adresse http://localhost:8080/swagger-ui/index.html#/

## Lancer les tests unitaires

_Executez la commande_ ``mvn clean test``

## Créer un rapport JaCoCo du projet

_Executez la commande_ ``mvn clean verify``

## Voir le rapport JaCoCo

Remplacer "cheminDuProjet" par le chemin du projet sur votre ordinateur.  
Dans un navigateur internet copier et coller l'adresse ``/cheminDuProjet/target/site/jacoco-ut/index.html``

## Créer un rapport Surefire sur les tests unitaires du projet

_Executez la commande_ ``mvn test``

## Voir le rapport Surefire

Remplacer "cheminDuProjet" par le chemin du projet sur votre ordinateur.  
Dans un navigateur internet copier et coller l'adresse ``/cheminDuProjet/target/site/surefire-report.html``

## Créer un site du projet

_Executez la commande_ ``mvn site``

## Voir le site

Remplacer "cheminDuProjet" par le chemin du projet sur votre ordinateur.  
Dans un navigateur internet copier et coller l'adresse ``/cheminDuProjet/target/site/index.html``

## Développé avec

* eclipse 4.27.0

## Versions

Dernière version: SNAPSHOT: 0.0.1

## Auteur

* **Philippe PERNET** _alias_ [@Philiform](https://github.com/Philiform)

## License

Ce projet n'est pas sous licence

## Erreurs possibles

Lors de l'exécution de la commande: ``mvn clean verify``

[ERROR] com/safetynetalert/api/controller/AddressesControllerTest has been compiled by a more recent version of the Java Runtime (class file version 61.0), this version of the Java Runtime only recognizes class file versions up to 55.0

_Solution_: modifier la variable d'environnement JAVA_HOME

* dans l'invite de commande copier et coller: ``JAVA_HOME=/cheminDuJdk/jdk-17.x.x``

_Vérification_:

* dans l'invite de commande copier et coller: ``mvn -v``
* la commande doit afficher une ligne avec: ``Java version: 17.x.x``
