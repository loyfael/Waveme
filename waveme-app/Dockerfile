# Utiliser l'image Node.js appropriée
FROM node:20.11-slim

# Installer les dépendances système nécessaires
RUN apt-get update && apt-get install -y

# Définir le dossier de travail
WORKDIR /app

# Copier package.json et package-lock.json pour installer les dépendances
COPY package*.json ./

# Configurer npm pour ignorer les erreurs de réseau temporaires
RUN npm config set fetch-retries 5
RUN npm config set fetch-retry-mintimeout 20000
RUN npm config set fetch-retry-maxtimeout 120000

# Installer les dépendances avec réessai en cas d'échec
RUN npm install || npm install && npm cache clean --force

# Vérifier l'installation des modules
RUN ls -la /app/node_modules

# Installer Expo CLI globalement
RUN npm install -g expo-cli

# Copier le reste de l'application dans le conteneur
COPY . .

# Exposer le port utilisé
EXPOSE 3000

# Commande pour démarrer l'application
CMD ["npm", "run", "web"]