@echo off
echo ====================================
echo  Chamado Petro - Deploy Local
echo ====================================
echo.

echo 1. Iniciando Backend (Spring Boot) na porta 8081...
start "Backend" cmd /k "cd backend && mvn spring-boot:run"

echo 2. Aguardando 10 segundos para o backend inicializar...
timeout /t 10 /nobreak > nul

echo 3. Iniciando ngrok para o Backend...
start "Backend ngrok" cmd /k "ngrok http 8081"

echo 4. Aguardando 5 segundos para o ngrok do backend...
timeout /t 5 /nobreak > nul

echo 5. Iniciando Frontend (Next.js) na porta 3000...
start "Frontend" cmd /k "cd frontend && npm run dev"

echo 6. Aguardando 10 segundos para o frontend inicializar...
timeout /t 10 /nobreak > nul

echo 7. Iniciando ngrok para o Frontend...
start "Frontend ngrok" cmd /k "ngrok http 3000"

echo.
echo ====================================
echo  Todos os serviços foram iniciados!
echo ====================================
echo.
echo PRÓXIMOS PASSOS:
echo 1. Anote as URLs geradas pelo ngrok
echo 2. Atualize o arquivo .env.local com as URLs
echo 3. Reinicie o frontend para aplicar as mudanças
echo.
echo URLs do ngrok:
echo - Backend: https://xxxxx.ngrok-free.app
echo - Frontend: https://yyyyy.ngrok-free.app
echo.
pause