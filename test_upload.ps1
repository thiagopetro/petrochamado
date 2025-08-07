# Script para testar upload do arquivo CSV
$uri = "http://localhost:8081/api/tickets/import"
$filePath = "C:\Users\thiag\Documents\lovablepetro\chamadopetro\chamados_corrigido.csv"

# Criar boundary para multipart
$boundary = [System.Guid]::NewGuid().ToString()
$LF = "`r`n"

# Ler o arquivo
$fileBytes = [System.IO.File]::ReadAllBytes($filePath)
$fileName = [System.IO.Path]::GetFileName($filePath)

# Criar o corpo da requisição multipart
$bodyLines = (
    "--$boundary",
    "Content-Disposition: form-data; name=`"file`"; filename=`"$fileName`"",
    "Content-Type: text/csv",
    "",
    [System.Text.Encoding]::UTF8.GetString($fileBytes),
    "--$boundary--"
) -join $LF

# Fazer a requisição
try {
    $response = Invoke-RestMethod -Uri $uri -Method Post -Body $bodyLines -ContentType "multipart/form-data; boundary=$boundary"
    Write-Host "Sucesso!" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "Erro:" -ForegroundColor Red
    Write-Host $_.Exception.Message
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
    }
}