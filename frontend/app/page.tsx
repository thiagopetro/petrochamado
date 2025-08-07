"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { PageHeader } from "@/components/page-header"
import { priorityColors, statusColors, type Ticket } from "@/lib/mock-data"


import { AlertCircle, CheckCircle, Clock, Ticket as TicketIcon, TrendingUp } from "lucide-react"
import Link from "next/link"

export default function Dashboard() {
  const [period, setPeriod] = useState("month")
  const [tickets, setTickets] = useState<Ticket[]>([])
  const [loading, setLoading] = useState(true)


  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'
        const response = await fetch(`${baseUrl}/api/tickets`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        })
        
        if (response.ok) {
          const data = await response.json()
          setTickets(data)
        } else {
          console.error('Erro na resposta:', response.status, response.statusText)
        }
      } catch (error) {
        console.error('Erro ao buscar tickets:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchTickets()
  }, [])

  // Calcular métricas
  const totalTickets = tickets.length
  const openTickets = tickets.filter((t) => t.status !== "Resolvido").length
  const resolvedTickets = tickets.filter((t) => t.status === "Resolvido").length
  const pendingTickets = tickets.filter((t) => t.status === "Aguardando usuário").length

  // 5 chamados mais recentes (excluindo os resolvidos)
  const recentTickets = tickets
    .filter((t) => t.status !== "Resolvido")
    .sort((a, b) => new Date(b.abertoEm).getTime() - new Date(a.abertoEm).getTime())
    .slice(0, 5)

  if (loading) {
    return (
      <div className="flex flex-col h-screen">
        <PageHeader title="Dashboard" breadcrumbs={[{ label: "Dashboard" }]} />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p>Carregando dados...</p>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="flex flex-col h-screen">
      <PageHeader title="Dashboard" breadcrumbs={[{ label: "Dashboard" }]}>
        <Select value={period} onValueChange={setPeriod}>
          <SelectTrigger className="w-40">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="month">Último mês</SelectItem>
            <SelectItem value="quarter">Trimestre</SelectItem>
            <SelectItem value="year">Ano</SelectItem>
          </SelectContent>
        </Select>
      </PageHeader>

      <div className="flex-1 overflow-auto p-6 space-y-6">
        {/* Cards de Métricas */}
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Total de Chamados</CardTitle>
              <TicketIcon className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalTickets}</div>
              <p className="text-xs text-muted-foreground">
                <TrendingUp className="inline h-3 w-3 mr-1" />
                +12% em relação ao mês anterior
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Chamados Abertos</CardTitle>
              <AlertCircle className="h-4 w-4 text-orange-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-orange-600">{openTickets}</div>
              <p className="text-xs text-muted-foreground">
                {Math.round((openTickets / totalTickets) * 100)}% do total
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Resolvidos</CardTitle>
              <CheckCircle className="h-4 w-4 text-green-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">{resolvedTickets}</div>
              <p className="text-xs text-muted-foreground">
                {Math.round((resolvedTickets / totalTickets) * 100)}% do total
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Pendentes</CardTitle>
              <Clock className="h-4 w-4 text-blue-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-blue-600">{pendingTickets}</div>
              <p className="text-xs text-muted-foreground">Aguardando usuário</p>
            </CardContent>
          </Card>
        </div>



        {/* Chamados Recentes */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base">Chamados Mais Recentes</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {recentTickets.map((ticket) => (
                <Link key={ticket.id} href={`/tickets/${ticket.id}`} className="block">
                  <div className="flex items-center justify-between p-3 border rounded-lg hover:bg-muted/50 transition-colors cursor-pointer">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-medium text-sm">{ticket.ticketId}</span>
                        <Badge variant="outline" className={priorityColors[ticket.prioridade]}>
                          {ticket.prioridade}
                        </Badge>
                        <Badge variant="outline" className={statusColors[ticket.status]}>
                          {ticket.status}
                        </Badge>
                      </div>
                      <p className="text-sm text-muted-foreground truncate">{ticket.titulo}</p>
                      <p className="text-xs text-muted-foreground">
                        Aberto por {ticket.abertoPor} • {new Date(ticket.abertoEm).toLocaleDateString("pt-BR")}
                      </p>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
