"use client"
import { useParams } from "next/navigation"
import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { PageHeader } from "@/components/page-header"
import { priorityColors, statusColors, type Ticket } from "@/lib/mock-data"
import { ArrowLeft, Edit, Clock, User, Calendar, FileText } from "lucide-react"
import Link from "next/link"

export default function TicketDetailsPage() {
  const params = useParams()
  const ticketId = params.id as string
  const [ticket, setTicket] = useState<Ticket | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchTicket = async () => {
      try {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'
        const response = await fetch(`${baseUrl}/api/tickets/${ticketId}`)
        if (response.ok) {
          const data = await response.json()
          setTicket(data)
        }
      } catch (error) {
        console.error('Erro ao buscar ticket:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchTicket()
  }, [ticketId])

  if (loading) {
    return (
      <div className="flex flex-col h-screen">
        <PageHeader
          title="Carregando..."
          breadcrumbs={[
            { label: "Dashboard", href: "/" },
            { label: "Chamados", href: "/tickets" },
            { label: "Carregando..." },
          ]}
        />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p>Carregando chamado...</p>
          </div>
        </div>
      </div>
    )
  }

  if (!ticket) {
    return (
      <div className="flex flex-col h-screen">
        <PageHeader
          title="Chamado não encontrado"
          breadcrumbs={[
            { label: "Dashboard", href: "/" },
            { label: "Chamados", href: "/tickets" },
            { label: "Não encontrado" },
          ]}
        />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <h2 className="text-2xl font-bold mb-2">Chamado não encontrado</h2>
            <p className="text-muted-foreground mb-4">O chamado {ticketId} não existe ou foi removido.</p>
            <Button asChild>
              <Link href="/tickets">Voltar para lista</Link>
            </Button>
          </div>
        </div>
      </div>
    )
  }

  // Simular histórico de alterações
  const history = [
    {
      date: ticket.abertoem,
      action: "Chamado criado",
      user: ticket.abertopor,
      details: `Chamado criado com prioridade ${ticket.prioridade}`,
    },
    {
      date: ticket.atualizado,
      action: "Status alterado",
      user: ticket.atribuidoa,
      details: `Status alterado para "${ticket.status}"`,
    },
  ]

  return (
    <div className="flex flex-col h-screen">
      <PageHeader
        title={`Chamado ${ticket.id}`}
        breadcrumbs={[{ label: "Dashboard", href: "/" }, { label: "Chamados", href: "/tickets" }, { label: ticket.id }]}
      >
        <div className="flex gap-2">
          <Button variant="outline" asChild>
            <Link href="/tickets">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Voltar
            </Link>
          </Button>
          <Button asChild>
            <Link href={`/tickets/${ticket.id}/edit`}>
              <Edit className="h-4 w-4 mr-2" />
              Editar
            </Link>
          </Button>
        </div>
      </PageHeader>

      <div className="flex-1 overflow-auto p-6">
        <div className="max-w-4xl mx-auto space-y-6">
          {/* Informações Principais */}
          <Card>
            <CardHeader>
              <div className="flex items-start justify-between">
                <div>
                  <CardTitle className="text-xl">{ticket.titulo}</CardTitle>
                  <div className="flex items-center gap-2 mt-2">
                    <Badge variant="outline" className={priorityColors[ticket.prioridade]}>
                      {ticket.prioridade}
                    </Badge>
                    <Badge variant="outline" className={statusColors[ticket.status]}>
                      {ticket.status}
                    </Badge>
                  </div>
                </div>
                <div className="text-right text-sm text-muted-foreground">
                  <p>Ticket #{ticket.id}</p>
                  <p>Criado em {new Date(ticket.abertoem).toLocaleDateString("pt-BR")}</p>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <h4 className="font-medium mb-2 flex items-center gap-2">
                  <FileText className="h-4 w-4" />
                  Descrição
                </h4>
                <p className="text-muted-foreground leading-relaxed">{ticket.descricao}</p>
              </div>

              <Separator />

              <div className="grid gap-4 md:grid-cols-2">
                <div className="space-y-3">
                  <div className="flex items-center gap-2">
                    <User className="h-4 w-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm font-medium">Solicitante</p>
                      <p className="text-sm text-muted-foreground">{ticket.abertopor}</p>
                    </div>
                  </div>

                  <div className="flex items-center gap-2">
                    <User className="h-4 w-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm font-medium">Responsável</p>
                      <p className="text-sm text-muted-foreground">{ticket.atribuidoa}</p>
                    </div>
                  </div>
                </div>

                <div className="space-y-3">
                  <div className="flex items-center gap-2">
                    <Calendar className="h-4 w-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm font-medium">Data de Abertura</p>
                      <p className="text-sm text-muted-foreground">
                        {new Date(ticket.abertoem).toLocaleString("pt-BR")}
                      </p>
                    </div>
                  </div>

                  <div className="flex items-center gap-2">
                    <Clock className="h-4 w-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm font-medium">Última Atualização</p>
                      <p className="text-sm text-muted-foreground">
                        {new Date(ticket.atualizado).toLocaleString("pt-BR")}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Histórico */}
          <Card>
            <CardHeader>
              <CardTitle className="text-lg">Histórico de Alterações</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {history.map((entry, index) => (
                  <div key={index} className="flex gap-4">
                    <div className="flex flex-col items-center">
                      <div className="w-2 h-2 bg-primary rounded-full" />
                      {index < history.length - 1 && <div className="w-px h-8 bg-border mt-2" />}
                    </div>
                    <div className="flex-1 pb-4">
                      <div className="flex items-center justify-between mb-1">
                        <p className="font-medium text-sm">{entry.action}</p>
                        <p className="text-xs text-muted-foreground">{new Date(entry.date).toLocaleString("pt-BR")}</p>
                      </div>
                      <p className="text-sm text-muted-foreground">{entry.details}</p>
                      <p className="text-xs text-muted-foreground mt-1">por {entry.user}</p>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
