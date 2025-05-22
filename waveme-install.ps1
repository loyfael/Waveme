$ErrorActionPreference = "Stop"

# Step 1 - Load .env into environment
$envVars = Get-Content .env | Where-Object { $_ -notmatch "^#|^\s*$" } |
  ForEach-Object {
    $pair = $_ -split '=', 2
    [PSCustomObject]@{ Key = $pair[0]; Value = $pair[1] }
  }

foreach ($var in $envVars) {
  $env:$($var.Key) = $var.Value
}

# Step 2 - Install mc.exe if missing
if (-not (Get-Command "mc.exe" -ErrorAction SilentlyContinue)) {
  Write-Host "📦 Downloading MinIO Client (mc.exe)..."
  Invoke-WebRequest -Uri "https://dl.min.io/client/mc/release/windows-amd64/mc.exe" -OutFile "mc.exe"
  $env:PATH += ";$PWD"
  Write-Host "✅ mc.exe downloaded to current directory."
} else {
  Write-Host "✅ MinIO Client already available."
}

# Step 3 - Prompt for application.properties
Write-Host ""
Write-Host "📝 Please make sure src/main/resources/application.properties is filled in the backend."
Read-Host "Press Enter when ready..."

# Step 4 - Start containers (excluding backend)
Write-Host ""
$containers = docker ps --format "{{.Names}}"
if ($containers -contains "waveme-postgres") {
  Write-Host "✅ Containers seem to be running already. Waiting for 10 seconds..."
  Start-Sleep -Seconds 10
} else {
  Write-Host "🔨 Building containers..."
  docker-compose build --no-cache

  Write-Host "🚀 Starting containers (excluding backend)..."
  docker-compose up -d postgres pgadmin minio
}

# Step 5 - Get local IP
Write-Host ""
$localIP = (Get-NetIPAddress -AddressFamily IPv4 |
  Where-Object { $_.InterfaceAlias -notlike "*Loopback*" -and $_.IPAddress -notlike "169.*" } |
  Select-Object -First 1).IPAddress
Write-Host "🌐 Detected local IP: $localIP"
Write-Host ""
Write-Host "🔑 Open pgAdmin at: http://localhost:5050/"
Write-Host "👉 Create a server named 'waveme' using IP: $localIP"
Write-Host "ℹ️ Use credentials from your .env file / docker-compose.yml"
Read-Host "Press Enter when the server is created in pgAdmin..."

# Step 6 - Run backend locally or in Docker
Write-Host ""
$localBackend = Read-Host "❓ Do you want to run the backend locally (e.g. in IntelliJ)? (y/n)"
if ($localBackend -eq "y" -or $localBackend -eq "Y") {
  Read-Host "👉 Start the backend locally, then press Enter once it shows: ----------WAVEME BACKEND STARTED----------"
} else {
  Write-Host "🚀 Starting backend container..."
  docker-compose up -d backend

  Write-Host "⏳ Waiting for backend to start..."
  $found = $false
  docker logs -f waveme-backend 2>&1 |
    ForEach-Object {
      if ($_ -match "----------WAVEME BACKEND STARTED----------") {
        $found = $true
        break
      }
    }

  if (-not $found) {
    Write-Host "❌ Backend did not start. Check with: docker logs waveme-backend"
    exit 1
  }

  if (-not (docker ps --format "{{.Names}}" | Where-Object { $_ -eq "waveme-backend" })) {
    Write-Host "❌ Backend container stopped. Check logs."
    exit 1
  }

  Write-Host "✅ Backend started successfully."
}

# Step 7 - Insert roles
Write-Host ""
Write-Host "🗂️ Inserting default roles into the database..."
docker exec -i waveme-postgres psql -U $env:POSTGRES_USER -d $env:POSTGRES_DB `
  -c "INSERT INTO role (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;"
docker exec -i waveme-postgres psql -U $env:POSTGRES_USER -d $env:POSTGRES_DB `
  -c "INSERT INTO role (name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;"
docker exec -i waveme-postgres psql -U $env:POSTGRES_USER -d $env:POSTGRES_DB `
  -c "INSERT INTO role (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;"
Write-Host "✅ Roles inserted."

# Step 8 - Create MinIO bucket
Write-Host ""
Write-Host "📦 Checking/Creating MinIO bucket 'waveme'..."
mc.exe alias set local http://localhost:9000 $env:MINIO_ROOT_USER $env:MINIO_ROOT_PASSWORD | Out-Null

try {
  mc.exe ls local/waveme | Out-Null
  Write-Host "ℹ️ Bucket already exists."
} catch {
  mc.exe mb local/waveme
  Write-Host "✅ Bucket created."
}

Write-Host ""
Write-Host "🎉 Waveme environment successfully initialized."
