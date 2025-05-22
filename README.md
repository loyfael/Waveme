# Waveme Project
Waveme is a social networking application focused on memes sharing. It consists of a React Native frontend using Expo and a Spring Boot Java backend with PostgreSQL as database. The project is containerized using Docker and orchestrated with Docker Compose.
> [!WARNING]
> This project is part of a student project and is provided as-is. While every effort has been made to ensure its functionality, the authors cannot be held responsible for any critical issues, bugs, or problems encountered while using the project.
## Usefull links
- [FIGMA](https://www.figma.com/design/Y2lEnBAA5OJLVWoeQz6Ptd/Waveme?node-id=0-1&node-type=canvas&t=GhNvvwdEAVWkzJTs-0)
## Architectury
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
# Backend Setup (automated with script)
## 1. Prepare the `.env` file
At the root of your project (same level as `docker-compose.yml`), create a `.env` file with the following variables:
```env
POSTGRES_DB=waveme
POSTGRES_USER=admin
POSTGRES_PASSWORD=your_postgres_password

PGADMIN_EMAIL=admin@waveme.com
PGADMIN_PASSWORD=your_pgadmin_password

MINIO_ROOT_USER=admin
MINIO_ROOT_PASSWORD=your_minio_password
```
Make sure this file is **not committed** to Git (add it to `.gitignore`).
## 2. Fill `application.properties`
In the backend folder, create or update:
```
src/main/resources/application.properties
```
## 3. Run the setup script
From the root of the project, run:

### Linux
```sh
chmod +x install-waveme.sh
./install-waveme.sh
```

### Windows
```ps1
.\install-waveme.ps1
```

This script will:
* Install MinIO Client (`mc`) if missing
* Build and start required Docker containers (PostgreSQL, pgAdmin, MinIO)
* Show your local IP for pgAdmin
* Ask whether you want to run the backend locally or via Docker
* Wait for backend to be fully started
* Insert default roles in the database
* Create the `waveme` bucket in MinIO
## 4. Test the backend
Use Insomnia or Postman to send a request to:
```http
POST http://localhost:9080/api/auth/register
```

With this JSON body:
```json
{
  "pseudo": "test",
  "email": "test@test.com",
  "password": "teeeeeeeeeeeeeeeeeeest",
  "role": "ROLE_USER"
}
```

You should get a `200 OK` and see the new user in the database.

✅ You’re now ready to use the Waveme backend!

1. How upload a file?
- Login
- Retrieve the JWT.
- Put the JWT in auth -> BearerToken -> Insert in token field.
- For the body, do as shown on the screen
![image](https://github.com/user-attachments/assets/25a822d9-55a4-47b8-b073-c8d63c6c6141)
### Frontend
1. Go to the project and run `npm i` to install the dependencies.
2. Go to the URL https://localhost:3000
3. Finish
### You're ready !
