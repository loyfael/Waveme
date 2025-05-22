#!/bin/bash

set -e

# Load environment variables from .env
export $(grep -v '^#' .env | xargs)

# Step 1 - Install mc if missing
if ! command -v mc &> /dev/null; then
  echo "📦 Installing MinIO Client (mc)..."
  curl -sO https://dl.min.io/client/mc/release/linux-amd64/mc
  chmod +x mc
  sudo mv mc /usr/local/bin/
  echo "✅ MinIO Client installed."
else
  echo "✅ MinIO Client already installed."
fi

# Step 2 - Prompt for application.properties
echo
echo "📝 Please make sure src/main/resources/application.properties is filled in the backend."
read -p "Press Enter when it's ready..."

# Step 3 - Build & start containers (except backend)
echo
echo "🔍 Checking if containers are already running..."
if docker ps --format '{{.Names}}' | grep -q waveme-postgres; then
  echo "✅ Containers seem to be running already. Waiting for 10 seconds..."
  sleep 10
else
  echo "🔨 Building containers..."
  docker-compose build --no-cache

  echo "🚀 Starting containers (excluding backend)..."
  docker-compose up -d postgres pgadmin minio
fi

# Step 4 - Show local IP
echo
LOCAL_IP=$(ip -4 addr show | grep inet | grep -v 127.0.0.1 | awk '{print $2}' | cut -d/ -f1 | head -n 1)
echo "🌐 Detected local IP: $LOCAL_IP"
echo
echo "🔑 Open pgAdmin at: http://localhost:5050/"
echo "👉 Create a server named 'waveme' using IP: $LOCAL_IP"
echo "ℹ️ Use credentials from your .env file / docker-compose.yml"
read -p "Press Enter when the server is created in pgAdmin..."

# Step 5 - Ask if backend is run locally or with Docker
echo
read -p "❓ Do you want to run the backend locally (e.g. from IntelliJ)? (y/n): " local_backend

if [[ "$local_backend" == "y" || "$local_backend" == "Y" ]]; then
  read -p "👉 Start the backend locally, then press Enter once it started."
else
  echo "🚀 Starting backend container..."
  docker-compose up -d backend

  echo "⏳ Waiting for backend to start..."
  docker logs -f waveme-backend 2>&1 | grep -m 1 "----------WAVEME BACKEND STARTED----------" > /dev/null

  if ! docker ps --format '{{.Names}}' | grep -q "^waveme-backend$"; then
    echo "❌ Backend container stopped unexpectedly. Check logs:"
    echo "   docker logs waveme-backend"
    exit 1
  fi

  echo "✅ Backend started and initialized."
fi

# Step 6 - Insert roles
echo
echo "🗂️ Inserting default roles into the database..."
docker exec -i waveme-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" <<EOF
INSERT INTO role (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
EOF
echo "✅ Roles inserted."

# Step 7 - Create MinIO bucket
echo
echo "📦 Checking/Creating MinIO bucket 'waveme'..."
mc alias set local http://localhost:9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD" > /dev/null

if ! mc ls local/waveme > /dev/null 2>&1; then
  mc mb local/waveme
  echo "✅ Bucket 'waveme' created."
else
  echo "ℹ️ Bucket 'waveme' already exists."
fi

# Done
echo
echo "🎉 Waveme environment successfully initialized."
