# 🌊 Waveme Project
<div align="center">

![Waveme Logo](https://img.shields.io/badge/Waveme-Social%20Memes%20Platform-blue?style=for-the-badge)<br>
**A modern social networking application focused on memes sharing**<br>
[![React Native](https://img.shields.io/badge/React_Native-Expo-61DAFB?style=flat&logo=react)](https://reactnative.dev/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-Java-6DB33F?style=flat&logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791?style=flat&logo=postgresql)](https://postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-47A248?style=flat&logo=mongodb)](https://mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=flat&logo=docker)](https://docker.com/)
</div>

## 📋 Table of Contents
- [🎯 Project Overview](#-project-overview)
- [👥 Contributors](#-contributors)
- [🏗️ Architecture](#️-architecture)
- [🚀 Quick Start](#-quick-start)
- [⚙️ Backend Setup](#️-backend-setup)
- [📱 Frontend Setup](#-frontend-setup)
- [📚 Resources](#-resources)

## 🎯 Project Overview
Waveme is a comprehensive social networking platform designed for meme enthusiasts. Built with modern technologies, it offers a seamless experience across mobile and web platforms.
### ✨ Key Features
- 📱 Cross-platform mobile application (iOS/Android)
- 🌐 Web application support
- 🖼️ Image sharing and meme creation
- 👤 User authentication and profiles
- 🔒 Role-based access control
- 💬 Social interactions and commenting
- ☁️ Cloud storage integration

### 🛠️ Technology Stack
- **Frontend**: React Native with Expo
- **Backend**: Spring Boot (Java)
- **Databases**: PostgreSQL (relational), MongoDB (document store)
- **Storage**: MinIO (S3-compatible object storage)
- **Infrastructure**: Docker & Docker Compose
- **Admin Tools**: pgAdmin for database management

> [!WARNING]
> This project is part of a student collaboration and is provided as-is. While every effort has been made to ensure its functionality, the authors cannot be held responsible for any critical issues, bugs, or problems encountered while using the project.

## 👥 Contributors
This project was developed collaboratively by:
- **Loyfaël (me)** - Full-stack dev, specialized in backend.
- **Brice Martin** - Full-stack dev, specialized in frontend/mobile.

*Special thanks to all contributors who made this project possible.*
## 📚 Resources
- 🎨 [**Figma Design**](https://www.figma.com/design/Y2lEnBAA5OJLVWoeQz6Ptd/Waveme?node-id=0-1&node-type=canvas&t=GhNvvwdEAVWkzJTs-0) - UI/UX mockups and design system
- 📖 [**API Documentation**](http://localhost:8080/swagger-ui.html) - Interactive API documentation (when running locally)
## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Native  │    │   Spring Boot   │    │   PostgreSQL    │
│   Frontend      │◄──►│   Backend       │◄──►│   Database      │
│   (Expo)        │    │   (Java)        │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                       ┌────────┼────────┐
                       │                 │
              ┌─────────▼──┐    ┌─────────▼──┐
              │  MongoDB   │    │   MinIO    │
              │  (NoSQL)   │    │ (Storage)  │
              └────────────┘    └────────────┘
```

### 📁 Project Structure
```
Waveme/
├── 📱 waveme-app/           # React Native frontend
│   ├── src/
│   ├── assets/
│   ├── package.json
│   └── app.json
├── ⚙️ waveme-backend/       # Spring Boot backend
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── Dockerfile
├── 🔧 config/               # Configuration files
│   ├── .env
│   └── application.properties
├── 🐳 docker-compose.yml    # Container orchestration
└── 📖 README.md             # This documentation
```

## 🚀 Quick Start

### Prerequisites
- 🐳 Docker & Docker Compose
- ☕ Java 21
- 📱 Node.js 20+
- 📋 Git
### ⚡ Fast Setup
```bash
# 1. Clone the repository
git clone https://github.com/loisdps/Waveme.git
cd Waveme

# 2. Start infrastructure services
docker compose up -d

# 3. Install dependencies
cd waveme-app && npm install
cd ../waveme-backend && ./mvnw install
```

---

# 📖 Detailed Setup Guide
# ⚙️ Backend Setup

## � Prerequisites
- ☕ Java 21 or higher
- 🐳 Docker & Docker Compose
- 🌐 Internet connection for downloading dependencies (lol)
## 📋 Step-by-step Configuration
### 1. 📁 Environment Configuration
**Copy configuration files to correct locations:**
```bash
# Copy environment variables to project root
cp config/.env ./

# Copy application properties to backend resources
cp config/application.properties waveme-backend/src/main/resources/
```
### 2. 🐳 Start Infrastructure Services
```bash
# Start all containers except the backend
docker compose up -d
```

**Services started:**
- 🐘 PostgreSQL (port 5432)
- 🍃 MongoDB (port 27017)
- 📦 MinIO (ports 9000/9001)
- 🛠️ pgAdmin (port 5050)

### 3. �️ Database Configuration
#### PostgreSQL Setup via pgAdmin
1. **Access pgAdmin**: [http://localhost:5050/](http://localhost:5050/)
2. **Login credentials**: See `config/.env` file
3. **Create database**: 
   - Right-click "Servers" → Create → Server
   - **Name**: `Waveme-DB`
   - **Connection tab**:
     - **Host**: Your local IP (e.g., `192.168.1.100`)
     - **Port**: `5432`
     - **Username**: `admin`
     - **Password**: See `config/.env`
4. **Create database**: `waveme`
#### 💡 Quick Database Connection Test
```bash
# Test PostgreSQL connection
docker exec -it waveme-postgres psql -U admin -d waveme -c "\dt"
```
### 4. � Object Storage Configuration (MinIO)
1. **Access MinIO Console**: [http://localhost:9001/](http://localhost:9001/)
2. **Login**: See credentials in `config/.env`
3. **Create Access Key**:
   - Navigate to "Access Keys"
   - Click "Create access key"
   - **Name**: `minio`
   - Copy generated **Access Key** and **Secret Key**
4. **Update Backend Configuration**:
   - Edit `waveme-backend/src/main/resources/application.properties`
   - Update MinIO credentials:
     ```properties
     minio.access-key=YOUR_GENERATED_ACCESS_KEY
     minio.secret-key=YOUR_GENERATED_SECRET_KEY
     ```
### 5. 🚀 Backend Initialization
```bash
cd waveme-backend

# Build and start the backend
./mvnw spring-boot:run
```
**On first startup, the backend will:**
- ✅ Create all PostgreSQL tables automatically
- ✅ Initialize MongoDB collections
- ✅ Set up MinIO buckets
- ✅ Apply database migrations

### 6. 👤 Initialize User Roles
**Connect to PGADMIN and run:**
```sql
-- Insert default roles
INSERT INTO role (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
```

**Via pgAdmin Query Tool or command line:**
```bash
docker exec -it waveme-postgres psql -U admin -d waveme -c "
INSERT INTO role (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
"
```
### 7. ✅ Verify Backend Status
**Check if services are running:**
```bash
# Check containers status
docker compose ps

# Check backend logs
docker compose logs -f waveme-backend

# Test API endpoint
curl http://localhost:8080/api/health
```

**Expected endpoints:**
- 🌐 **Backend API**: [http://localhost:8080](http://localhost:8080)
- 📖 **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- 🛠️ **pgAdmin**: [http://localhost:5050](http://localhost:5050)
- 📦 **MinIO Console**: [http://localhost:9001](http://localhost:9001)

## ⚠️ MongoDB Warning
> **❌ Do NOT install MongoDB locally!**
> Since we use MongoDB in Docker, having a local MongoDB instance can cause:
> - 🔌 Port conflicts (27017)
> - 🔗 Connection issues
> - 📊 Data inconsistencies
> 
> **If you have MongoDB installed locally, please uninstall it before proceeding.**

## 🔧 Troubleshooting
<details>
<summary>Common Issues & Solutions</summary>
### Database Connection Issues
```bash
# Check if PostgreSQL is running
docker ps | grep postgres

# Reset PostgreSQL container
docker compose restart postgres
```

### MinIO Access Key Issues
```bash
# Reset MinIO container
docker compose restart minio

# Check MinIO logs
docker compose logs minio
```

### Backend Won't Start
```bash
# Clean and rebuild
cd waveme-backend
./mvnw clean install
./mvnw spring-boot:run
```

</details>

# 📱 Frontend Setup

## � Prerequisites
- �📱 Node.js 20+ and npm/yarn
- 📲 Expo CLI (`npm install -g @expo/cli`)
- 📱 Expo Go app (for mobile testing)
- 🌐 Modern web browser (for web testing)

## 📋 Installation Steps

### 1. 📦 Install Dependencies

```bash
# Navigate to frontend directory
cd waveme-app

# Install dependencies (choose one)
npm install
# OR
yarn install
```
### 2. 🔧 Environment Configuration
Create `.env` file in `waveme-app/` directory:
```env
# API Configuration
EXPO_PUBLIC_API_URL=http://localhost:8080/api
EXPO_PUBLIC_WS_URL=ws://localhost:8080/ws

# Development Configuration
EXPO_PUBLIC_ENV=development
```

### 3. 🚀 Start Development Server
```bash
# Start Expo development server
npm start
# OR
yarn start
```

**This will open the Expo Developer Tools in your browser and display a QR code.**

## 📱 Testing Options
### 📲 Mobile Device Testing
1. **Install Expo Go** on your mobile device:
   - 📱 [iOS App Store](https://apps.apple.com/app/expo-go/id982107779)
   - 🤖 [Google Play Store](https://play.google.com/store/apps/details?id=host.exp.exponent)
2. **Connect your device**:
   - Ensure your device is on the **same Wi-Fi network** as your development machine
   - Scan the QR code with Expo Go app (Android) or Camera app (iOS)
3. **Alternative connection methods**:
   ```bash
   # Send link via email/SMS
   npm run start -- --send-to your-email@example.com
   
   # Use tunnel connection (if local network doesn't work)
   npm run start -- --tunnel
   ```

### 🌐 Web Browser Testing
```bash
# Start web version
npm run web
# OR
yarn web
```
**This will open the app in your default web browser at [http://localhost:19006](http://localhost:19006)**
### 📱 Emulator Testing
#### Android Emulator
```bash
# Start Android emulator (requires Android Studio)
npm run android
# OR
yarn android
```
#### iOS Simulator (macOS only)
```bash
# Start iOS simulator (requires Xcode)
npm run ios
# OR
yarn ios
```
## 🔧 Available Scripts
```bash
# Development
npm start          # Start Expo development server
npm run web        # Start web version
npm run android    # Start Android emulator
npm run ios        # Start iOS simulator

# Building
npm run build      # Build for production
npm run build:web  # Build web version

# Utilities
npm run lint       # Run ESLint
npm run test       # Run tests
npm run clear      # Clear Expo cache
```
## 🛠️ Development Tools
### 📊 Expo Developer Tools
- **Metro Bundler**: Shows bundling progress and errors
- **Device Logs**: Real-time logging from your device
- **Performance Monitor**: Track app performance
- **Remote Debugging**: Debug with Chrome DevTools
### 🔍 Debugging Tips
```bash
# Clear Expo cache (if experiencing issues)
npx expo start --clear

# Reset Metro bundler cache
npx expo start --clear --reset-cache

# Enable remote debugging
# Shake device → "Debug remote JS"
```
## 📱 Device Requirements
### Minimum Requirements
- **iOS**: iOS 11+
- **Android**: Android 5+ (API 21)
- **Web**: Modern browsers (Chrome, Firefox, Safari, Edge)
### Recommended for Development
- **iOS**: iOS 13+
- **Android**: Android 8+ (API 26)
- **RAM**: 4GB+ recommended
- **Network**: Stable Wi-Fi connection
## 🔧 Troubleshooting
<details>
<summary>Common Issues & Solutions</summary>
### QR Code Not Working
```bash
# Try tunnel connection
npx expo start --tunnel

# Or use local IP
```bash
npx expo start --lan
```
### Network Connection Issues
1. Ensure both devices are on the same network
2. Check firewall settings
3. Try using tunnel mode
4. Restart your router if necessary

### App Won't Load
```bash
# Clear cache and restart
npx expo start --clear
rm -rf node_modules && npm install
```
### Performance Issues
- Close other apps on your device
- Use a physical device instead of emulator
- Enable development mode on your device
</details>

## ✅ Verification
Your frontend setup is complete when you can:
- ✅ See the Expo DevTools in your browser
- ✅ Scan QR code and load app on mobile device
- ✅ Open web version in browser
- ✅ See the Waveme login/splash screen
- ✅ Connect to backend API (check network requests)

---

# 🎉 You're All Set!
Congratulations! You now have the complete Waveme project running locally.
## 🚀 Next Steps
1. **🔐 Create an admin account** through the app
2. **📸 Upload some test memes** to verify storage
3. **🧪 Test API endpoints** with tools like Postman
4. **🎨 Explore the Figma designs** for UI reference
5. **📖 Check the API documentation** at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**Happy coding! 🚀**
