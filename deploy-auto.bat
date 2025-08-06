@echo off
title Chamado Petro - Deploy Automatico
color 0a

echo ===============================================
echo  CHAMADO PETRO - DEPLOY AUTOMATICO
echo ===============================================
echo.

:START
echo [%time%] Iniciando novo tunel ngrok...
echo.

:: Matar processos ngrok antigos
taskkill /f /im ngrok.exe >nul 2>&1

:: Aguardar 2 segundos
timeout /t 2 /nobreak >nul

:: Iniciar ngrok em background e capturar URL
echo Aguarde... criando tunel...
start /min "ngrok-tunnel" ngrok http 3000

:: Aguardar ngrok inicializar
timeout /t 8 /nobreak >nul

:: Obter URL do ngrok via API local
for /f "tokens=*" %%i in ('curl -s http://127.0.0.1:4040/api/tunnels ^| findstr "public_url" ^| findstr "https" ^| cut -d":" -f2,3 ^| tr -d "\"," ^| head -1') do set NGROK_URL=%%i

if defined NGROK_URL (
    echo.
    echo ===============================================
    echo  TUNEL ATIVO!
    echo ===============================================
    echo  URL Publica: %NGROK_URL%
    echo ===============================================
    echo.
    
    echo Configurando backend...
    set FRONTEND_URL=%NGROK_URL%
    
    echo.
    echo INSTRUCOES:
    echo 1. Configure no backend: set FRONTEND_URL=%NGROK_URL%
    echo 2. Reinicie o backend: mvn spring-boot:run
    echo 3. Reinicie o frontend: npm run dev
    echo 4. Acesse: %NGROK_URL%
    echo 5. Clique em "Visit Site" na pagina de aviso
    echo.
    echo IMPORTANTE: Esta janela deve permanecer aberta!
    echo Se o tunel cair, sera recriado automaticamente.
    echo.
    
    :: Monitorar túnel
    :MONITOR
    timeout /t 30 /nobreak >nul
    curl -s %NGROK_URL% >nul 2>&1
    if errorlevel 1 (
        echo.
        echo [%time%] Tunel caiu! Reiniciando...
        goto START
    )
    goto MONITOR
    
) else (
    echo ERRO: Nao foi possivel obter URL do ngrok
    echo Tentando novamente em 10 segundos...
    timeout /t 10 /nobreak >nul
    goto START
)

pause