"use client"

import { useState, useEffect } from "react"
import { useToast } from "@/hooks/use-toast"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { PageHeader } from "@/components/page-header"
import { priorityColors, statusColors, technicians, type Ticket } from "@/lib/mock-data"
import { Search, FileText, Download, Calendar } from "lucide-react"
import Link from "next/link"

export default function ReportsPage() {
  const { toast } = useToast()
  const [tickets, setTickets] = useState<Ticket[]>([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState("")
  const [priorityFilter, setPriorityFilter] = useState("all")
  const [technicianFilter, setTechnicianFilter] = useState("all")
  const [dateFilter, setDateFilter] = useState("all")
  const [currentPage, setCurrentPage] = useState(1)
  const itemsPerPage = 25

  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'
        const response = await fetch(`${baseUrl}/api/tickets`)
        if (response.ok) {
          const data = await response.json()
          setTickets(data)
        }
      } catch (error) {
        console.error('Erro ao buscar tickets:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchTickets()
  }, [])

  // Filtrar apenas tickets resolvidos
  const resolvedTickets = tickets.filter((ticket) => ticket.status === "Resolvido")

  // Aplicar filtros adicionais
  const filteredTickets = resolvedTickets.filter((ticket) => {
    const matchesSearch =
      ticket.titulo.toLowerCase().includes(searchTerm.toLowerCase()) ||
      ticket.ticketId.toLowerCase().includes(searchTerm.toLowerCase()) ||
      ticket.abertoPor.toLowerCase().includes(searchTerm.toLowerCase())

    const matchesPriority = priorityFilter === "all" || ticket.prioridade === priorityFilter
    const matchesTechnician = technicianFilter === "all" || ticket.atribuidoA === technicianFilter

    let matchesDate = true
    if (dateFilter !== "all") {
      const ticketDate = new Date(ticket.abertoEm)
      const now = new Date()
      
      switch (dateFilter) {
        case "week":
          const weekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
          matchesDate = ticketDate >= weekAgo
          break
        case "month":
          const monthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
          matchesDate = ticketDate >= monthAgo
          break
        case "quarter":
          const quarterAgo = new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000)
          matchesDate = ticketDate >= quarterAgo
          break
      }
    }

    return matchesSearch && matchesPriority && matchesTechnician && matchesDate
  })

  // Paginação
  const totalPages = Math.ceil(filteredTickets.length / itemsPerPage)
  const startIndex = (currentPage - 1) * itemsPerPage
  const paginatedTickets = filteredTickets.slice(startIndex, startIndex + itemsPerPage)

  const clearFilters = () => {
    setSearchTerm("")
    setPriorityFilter("all")
    setTechnicianFilter("all")
    setDateFilter("all")
    setCurrentPage(1)
  }

  const exportToCSV = () => {
    const csvContent = [
      ["Código", "Título", "Prioridade", "Responsável", "Aberto Por", "Data Abertura", "Data Resolução"],
      ...filteredTickets.map(ticket => [
        ticket.ticketId,
        ticket.titulo,
        ticket.prioridade,
        ticket.atribuidoA,
        ticket.abertoPor,
        new Date(ticket.abertoEm).toLocaleDateString("pt-BR"),
        ticket.status === "Resolvido" ? new Date(ticket.atualizado).toLocaleDateString("pt-BR") : "-"
      ])
    ].map(row => row.join(",")).join("\n")

    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" })
    const link = document.createElement("a")
    const url = URL.createObjectURL(blob)
    link.setAttribute("href", url)
    link.setAttribute("download", `relatorio-chamados-resolvidos-${new Date().toISOString().split('T')[0]}.csv`)
    link.style.visibility = "hidden"
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)

    toast({
      title: "Relatório exportado",
      description: "O arquivo CSV foi baixado com sucesso.",
    })
  }

  if (loading) {
    return (
      <div className="flex flex-col h-screen">
        <PageHeader title="Relatórios" breadcrumbs={[{ label: "Dashboard", href: "/" }, { label: "Relatórios" }]} />
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
      <PageHeader title="Relatórios" breadcrumbs={[{ label: "Dashboard", href: "/" }, { label: "Relatórios" }]}>
        <Button onClick={exportToCSV} disabled={filteredTickets.length === 0}>
          <Download className="h-4 w-4 mr-2" />
          Exportar CSV
        </Button>
      </PageHeader>

      <div className="flex-1 overflow-auto p-6">
        {/* Métricas */}
        <div className="grid gap-4 md:grid-cols-3 mb-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Total Resolvidos</CardTitle>
              <FileText className="h-4 w-4 text-green-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">{resolvedTickets.length}</div>
              <p className="text-xs text-muted-foreground">Chamados resolvidos</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Filtrados</CardTitle>
              <Search className="h-4 w-4 text-blue-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-blue-600">{filteredTickets.length}</div>
              <p className="text-xs text-muted-foreground">Resultados da busca</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Taxa de Resolução</CardTitle>
              <Calendar className="h-4 w-4 text-purple-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-purple-600">
                {tickets.length > 0 ? Math.round((resolvedTickets.length / tickets.length) * 100) : 0}%
              </div>
              <p className="text-xs text-muted-foreground">Do total de chamados</p>
            </CardContent>
          </Card>
        </div>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center justify-between">
              <span>Chamados Resolvidos</span>
              <span className="text-sm font-normal text-muted-foreground">
                {filteredTickets.length} de {resolvedTickets.length} chamados
              </span>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {/* Filtros */}
            <div className="flex flex-wrap gap-4 items-center">
              <div className="flex-1 min-w-64">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder="Buscar por título, código ou solicitante..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>

              <Select value={priorityFilter} onValueChange={setPriorityFilter}>
                <SelectTrigger className="w-40">
                  <SelectValue placeholder="Prioridade" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todas</SelectItem>
                  <SelectItem value="Baixa">Baixa</SelectItem>
                  <SelectItem value="Média">Média</SelectItem>
                  <SelectItem value="Alta">Alta</SelectItem>
                  <SelectItem value="Crítica">Crítica</SelectItem>
                </SelectContent>
              </Select>

              <Select value={technicianFilter} onValueChange={setTechnicianFilter}>
                <SelectTrigger className="w-48">
                  <SelectValue placeholder="Responsável" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todos</SelectItem>
                  {technicians.map((tech) => (
                    <SelectItem key={tech} value={tech}>
                      {tech}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <Select value={dateFilter} onValueChange={setDateFilter}>
                <SelectTrigger className="w-40">
                  <SelectValue placeholder="Período" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todos</SelectItem>
                  <SelectItem value="week">Última semana</SelectItem>
                  <SelectItem value="month">Último mês</SelectItem>
                  <SelectItem value="quarter">Último trimestre</SelectItem>
                </SelectContent>
              </Select>

              <Button variant="outline" onClick={clearFilters}>
                Limpar Filtros
              </Button>
            </div>

            {/* Tabela */}
            <div className="border rounded-lg">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Código</TableHead>
                    <TableHead>Título</TableHead>
                    <TableHead>Prioridade</TableHead>
                    <TableHead>Responsável</TableHead>
                    <TableHead>Aberto Por</TableHead>
                    <TableHead>Data Abertura</TableHead>
                    <TableHead>Ações</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {paginatedTickets.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={7} className="text-center py-8 text-muted-foreground">
                        Nenhum chamado resolvido encontrado
                      </TableCell>
                    </TableRow>
                  ) : (
                    paginatedTickets.map((ticket) => (
                      <TableRow key={ticket.id}>
                        <TableCell className="font-medium">{ticket.ticketId}</TableCell>
                        <TableCell className="max-w-xs truncate">{ticket.titulo}</TableCell>
                        <TableCell>
                          <Badge variant="outline" className={priorityColors[ticket.prioridade]}>
                            {ticket.prioridade}
                          </Badge>
                        </TableCell>
                        <TableCell>{ticket.atribuidoA}</TableCell>
                        <TableCell>{ticket.abertoPor}</TableCell>
                        <TableCell>{new Date(ticket.abertoEm).toLocaleDateString("pt-BR")}</TableCell>
                        <TableCell>
                          <Button variant="ghost" size="sm" asChild>
                            <Link href={`/tickets/${ticket.id}`}>
                              <FileText className="h-4 w-4" />
                            </Link>
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            </div>

            {/* Paginação */}
            {totalPages > 1 && (
              <div className="flex items-center justify-between">
                <p className="text-sm text-muted-foreground">
                  Mostrando {startIndex + 1} a {Math.min(startIndex + itemsPerPage, filteredTickets.length)} de {filteredTickets.length} resultados
                </p>
                <div className="flex items-center space-x-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setCurrentPage(currentPage - 1)}
                    disabled={currentPage === 1}
                  >
                    Anterior
                  </Button>
                  <span className="text-sm">
                    Página {currentPage} de {totalPages}
                  </span>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setCurrentPage(currentPage + 1)}
                    disabled={currentPage === totalPages}
                  >
                    Próxima
                  </Button>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}