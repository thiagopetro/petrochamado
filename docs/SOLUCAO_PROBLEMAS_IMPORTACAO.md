# Guia de Solução de Problemas - Importação de Chamados

## Problemas Comuns e Soluções

### 1. Erro: "Formato de arquivo inválido"

**Causa:** O arquivo CSV não possui o formato esperado.

**Soluções:**
- Verifique se o arquivo possui exatamente 6 colunas separadas por ponto e vírgula (;)
- **IMPORTANTE:** Se o arquivo tiver colunas extras vazias no final, remova-as
- Confirme se a primeira linha contém os cabeçalhos corretos
- Certifique-se de que o arquivo está codificado em UTF-8

**Exemplo de problema comum:**
```
# INCORRETO (7 colunas - duas vazias no final)
Número;Prioridade;Aberto(a) por;Aberto(a);Atribuído(a) a;Descrição resumida;;

# CORRETO (6 colunas)
Número;Prioridade;Aberto(a) por;Aberto(a);Atribuído(a) a;Descrição resumida
```

### 2. Erro: "Linha deve conter pelo menos 6 campos"

**Causa:** O arquivo CSV não possui o número mínimo de colunas necessárias.

**Solução:**
- Verifique se o arquivo possui exatamente estas 6 colunas obrigatórias:
  1. Número (ex: INC3221310)
  2. Prioridade (ex: 3 - Moderada)
  3. Aberto(a) por (nome da pessoa)
  4. Aberto(a) (data de abertura - será ignorada)
  5. Atribuído(a) a (nome do responsável - pode estar vazio)
  6. Descrição resumida (descrição do problema)

### 3. Formato do Arquivo CSV

**Formato Correto:**
```
Número;Prioridade;Aberto(a) por;Aberto(a);Atribuído(a) a;Descrição resumida;;
INC3221310;3 - Moderada;João Silva;29/07/2025 11:42:05;Maria Santos;Erro ao emitir PT na aplicação;;
```

**Pontos Importantes:**
- Use ponto e vírgula (`;`) como separador
- A primeira linha deve conter os cabeçalhos
- Campos vazios são permitidos, mas as colunas devem existir
- As duas últimas colunas podem estar vazias

### 4. Valores Válidos para Prioridade

**Formatos Aceitos:**
- `1 - Crítica` ou `Crítica` ou `Critical`
- `2 - Alta` ou `Alta` ou `High`
- `3 - Moderada` ou `Moderada` ou `Medium`
- `4 - Baixa` ou `Baixa` ou `Low`

### 5. Campos Obrigatórios vs Opcionais

**Obrigatórios (não podem estar vazios):**
- Número do chamado
- Aberto(a) por
- Descrição resumida

**Opcionais (podem estar vazios):**
- Atribuído(a) a (será definido como "Não atribuído")
- Título (será gerado automaticamente baseado na descrição)
- Status (será definido como "Aberto")

### 6. Como Testar a Importação

1. **Teste com arquivo pequeno:**
   - Crie um arquivo com apenas 2-3 linhas de dados
   - Verifique se a importação funciona

2. **Verifique o formato:**
   - Abra o arquivo em um editor de texto
   - Confirme que usa ponto e vírgula como separador
   - Conte o número de colunas em cada linha

3. **Use a funcionalidade de tabela:**
   - Na página de importação, use a aba "Inserir Dados Manualmente"
   - Cole os dados diretamente na tabela
   - Teste com poucos registros primeiro

### 7. Mensagens de Erro Comuns

| Erro | Causa | Solução |
|------|-------|----------|
| "Arquivo não pode estar vazio" | Arquivo sem conteúdo | Verifique se o arquivo tem dados |
| "Formato de arquivo não suportado" | Extensão incorreta | Use .csv, .xlsx ou .txt |
| "Número do chamado não pode estar vazio" | Campo obrigatório vazio | Preencha o número do chamado |
| "Campo 'Aberto(a) por' não pode estar vazio" | Campo obrigatório vazio | Preencha quem abriu o chamado |
| "Descrição resumida não pode estar vazia" | Campo obrigatório vazio | Preencha a descrição |

### 8. Exemplo de Arquivo Válido

Crie um arquivo `teste.csv` com este conteúdo:

```csv
Número;Prioridade;Aberto(a) por;Aberto(a);Atribuído(a) a;Descrição resumida;;
TEST001;3 - Moderada;João Silva;01/01/2025 10:00:00;Maria Santos;Teste de importação;;
TEST002;2 - Alta;Pedro Costa;01/01/2025 11:00:00;;Segundo teste de importação;;
```

### 9. Verificação Pós-Importação

Após a importação:
1. Verifique a mensagem de sucesso
2. Acesse a lista de chamados
3. Confirme se os dados foram importados corretamente
4. Verifique se há duplicatas ou erros reportados

### 10. Como Corrigir Arquivo com Colunas Extras

Se seu arquivo CSV tiver colunas extras vazias no final:

**No Windows (PowerShell):**
```powershell
Get-Content 'seu_arquivo.csv' | ForEach-Object { ($_ -split ';')[0..5] -join ';' } | Set-Content 'arquivo_corrigido.csv'
```

**No Excel:**
1. Abra o arquivo CSV
2. Selecione e delete as colunas extras (G, H, etc.)
3. Salve como CSV (separado por ponto e vírgula)

**Verificação:**
- Conte o número de pontos e vírgulas em cada linha
- Deve haver exatamente 5 pontos e vírgulas (para 6 colunas)

### 11. Contato para Suporte

Se o problema persistir:
1. Anote a mensagem de erro exata
2. Verifique os logs do servidor
3. Teste com o arquivo de exemplo fornecido
4. Documente os passos que levaram ao erro