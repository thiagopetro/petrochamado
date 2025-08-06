"use client"

import type React from "react"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { PageHeader } from "@/components/page-header"
import { technicians } from "@/lib/mock-data"
import { Save, ArrowLeft } from "lucide-react"
import { useToast } from "@/hooks/use-toast"
import Link from "next/link"

export default function NewTicketPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [isLoading, setIsLoading] = useState(false)

  const [formData, setFormData] = useState({
    titulo: "",
    descricao: "",
    prioridade: "",
    abertoPor: "",
    emailAbertoPor: "",
    atribuidoA: "",
    status: "Aguardando usuário",
  })



  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    try {
      const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'
      
      const ticketData = {
        titulo: formData.titulo,
        descricao: formData.descricao,
        prioridade: formData.prioridade,
        abertoPor: formData.abertoPor,
        emailAbertoPor: formData.emailAbertoPor,
        atribuidoA: formData.atribuidoA,
        status: formData.status
      }

      const response = await fetch(`${baseUrl}/api/tickets`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(ticketData)
      })

      if (response.ok) {
        const createdTicket = await response.json()
        toast({
          title: "Chamado criado com sucesso!",
          description: `Ticket ${createdTicket.ticketId} foi criado e atribuído a ${formData.atribuidoA}.`,
        })
        router.push("/tickets")
      } else {
        const errorData = await response.text()
        toast({
          title: "Erro ao criar chamado",
          description: "Não foi possível criar o chamado. Tente novamente.",
          variant: "destructive"
        })
        console.error('Erro ao criar ticket:', errorData)
      }
    } catch (error) {
      console.error('Erro ao criar ticket:', error)
      toast({
        title: "Erro de conexão",
        description: "Não foi possível conectar ao servidor. Verifique sua conexão.",
        variant: "destructive"
      })
    } finally {
      setIsLoading(false)
    }
  }

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <div className="flex flex-col h-screen">
      <PageHeader
        title="Novo Chamado"
        breadcrumbs={[
          { label: "Dashboard", href: "/" },
          { label: "Chamados", href: "/tickets" },
          { label: "Novo Chamado" },
        ]}
      >
        <Button variant="outline" asChild>
          <Link href="/tickets">
            <ArrowLeft className="h-4 w-4 mr-2" />
            Voltar
          </Link>
        </Button>
      </PageHeader>

      <div className="flex-1 overflow-auto p-6">
        <div className="max-w-2xl mx-auto">
          <Card>
            <CardHeader>
              <CardTitle>Informações do Chamado</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
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
                    </SelectContent>
                  </Select>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="titulo">Título/Descrição Resumida *</Label>
                  <Input
                    id="titulo"
                    placeholder="Ex: Erro ao emitir PT na aplicação"
                    value={formData.titulo}
                    onChange={(e) => handleInputChange("titulo", e.target.value)}
                    required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="descricao">Descrição Detalhada *</Label>
                  <Textarea
                    id="descricao"
                    placeholder="Descreva o problema em detalhes, incluindo passos para reproduzir, mensagens de erro, etc."
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
                        <SelectValue placeholder="Selecione a prioridade" />
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
                      value={formData.atribuidoA}
                      onValueChange={(value) => handleInputChange("atribuidoA", value)}
                      required
                    >
                      <SelectTrigger>
                        <SelectValue placeholder="Selecione o responsável" />
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
                      placeholder="Nome do solicitante"
                      value={formData.abertoPor}
                      onChange={(e) => handleInputChange("abertoPor", e.target.value)}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="emailAbertoPor">Email de quem abriu *</Label>
                    <Input
                      id="emailAbertoPor"
                      type="email"
                      placeholder="email@exemplo.com"
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
                        Criar Chamado
                      </>
                    )}
                  </Button>
                  <Button type="button" variant="outline" asChild>
                    <Link href="/tickets">Cancelar</Link>
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
