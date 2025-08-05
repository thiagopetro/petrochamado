"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { useParams, useRouter } from "next/navigation"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { PageHeader } from "@/components/page-header"
import { Save, ArrowLeft } from "lucide-react"
import { useToast } from "@/hooks/use-toast"
import Link from "next/link"

interface User {
  id: number
  nome: string
  login: string
  status: string
  criadoEm: string
  atualizadoEm: string
}

export default function EditUserPage() {
  const params = useParams()
  const router = useRouter()
  const { toast } = useToast()
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [formData, setFormData] = useState({
    nome: "",
    login: "",
    senha: "",
    confirmarSenha: "",
    status: "ATIVO"
  })

  const userId = params.id as string

  useEffect(() => {
    if (userId) {
      fetchUser()
    }
  }, [userId])

  const fetchUser = async () => {
    try {
      setLoading(true)
      const response = await fetch(`http://localhost:8081/api/users/${userId}`)
      if (response.ok) {
        const userData = await response.json()
        setUser(userData)
        setFormData({
          nome: userData.nome,
          login: userData.login,
          senha: "",
          confirmarSenha: "",
          status: userData.status
        })
      } else {
        toast({
          title: "Erro",
          description: "Usuário não encontrado",
          variant: "destructive",
        })
        router.push('/users')
      }
    } catch (error) {
      console.error('Erro ao buscar usuário:', error)
      toast({
        title: "Erro",
        description: "Erro ao conectar com o servidor",
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }

  const handleInputChange = (field: string, value: string) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    // Validações
    if (!formData.nome.trim()) {
      toast({
        title: "Erro",
        description: "Nome é obrigatório",
        variant: "destructive",
      })
      return
    }

    if (!formData.login.trim()) {
      toast({
        title: "Erro",
        description: "Login é obrigatório",
        variant: "destructive",
      })
      return
    }

    // Se senha foi preenchida, validar
    if (formData.senha.trim()) {
      if (formData.senha !== formData.confirmarSenha) {
        toast({
          title: "Erro",
          description: "Senhas não coincidem",
          variant: "destructive",
        })
        return
      }

      if (formData.senha.length < 6) {
        toast({
          title: "Erro",
          description: "Senha deve ter pelo menos 6 caracteres",
          variant: "destructive",
        })
        return
      }
    }

    try {
      setSaving(true)
      
      const userData: any = {
        nome: formData.nome.trim(),
        login: formData.login.trim(),
        status: formData.status
      }

      // Só incluir senha se foi preenchida
      if (formData.senha.trim()) {
        userData.senha = formData.senha
      }

      const response = await fetch(`http://localhost:8081/api/users/${userId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      })

      if (response.ok) {
        toast({
          title: "Sucesso",
          description: "Usuário atualizado com sucesso",
        })
        router.push('/users')
      } else {
        const errorData = await response.text()
        toast({
          title: "Erro",
          description: errorData || "Erro ao atualizar usuário",
          variant: "destructive",
        })
      }
    } catch (error) {
      console.error('Erro ao atualizar usuário:', error)
      toast({
        title: "Erro",
        description: "Erro ao conectar com o servidor",
        variant: "destructive",
      })
    } finally {
      setSaving(false)
    }
  }

  if (loading) {
    return (
      <div className="flex flex-col h-screen">
        <PageHeader
          title="Editar Usuário"
          breadcrumbs={[
            { label: "Dashboard", href: "/" },
            { label: "Usuários", href: "/users" },
            { label: "Editar" },
          ]}
        />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900 mx-auto mb-4"></div>
            <p>Carregando usuário...</p>
          </div>
        </div>
      </div>
    )
  }

  if (!user) {
    return (
      <div className="flex flex-col h-screen">
        <PageHeader
          title="Usuário não encontrado"
          breadcrumbs={[
            { label: "Dashboard", href: "/" },
            { label: "Usuários", href: "/users" },
            { label: "Não encontrado" },
          ]}
        />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <h2 className="text-2xl font-bold mb-2">Usuário não encontrado</h2>
            <p className="text-muted-foreground mb-4">O usuário {userId} não existe ou foi removido.</p>
            <Button asChild>
              <Link href="/users">Voltar para lista</Link>
            </Button>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="flex flex-col h-screen">
      <PageHeader
        title={`Editar Usuário: ${user.nome}`}
        breadcrumbs={[
          { label: "Dashboard", href: "/" },
          { label: "Usuários", href: "/users" },
          { label: "Editar" },
        ]}
      >
        <Button variant="outline" asChild>
          <Link href="/users">
            <ArrowLeft className="h-4 w-4 mr-2" />
            Voltar
          </Link>
        </Button>
      </PageHeader>

      <div className="flex-1 overflow-auto p-6">
        <div className="max-w-2xl mx-auto">
          <Card>
            <CardHeader>
              <CardTitle>Informações do Usuário</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="grid gap-4 md:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="nome">Nome *</Label>
                    <Input
                      id="nome"
                      value={formData.nome}
                      onChange={(e) => handleInputChange("nome", e.target.value)}
                      placeholder="Nome completo do usuário"
                      required
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="login">Login *</Label>
                    <Input
                      id="login"
                      value={formData.login}
                      onChange={(e) => handleInputChange("login", e.target.value)}
                      placeholder="Nome de usuário para login"
                      required
                    />
                  </div>
                </div>

                <div className="grid gap-4 md:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="senha">Nova Senha (opcional)</Label>
                    <Input
                      id="senha"
                      type="password"
                      value={formData.senha}
                      onChange={(e) => handleInputChange("senha", e.target.value)}
                      placeholder="Deixe em branco para manter a atual"
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="confirmarSenha">Confirmar Nova Senha</Label>
                    <Input
                      id="confirmarSenha"
                      type="password"
                      value={formData.confirmarSenha}
                      onChange={(e) => handleInputChange("confirmarSenha", e.target.value)}
                      placeholder="Confirme a nova senha"
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="status">Status</Label>
                  <Select value={formData.status} onValueChange={(value) => handleInputChange("status", value)}>
                    <SelectTrigger>
                      <SelectValue placeholder="Selecione o status" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="ATIVO">Ativo</SelectItem>
                      <SelectItem value="INATIVO">Inativo</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="flex gap-4 pt-4">
                  <Button type="submit" disabled={saving}>
                    <Save className="h-4 w-4 mr-2" />
                    {saving ? "Salvando..." : "Salvar Alterações"}
                  </Button>
                  <Button type="button" variant="outline" asChild>
                    <Link href="/users">Cancelar</Link>
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