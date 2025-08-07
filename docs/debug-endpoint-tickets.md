# Depuração do Endpoint /api/tickets

## Problema Identificado

O endpoint `/api/tickets` estava retornando erro 400 (Bad Request) com a mensagem "Status não encontrado: 1 - Aberto".

## Causa Raiz

O problema estava no arquivo de teste `test-ticket.json` que continha um valor de status inválido:
- **Status enviado**: `"1 - Aberto"`
- **Status válidos no enum**: `"Aguardando usuário"`, `"Em atendimento"`, `"Problema confirmado"`, `"Resolvido"`

## Solução Implementada

1. **Identificação do problema**: Utilizamos logs de debug temporários no `TicketController` e `TicketService` para rastrear o fluxo da requisição.

2. **Criação de interceptor**: Implementamos um `RequestLoggingInterceptor` temporário para capturar detalhes das requisições HTTP.

3. **Análise do enum Status**: Verificamos que o enum `Status.java` não possuía o valor `"1 - Aberto"` enviado no teste.

4. **Correção do arquivo de teste**: Alteramos o status no `test-ticket.json` de `"1 - Aberto"` para `"Aguardando usuário"`.

5. **Limpeza**: Removemos todos os logs de debug e arquivos temporários criados para depuração.

## Arquivos Modificados

- `test-ticket.json`: Corrigido o valor do status
- `TicketController.java`: Logs de debug adicionados e removidos
- `TicketService.java`: Logs de debug adicionados e removidos
- `RequestLoggingInterceptor.java`: Criado e removido (temporário)
- `WebConfig.java`: Criado e removido (temporário)

## Resultado

O endpoint `/api/tickets` agora funciona corretamente:
- Aceita requisições POST com dados válidos
- Retorna status 201 (Created) em caso de sucesso
- Retorna os dados do ticket criado no corpo da resposta

## Testes Realizados

1. **Teste com status inválido**: Falhou conforme esperado
2. **Teste com status válido**: Sucesso (tickets criados com IDs 1 e 2)

## Lições Aprendidas

- Sempre validar os dados de teste contra os enums/constraints da aplicação
- Utilizar logs de debug temporários para rastrear problemas de validação
- O método `Status.fromDescricao()` lança `IllegalArgumentException` para valores não encontrados
- A configuração de segurança permite todas as requisições (`.anyRequest().permitAll()`)