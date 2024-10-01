# Utiliser l'image Node.js appropriée
FROM node:20-bullseye

# Installer expo-cli globalement
RUN npm install -g expo-cli@5.11.3

# Installer les dépendances spécifiques
RUN apt-get update && apt-get install -y \
    watchman \
    && rm -rf /var/lib/apt/lists/*

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers package.json et package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier le reste de l'application
COPY . .

# Exposer les ports utilisés par Expo
EXPOSE 19000 19001 19002

# Définir les variables d'environnement nécessaires
ENV EXPO_DEVTOOLS_LISTEN_ADDRESS=0.0.0.0

# Démarrer le serveur Expo
CMD ["npm", "start"]
