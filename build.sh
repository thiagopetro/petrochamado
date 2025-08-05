#!/bin/bash

# Script de build para Render.com

echo "🚀 Iniciando build do Chamado Petro..."

# Verificar se estamos no diretório correto
if [ ! -f "render.yaml" ]; then
    echo "❌ Erro: render.yaml não encontrado!"
    exit 1
fi

# Build do Backend
echo "📦 Construindo Backend Spring Boot..."
cd backend
chmod +x mvnw
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Erro no build do backend!"
    exit 1
fi
cd ..

# Build do Frontend
echo "🎨 Construindo Frontend Next.js..."
cd frontend
npm ci
npm run build
if [ $? -ne 0 ]; then
    echo "❌ Erro no build do frontend!"
    exit 1
fi
cd ..

echo "✅ Build concluído com sucesso!"