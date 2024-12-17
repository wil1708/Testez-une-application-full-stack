# Présentation du projet

Ce projet est une application full-stack composé d'une API REST Spring connecté à une base de donnée Mysql, et d'un portail pour le front-end par une application Angular.
Les deux applications sont tout simplement nommées back et front.
L'application permet à un administrateur de créer et organiser des sessions de Yoga, et à des utilisateurs de s'y inscrire.
Ce projet m'a permis de réaliser des tests unitaires et d'intégration sur les parties front et back, et des tests end-to-end sur la partie front.
Les instructions ci-jointes vous permettront de télécharger et configurer l'application, puis de réaliser les tests en lançant quelques commandes.

# Téléchargement de l'application

1. Téléchargez l'application à l'adresse https://github.com/wil1708/Testez-une-application-full-stack en cliquant sur CODE puis DOWNLOAD ZIP ; puis unzip du dossier téléchargé.
Ou en utilisant la commande git clone https://github.com/wil1708/Testez-une-application-full-stack à l'endroit que vous souhaitez sur votre disque dur.

# Installation de la version 8 de java

1. Téléchargez la version d'Amazon Corretto 8 (JDK) dans la version de votre système d'exploitation et installez-la sur votre ordinateur.
Ce JDK est nécessaire pour faire tourner la partie back-end de l'application.

# Installation de la base de donnée Mysql

1. Si vous n'avez pas de système de gestion de base de donnée Mysql sur votre ordinateur, téléchargez WAMP ici : https://www.wampserver.com/en/download-wampserver-64bits/ et installez-le.
2. Lancez l'application WAMP et vérifier qu'elle est lancée et fonctionnelle avec une icone W en vert signifiant que les services de WAMP sont lancés (elle apparait en bas à droite de votre bureau si vous utilisez Windows).
3. Cliquez droit sur l'icone de WAMP puis allez dans WAMP settings et vérifiez que "Allow Mysql" est coché ; puis dans Tools vérifiez que Mysql est bien sur le port 3306. S'il ne l'est pas il va falloir le modifier dans WAMP
   en cliquant sur "Use a port other than XXXX".
4. Cliquez gauche sur l'icone de WAMP et lancez phpMyAdmin. Sélectionnez MySQL dans Choix du serveur et entrez vos identifiants. Puis créez une base de donnée nommée test en sélectionnant utf8_general_ci.
5. Votre base de donnée est maintenant prête et crée. Laissez-bien WAMP ouvert et fonctionnel (icone en couleur verte), nous allons maintenant configurer la partie back de l'application.

# Configuration de la partie back-end API REST Spring

1. Dans le dossier de l'application que vous avez téléchargé, ouvrez le sous-dossier back dans l'IDE de votre choix (exemple IntelliJ).
2. Dans le package src->main->java->resources de l'application back, à l'intérieur de votre IDE, vous trouverez le fichier application.properties. Ouvrez-le pour le configurer.
3. Pour la ligne : spring.datasource.url=jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true normalement vous n'avez pas besoin de faire de changement sauf si vous avez décidé d'utiliser un autre port que 3306 pour votre base de données
   Mysql ; auquel cas modifiez la ligne avec le numéro de port choisi pour votre serveur Mysql. Si vous avez nommé votre base de données par autre chose que 'test' vous devez aussi modifier le nom de cette ligne.
4. Ensuite modifiez les username et password des lignes spring.datasource en fonction de vos identifiants crées pour WAMP et permettre l'accès à vos bases de données par l'application back-end.
5. Vous pouvez ensuite ouvrir le fichier SpringBootSecurityJwtApplication dans le package src->main->java pour lancer l'application, ce qui aura pour effet de créer votre base de données nouvellement créee grâce au mode update de votre fichier application.properties déjà configuré.
6. Ensuite ouvrez votre base de données dans phpMyAdmin et lancez le script SQL suivant afin d'obtenir les données de bases nécessaires pour le fonctionnement de l'application :
   
INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');
INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'); 

7. Pour lancer les tests ouvrez un terminal à l'emplacement de l'application back, et lancez la commande mvn clean test.
8. Pour obtenir le coverage de l'application back par les tests, lancez ensuite la commande mvn jacoco:report. Puis drag and drop le fichier index.html dans votre navigateur pour l'ouvrir, se trouvant dans le dossier target->site->jacoco.
9. La configuration de la partie back-end est terminée, nous allons passer à la configuration de la partie front-end dans le prochain chapitre.

# Installation de NodeJs

1. Téléchargez la version 18 de NodeJs ici : https://nodejs.org/en/download/prebuilt-installer et installez-la.

# Configuration de la partie front-end Angular

1. Ouvrez le dossier front dans l'IDE de votre choix (exemple vsCode).
2. Ouvrez un terminal au niveau du chemin du dossier front et lancez la commande npm install pour installer toutes les dépendances du projet.
3. Une fois terminé, pour lancer l'application côté front, toujours dans le terminal au niveau du chemin du dossier front, lancez la commande ng serve. Vous devriez voir apparaitre l'applicatiion ouverte dans un nouvel onglet de votre navigateur.
4. Pour lancer les tests unitaires et d'intégration de la partie front-end, lancez la commande npm test pour les effectuer et obtenir le coverage dans le terminal.
5. Pour lancer les tests cypress end-to-end lancez la commande npm run e2e
6. Si vous avez la question Port 4200 is already in use. Would you like to use a different port Y/n répondez oui (Y).
7. Quelques dizaines de secondes plus tard une fenêtre de test cypress  va s'ouvrir vous demandant de choisir le navigateur à utiliser pour effectuer les tests. Choisissez celui qui vous convient puis cliquez sur les tests pour les effectuer.
8. Pour obtenir ensuite le coverage de l'application par les tests de cypress, lancez la commande npm run e2e:coverage dans un terminal à l'emplacement de l'application front.

# Login avec compte Admin

1. Une fois le front et le back ainsi que la base de donnée Mysql sont lancées, vous pouvez vous connecter sur le portail front avec le compte admin suivant :
login: yoga@studio.com
password: test!1234
   





