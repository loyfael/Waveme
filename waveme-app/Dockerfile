# Utiliser l'image Node.js appropriée
FROM node:20.11-slim

# Définir des variables d'environnement pour le développement
ENV NODE_ENV=development
ENV EXPO_DEVTOOLS_LISTEN_ADDRESS=0.0.0.0
ENV REACT_NATIVE_PACKAGER_HOSTNAME=0.0.0.0
ENV WATCHPACK_POLLING=true
ENV PORT=3000

# Installer les dépendances système nécessaires
RUN apt-get update && apt-get install -y \
    watchman \
    curl \
    git \
    && rm -rf /var/lib/apt/lists/*

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de dépendances
COPY package*.json ./
COPY tsconfig.json ./

# Installer toutes les dépendances, y compris devDependencies
RUN npm install

# Exposer les ports nécessaires
EXPOSE 3000 19001 19002 8081

# Commande pour démarrer l'application
CMD ["npx", "expo", "start", "--port", "3000"]