@echo off
echo ========================================
echo    Deploy Chamado Petro - Vercel
echo ========================================
echo.

echo [1/5] Verificando dependencias...
where git >nul 2>nul
if %errorlevel% neq 0 (
    echo ERRO: Git nao encontrado. Instale o Git primeiro.
    pause
    exit /b 1
)

where node >nul 2>nul
if %errorlevel% neq 0 (
    echo ERRO: Node.js nao encontrado. Instale o Node.js primeiro.
    pause
    exit /b 1
)

echo [2/5] Verificando arquivos necessarios...
if not exist "vercel.json" (
    echo ERRO: vercel.json nao encontrado.
    echo Execute este script na raiz do projeto.
    pause
    exit /b 1
)

if not exist "frontend\package.json" (
    echo ERRO: frontend\package.json nao encontrado.
    pause
    exit /b 1
)

echo [3/5] Instalando dependencias do frontend...
cd frontend
npm install
if %errorlevel% neq 0 (
    echo ERRO: Falha ao instalar dependencias.
    pause
    exit /b 1
)

echo [4/5] Testando build local...
npm run build
if %errorlevel% neq 0 (
    echo ERRO: Falha no build local.
    echo Verifique os erros acima antes de fazer deploy.
    pause
    exit /b 1
)

cd ..

echo [5/5] Preparando para deploy...
echo.
echo ========================================
echo           PROXIMO PASSO
echo ========================================
echo.
echo 1. Commit e push suas alteracoes para o GitHub:
echo    git add .
echo    git commit -m "Preparar para deploy Vercel"
echo    git push origin main
echo.
echo 2. Acesse https://vercel.com
echo 3. Clique em "New Project"
echo 4. Importe seu repositorio
echo 5. Configure:
echo    - Framework: Next.js
echo    - Root Directory: frontend
echo    - Build Command: npm run build
echo    - Output Directory: .next
echo.
echo 6. Adicione a variavel de ambiente:
echo    - NEXT_PUBLIC_API_URL: https://[SEU-BACKEND].onrender.com
echo.
echo 7. Clique em "Deploy"
echo.
echo ========================================
echo Para o backend, siga o guia DEPLOY-VERCEL.md
echo ========================================
echo.
pause