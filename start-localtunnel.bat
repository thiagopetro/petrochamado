@echo off
echo ====================================
echo  Chamado Petro - Deploy com LocalTunnel
echo ====================================
echo.

echo Parando ngrok (se estiver rodando)...
taskkill /f /im ngrok.exe 2>nul

echo.
echo 1. Certifique-se que o backend esta rodando na porta 8081
echo 2. Certifique-se que o frontend esta rodando na porta 3000
echo.

echo 3. Iniciando LocalTunnel para o Frontend (porta 3000)...
start "Frontend LocalTunnel" cmd /k "lt --port 3000"

echo.
echo 4. Aguardando 5 segundos...
timeout /t 5 /nobreak > nul

echo 5. Iniciando LocalTunnel para o Backend (porta 8081)...
start "Backend LocalTunnel" cmd /k "lt --port 8081"

echo.
echo ====================================
echo  LocalTunnels iniciados!
echo ====================================
echo.
echo URLs de acesso serão geradas aleatoriamente:
echo - Frontend: https://[random].loca.lt
echo - Backend:  https://[random].loca.lt
echo.
echo IMPORTANTE: Anote as URLs exatas que aparecerem
echo nas janelas do LocalTunnel para configurar corretamente.
echo.
pause