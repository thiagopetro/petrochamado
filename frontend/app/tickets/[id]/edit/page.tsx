"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { useParams, useRouter } from "next/navigation"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { PageHeader } from "@/components/page-header"
import { technicians, type Ticket } from "@/lib/mock-data"
import { Save, ArrowLeft } from "lucide-react"
import { useToast } from "@/hooks/use-toast"
import Link from "next/link"

export default function EditTicketPage() {
  const params = useParams()
  const router = useRouter()
  const { toast } = useToast()
  const ticketId = params.id as string

  const [ticket, setTicket] = useState<Ticket | null>(null)
  const [loading, setLoading] = useState(true)
  const [isLoading, setIsLoading] = useState(false)

  const [formData, setFormData] = useState({
    titulo: "",
    descricao: "",
    prioridade: "",
    abertopor: "",
    emailAbertoPor: "",
    atribuidoa: "",
    status: "Aguardando usuário",
  })

  useEffect(() => {
    const fetchTicket = async () => {
      try {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'
        const response = await fetch(`${baseUrl}/api/tickets/${ticketId}`)
        if (response.ok) {
          const data = await response.json()
          setTicket(data)
          setFormData({
            titulo: data.titulo || "",
            descricao: data.descricao || "",
            prioridade: data.prioridade || "",
            abertopor: data.abertopor || "",
            emailAbertoPor: data.emailAbertoPor || "",
            atribuidoa: data.atribuidoa || "",
            status: data.status || "Aguardando usuário",
          })
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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    // Simular salvamento
    await new Promise((resolve) => setTimeout(resolve, 1000))

    toast({
      title: "Chamado atualizado com sucesso!",
      description: `Ticket ${ticket.id} foi atualizado.`,
    })

    setIsLoading(false)
    router.push(`/tickets/${ticket.id}`)
  }

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <div className="flex flex-col h-screen">
      <PageHeader
        title={`Editar Chamado ${ticket.id}`}
        breadcrumbs={[
          { label: "Dashboard", href: "/" },
          { label: "Chamados", href: "/tickets" },
          { label: ticket.id, href: `/tickets/${ticket.id}` },
          { label: "Editar" },
        ]}
      >
        <Button variant="outline" asChild>
          <Link href={`/tickets/${ticket.id}`}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            Voltar
          </Link>
        </Button>
      </PageHeader>

      <div className="flex-1 overflow-auto p-6">
        <div className="max-w-2xl mx-auto">
          <Card>
            <CardHeader>
              <CardTitle>Editar Informações do Chamado</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="grid gap-4 md:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="ticket-id">Número do Ticket</Label>
                    <Input id="ticket-id" value={ticket.id} disabled className="bg-muted" />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="status">Status</Label>
                    <Select value={formData.status} onValueChange={(value) => handleInputChange("status", value)}>
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="Aguardando usuário">Aguardando usuário</SelectItem>
                        <SelectItem value="Em atendimento">Em atendimento</SelectItem>
                        <SelectItem value="Problema confirmado">Problema confirmado</SelectItem>
                        <SelectItem value="Resolvido">Resolvido</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="titulo">Título/Descrição Resumida *</Label>
                  <Input
                    id="titulo"
                    value={formData.titulo}
                    onChange={(e) => handleInputChange("titulo", e.target.value)}
                    required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="descricao">Descrição Detalhada *</Label>
                  <Textarea
                    id="descricao"
                    value={formData.descricao}
                    onChange={(e) => handleInputChange("descricao", e.target.value)}
                    rows={4}
                    required
                  />
                </div>

                <div className="grid gap-4 md:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="prioridade">Prioridade *</Label>
                    <Select
                      value={formData.prioridade}
                      onValueChange={(value) => handleInputChange("prioridade", value)}
                      required
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="1 - Crítica">
                          <div className="flex items-center gap-2">
                            <div className="w-2 h-2 bg-red-500 rounded-full" />1 - Crítica
                          </div>
                        </SelectItem>
                        <SelectItem value="2 - Alta">
                          <div className="flex items-center gap-2">
                            <div className="w-2 h-2 bg-orange-500 rounded-full" />2 - Alta
                          </div>
                        </SelectItem>
                        <SelectItem value="3 - Moderada">
                          <div className="flex items-center gap-2">
                            <div className="w-2 h-2 bg-yellow-500 rounded-full" />3 - Moderada
                          </div>
                        </SelectItem>
                        <SelectItem value="4 - Baixa">
                          <div className="flex items-center gap-2">
                            <div className="w-2 h-2 bg-green-500 rounded-full" />4 - Baixa
                          </div>
                        </SelectItem>
                      </SelectContent>
                    </Select>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="atribuidoa">Atribuído a *</Label>
                    <Select
                      value={formData.atribuidoa}
                      onValueChange={(value) => handleInputChange("atribuidoa", value)}
                      required
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        {technicians.map((tech) => (
                          <SelectItem key={tech} value={tech}>
                            {tech}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                <div className="grid gap-4 md:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="abertopor">Aberto por *</Label>
                    <Input
                      id="abertopor"
                      value={formData.abertopor}
                      onChange={(e) => handleInputChange("abertopor", e.target.value)}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="emailAbertoPor">Email de quem abriu *</Label>
                    <Input
                      id="emailAbertoPor"
                      type="email"
                      value={formData.emailAbertoPor}
                      onChange={(e) => handleInputChange("emailAbertoPor", e.target.value)}
                      required
                    />
                  </div>
                </div>

                <div className="flex gap-4 pt-4">
                  <Button type="submit" disabled={isLoading} className="flex-1">
                    {isLoading ? (
                      <>Salvando...</>
                    ) : (
                      <>
                        <Save className="h-4 w-4 mr-2" />
                        Salvar Alterações
                      </>
                    )}
                  </Button>
                  <Button type="button" variant="outline" asChild>
                    <Link href={`/tickets/${ticket.id}`}>Cancelar</Link>
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
