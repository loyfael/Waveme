# Utiliser l'image Node.js appropriée
FROM node:20.11-slim

# Installer les dépendances système nécessaires
RUN apt-get update && apt-get install -y

# Installer Expo CLI globalement
RUN npm install -g expo-cli

# Définir le dossier de travail
WORKDIR /app

# Copier package.json et package-lock.json pour installer les dépendances
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier le reste de l'application dans le conteneur
COPY . .

# Exposer le port utilisé
EXPOSE 3000

RUN yarn install

# Commande pour démarrer l'application
CMD ["npm", "start"]