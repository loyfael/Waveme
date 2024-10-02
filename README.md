# Waveme Project

Waveme est une application de réseau social axée sur le partage de memes. Elle se compose d'un frontend React Native utilisant Expo et d'un backend Java Spring Boot avec PostgreSQL comme base de données. Le projet est conteneurisé à l'aide de Docker et orchestré avec Docker Compose.

## LIENS UTILES
- [FIGMA](https://www.figma.com/design/Y2lEnBAA5OJLVWoeQz6Ptd/Waveme?node-id=0-1&node-type=canvas&t=GhNvvwdEAVWkzJTs-0)

## Architecture
### `archives`
Contient les archives du projet précédent (à retirer prochainement)
### `waveme-app`
Contient l'application frontend du projet en React Native x Expo.
### `waveme-backend`
Contient l'application backend du projet en Java 21 & Spring Boot.
### `command`
Contient les commandes shells utile du projet.
- `clean.sh` Permet d'entièrement reset le projet sur docker.
-  `restart.sh` Permet de redémarrer le projet.

## Prérequis
- [Docker](https://www.docker.com/get-started) installé sur votre machine.
- [Docker Compose](https://docs.docker.com/compose/install/) installé sur votre machine.
- [Git](https://git-scm.com/downloads) installé sur votre machine.

## Get started !
1. (Demandez l'accès) Clonez le projet `git clone https://github.com/loisdps/Waveme.git`
2. Une fois que c'est fait, à l'aide du docker-compose.template effectuez votre docker-compose.yml et remplissez
les différents identifiants présents à l'intérieur.
3. Une fois que docker et docker-compose sont installés sur votre machine, allez au path du dossier `command` et exécutez `./restart.sh`.
Le script va build l'ensemble des conteneurs. Si besoin plus tard vous pouvez utiliser `clean.sh` pour reset le projet.
> [!NOTE]
> Pour l'application frontend, le build peut être long. Veuillez patienter ~ 5 minutes.
4. Une fois que tout à été build, connectez vous à pgadmin à http://localhost:5050 avec les identifiants que vous avez fourni.
Créez une base de données en appuyant sur "add new server".
- Nommez la bdd exactement pareil que vous l'avez nommé dans le docker-compose.yml
- Pour le host, mettez le nom du conteneur `postgres`. Docker prendra directement l'adresse ip de Postgres.
*Documentation à finir.*

### Vous êtes désormais prêt(e) !