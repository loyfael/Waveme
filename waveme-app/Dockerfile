# Étape 1 – Build du frontend React Native Web avec Expo
FROM node:20.11-slim as builder

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# Build avec export (non spécifique à Webpack)
RUN npx expo export --platform web

# Étape 2 – Serveur Nginx
FROM nginx:stable-alpine

# Supprimer config par défaut
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copier les fichiers générés (dans dist par défaut)
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]