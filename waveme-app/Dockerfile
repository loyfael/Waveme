# Étape 1 – Build Expo Web
FROM node:20.11-slim as builder

WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .

# Build statique pour le web
RUN npx expo export --platform web

# Étape 2 – Serve via Nginx
FROM nginx:stable-alpine

# Suppression de la conf par défaut
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copie du build statique
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
