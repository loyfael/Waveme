# Waveme Project

Waveme is a social networking application focused on memes sharing. It consists of a React Native frontend using Expo and a Spring Boot Java backend with PostgreSQL as database. The project is containerized using Docker and orchestrated with Docker Compose.

> [!WARNING]
> This plugin is part of a student project and is provided as-is. While every effort has been made to ensure its functionality, the authors cannot be held responsible for any critical issues, bugs, or problems encountered while using the project.

## LIENS UTILES
- [FIGMA](https://www.figma.com/design/Y2lEnBAA5OJLVWoeQz6Ptd/Waveme?node-id=0-1&node-type=canvas&t=GhNvvwdEAVWkzJTs-0)

## Architecture
### `archives`
Contain all archives (à retirer prochainement)
### `waveme-app`
Frontend project: React Native x Expo.
### `waveme-backend`
Backend project: Java 21 & Spring Boot.

## Dependencies
- [Docker](https://www.docker.com/get-started) installé sur votre machine.
- [Docker Compose](https://docs.docker.com/compose/install/) installé sur votre machine.
- [Git](https://git-scm.com/downloads) installé sur votre machine.

## Get started !
1. Clone `git clone https://github.com/loisdps/Waveme.git`
2. Une fois que c'est fait, à l'aide du docker-compose.template effectuez votre docker-compose.yml et remplissez
les différents identifiants présents à l'intérieur.

### Backend
1. Once the containers have been launched, the backend is logically switched off, as it has no database or configuration. You need to create a application.properties and fill them.
2. Once this has been done, launch pgadmin at http://localhost:5050/, then log in with the credentials present in docker-compose.yml.
3. Once in pgadmin, click on create a server. Name this server `waveme`.
- Get the machine's IP with an `ipconfig` :warning: get the first one!
- Enter the IP in pgadmin.
- Set credentials. Write “admin” in the role.
4. The database is now created. You now need to run the backend once to create the tables.
5. Now go to the “role” table on pgadmin and execute this query:
```sql
INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_MODERATOR');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');
```
6. The database is now ready. We now need to set up postman. In postman, communication with the backend will always be http://localhost:8080/api/otherRoute.
For example, the registration route will always be http://localhost:8080/api/auth/register.

To register a first user, you don't need to authenticate, just fill in the body field:
```json
{
    "pseudo": "test",
    "email": "test@test.com",
    "password": "teeeeeeeeeeeeeeeeeeest",
    "role": "ROLE_USER"
}
```
7. Run the query, and you should get a response of 200. If you check the bdd, the row has been created.
8. Authentication is required to browse the backend (except for login and register routes).

**Register**
- Route: http://localhost:8080/api/auth/register
- Body:
```
{
    "pseudo": "test",
    "email": "test@test.com",
    "password": "teeeeeeeeeeeeeeeeeeest",
        "role": "ROLE_USER"
}
```

**Login**
- Route: http://localhost:8080/api/auth/login
- Body:
```
{
    "pseudo": "test",
    "password": "teeeeeeeeeeeeeeeeeeest"
}
```
Good? Now get the cookie and use it for your other request.

9. How upload a file?
- Login
- Retrieve the cookie.
- Put the cookie in auth -> BearerToken -> Insert in token field and the prefix will be jwtCookie
- For the body, do as shown on the screen
![image](https://github.com/user-attachments/assets/25a822d9-55a4-47b8-b073-c8d63c6c6141)


### Frontend
1. Go to the project and run `npm i` to install the dependencies. (Why doesn't this already work via docker? I've no idea).
2. Go to the URL https://localhost:3000
3. Finish

### You're ready !
