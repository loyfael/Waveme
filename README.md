# Waveme Project
Waveme is a social networking application focused on memes sharing. It consists of a React Native frontend using Expo and a Spring Boot Java backend with PostgreSQL as database. The project is containerized using Docker and orchestrated with Docker Compose.
> [!WARNING]
> This project is part of a student project and is provided as-is. While every effort has been made to ensure its functionality, the authors cannot be held responsible for any critical issues, bugs, or problems encountered while using the project.
## Usefull links
- [FIGMA](https://www.figma.com/design/Y2lEnBAA5OJLVWoeQz6Ptd/Waveme?node-id=0-1&node-type=canvas&t=GhNvvwdEAVWkzJTs-0)
## Get started !
1. Clone `git clone https://github.com/loisdps/Waveme.git`
2. Change directory to the project root: `cd Waveme`
3. Open the project in your favorite IDE (e.g., IntelliJ IDEA for backend, VS Code for frontend).
4. Install dependencies:
   - For the frontend, run `npm install` or `yarn install`.
   - For the backend, run `./mvnw install` or `mvn install` (if you have Maven installed globally).
5. Start the Docker containers:
   ```bash
   docker compose up -d
   ```
   This will start the PostgreSQL, MongoDB, MinIO, and pgAdmin containers.
6. Open the frontend in your browser:
   - For web: [http://localhost:3000/](http://localhost:3000/)
   - For mobile: Use Expo Go app on your device or emulator.
   - For backend API testing, you can use tools like Postman or cURL to interact with the endpoints.
   - For pgAdmin, open [http://localhost:5050/](http://localhost:5050/) to manage your PostgreSQL database.
## Project Structure
- `waveme-app`: Contains the React Native frontend code.
- `waveme-backend`: Contains the Spring Boot backend code.
- `config`: Contains configuration files for the project, including environment variables and application properties.
- `docker-compose.yml`: Docker Compose file to orchestrate the containers.
- `README.md`: This file, providing an overview and setup instructions for the project.

# Waveme Project Setup Guide
# Backend Setup

## 📁 Step-by-step Setup

### 1. Environment Configuration

* Copy the `.env` file from the `📁config` folder to the project root.
* Copy `application.properties` from `📁config` to the backend's `src/main/resources/` folder.

### 2. Start Containers (Except Backend)

```bash
docker compose up -d
```

**Do not** start the backend yet.

## 📂 Database Setup
### 3. PostgreSQL (via pgAdmin)

* Open: [http://localhost:5050/](http://localhost:5050/)
* Create a **new database**: `waveme`
* Use the following connection info:

  * **Host**: your local IP (e.g., `192.168.x.x`)
  * **Username**: `admin`
  * **Password**: see `📁config`

## 📆 Object Storage – MinIO

* Open: [http://localhost:9001/](http://localhost:9001/)

  * (Port 9000 is for MinIO API – you can ignore it)
* Create a new **Access Key** named `minio`
* Copy the generated key and paste it into the backend's `application.properties` under the `access-key` property.

## 🚀 Backend Initialization

* Now start the backend.
* On first run, it will:

  * Create all required **PostgreSQL** tables
  * Initialize **MongoDB**

## 🧾 Insert Roles Manually

In the `role` table of PostgreSQL, run:
```sql
INSERT INTO role (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
```
## ⚠️ Warning about MongoDB
> **Do NOT have MongoDB installed locally.**
> Since we already run MongoDB in a Docker container, having a local instance may cause **connection conflicts** with the backend.
> If you have MongoDB installed, please **uninstall it** before proceeding with the Waveme project setup.

# Frontend Setup
## 📱 Mobile Setup
1. Open the `waveme-app` folder in your terminal.
2. Run the following command to install dependencies:
   ```bash
   npm install
   ```
   or
   ```bash
   yarn install
   ```
3. Start the Expo development server:
   ```bash
   npm start
   ```
   or
   ```bash
   yarn start
   ```
4. Open the Expo Go app on your mobile device or emulator.
5. Scan the QR code displayed in your terminal or browser to run the app on your device.
6. If you encounter any issues, ensure that your device is connected to the same network as your development machine.
7. For web testing, you can run:
   ```bash
   npm run web
   ```
   or
   ```bash
   yarn web
   ```
   This will open the app in your default web browser.

## ✅ You're all set!
You can now start working with the Waveme project locally.
**You're ready !**
