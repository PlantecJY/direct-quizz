# Portail de sondage DirectQuiz
Plateforme de sondage en ligne ; outil pédagogique permettant aux professeurs d'interroger ses élèves en classe sur des questions de cours, et voir en direct les réponses de chacun.  

## Lancement et test du projet 
1. Récupération du projet Netbeans via un clonage Github 
2. Configuration du serveur GlassFish (cf documentation)
3. Configuration de la base de donnée (cf documentation)

## Précisions concernant la base de donnée 
* Initialisation de la BDD avec le script `direct-sondage.sql` (présent dans le dossier DirectQuiz)
* Chiffrement des mots de passe :
Utiliser ce nouveau script `direct-sondage.sql` pour les tests en local, car elle intègre les mots de passe chiffrés (algo MD5) et les modifications sur la longueur du champ mot de passe de l'ancienne base de donnée.

Désormais, les nouveaux comptes crées vont être présents dans la base de données avec des mots de passe chiffrés, donc
pour les tests, pensez à mémoriser le mot de passe d'un compte que vous créez car il ne sera plus possible de le voir
en clair en accédant à la base de donnée.
