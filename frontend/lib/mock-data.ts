export interface Ticket {
  id: string
  titulo: string
  descricao: string
  prioridade: "1 - Crítica" | "2 - Alta" | "3 - Moderada" | "4 - Baixa"
  abertoem: string
  abertopor: string
  emailAbertoPor: string
  atribuidoa: string
  atualizado: string
  status: "Aguardando usuário" | "Em atendimento" | "Problema confirmado" | "Resolvido"
}

// Dados mocados removidos - aplicação agora usa dados reais da API
// export const mockTickets: Ticket[] = []

export const technicians = ["Thiago de Amorim Moraes", "Carlos Eduardo Lima", "Ana Carolina Ferreira"]

export const priorityColors = {
  "1 - Crítica": "bg-red-100 text-red-800 border-red-200",
  "2 - Alta": "bg-orange-100 text-orange-800 border-orange-200",
  "3 - Moderada": "bg-yellow-100 text-yellow-800 border-yellow-200",
  "4 - Baixa": "bg-green-100 text-green-800 border-green-200",
}

export const statusColors = {
  "Aguardando usuário": "bg-blue-100 text-blue-800 border-blue-200",
  "Em atendimento": "bg-purple-100 text-purple-800 border-purple-200",
  "Problema confirmado": "bg-orange-100 text-orange-800 border-orange-200",
  Resolvido: "bg-green-100 text-green-800 border-green-200",
}
