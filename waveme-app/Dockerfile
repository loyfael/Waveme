# Utiliser l'image Node.js appropriée
FROM node:20-bullseye

# Installer les dépendances système nécessaires
RUN apt-get update && apt-get install -y \
    watchman \
    && rm -rf /var/lib/apt/lists/*

# Étape 3 : Installer Expo CLI globalement
RUN npm install -g expo-cli

# Étape 4 : Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Étape 5 : Copier les fichiers package.json et package-lock.json dans le conteneur
COPY package*.json ./

# Étape 6 : Installer les dépendances du projet
RUN npm install

# Étape 7 : Copier le reste de l'application dans le conteneur
COPY . .

# Étape 8 : Exposer le port utilisé par Expo (par défaut 19000, 19001, et 19002)
EXPOSE 19000 19001 19002

# Étape 9 : Lancer l'application Expo
CMD ["npm", "start"]
