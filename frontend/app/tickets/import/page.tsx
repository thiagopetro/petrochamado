"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { useToast } from "@/hooks/use-toast"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { PageHeader } from "@/components/page-header"
import { Upload, FileText, Download, AlertCircle, CheckCircle, X } from "lucide-react"
import { Badge } from "@/components/ui/badge"

interface ImportResult {
  success: number
  errors: string[]
  duplicates: string[]
}

export default function ImportTicketsPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [file, setFile] = useState<File | null>(null)
  const [importing, setImporting] = useState(false)
  const [importResult, setImportResult] = useState<ImportResult | null>(null)
  const [preview, setPreview] = useState<any[]>([])

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0]
    if (selectedFile) {
      const validTypes = [
        'text/csv',
        'application/vnd.ms-excel',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'text/plain'
      ]
      
      if (validTypes.includes(selectedFile.type) || 
          selectedFile.name.endsWith('.csv') || 
          selectedFile.name.endsWith('.xlsx') || 
          selectedFile.name.endsWith('.xls') || 
          selectedFile.name.endsWith('.txt')) {
        setFile(selectedFile)
        setImportResult(null)
        parseFilePreview(selectedFile)
      } else {
        toast({
          title: "Arquivo inválido",
          description: "Por favor, selecione um arquivo CSV, XLSX ou TXT.",
          variant: "destructive",
        })
      }
    }
  }

  const parseFilePreview = async (file: File) => {
    try {
      const text = await file.text()
      let lines: string[]
      
      if (file.name.endsWith('.csv') || file.name.endsWith('.txt')) {
        lines = text.split('\n').filter(line => line.trim())
      } else {
        // Para XLSX, vamos simular o parsing (em produção, usaria uma biblioteca como xlsx)
        lines = text.split('\n').filter(line => line.trim())
      }
      
      // Pegar apenas as primeiras 5 linhas para preview
      const previewData = lines.slice(0, 5).map((line, index) => {
        const columns = line.split(',').map(col => col.trim().replace(/"/g, ''))
        return {
          line: index + 1,
          data: columns
        }
      })
      
      setPreview(previewData)
    } catch (error) {
      console.error('Erro ao fazer preview do arquivo:', error)
      toast({
        title: "Erro",
        description: "Não foi possível fazer o preview do arquivo.",
        variant: "destructive",
      })
    }
  }

  const handleImport = async () => {
    if (!file) return

    setImporting(true)
    setImportResult(null)

    try {
      const formData = new FormData()
      formData.append('file', file)

      const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'
      const response = await fetch(`${baseUrl}/api/tickets/import`, {
        method: 'POST',
        body: formData,
      })

      if (response.ok) {
        const result = await response.json()
        setImportResult(result)
        
        if (result.success > 0) {
          toast({
            title: "Importação concluída",
            description: `${result.success} chamados importados com sucesso.`,
          })
        }
      } else {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Erro na importação')
      }
    } catch (error) {
      console.error('Erro na importação:', error)
      toast({
        title: "Erro na importação",
        description: error instanceof Error ? error.message : "Erro desconhecido",
        variant: "destructive",
      })
    } finally {
      setImporting(false)
    }
  }

  const downloadTemplate = () => {
    const csvContent = [
      ["Prioridade", "Número", "Aberto(a) por", "Aberto(a)", "Atribuído(a)", "Atualizado", "Descrição resumida"],
      ["Alta", "INC2024001", "João Silva", "2024-01-15", "Maria Santos", "2024-01-15", "Exemplo de chamado - Descrição detalhada do problema"],
      ["Crítica", "INC2024002", "Pedro Costa", "2024-01-16", "Ana Oliveira", "2024-01-16", "Outro exemplo - Outra descrição"]
    ].map(row => row.join(",")).join("\n")

    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" })
    const link = document.createElement("a")
    const url = URL.createObjectURL(blob)
    link.setAttribute("href", url)
    link.setAttribute("download", "template-importacao-chamados.csv")
    link.style.visibility = "hidden"
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)

    toast({
      title: "Template baixado",
      description: "O arquivo template foi baixado com sucesso.",
    })
  }

  return (
    <div className="flex flex-col h-screen">
      <PageHeader 
        title="Importar Chamados" 
        breadcrumbs={[
          { label: "Dashboard", href: "/" }, 
          { label: "Chamados", href: "/tickets" }, 
          { label: "Importar" }
        ]}
      >
        <Button variant="outline" onClick={downloadTemplate}>
          <Download className="h-4 w-4 mr-2" />
          Baixar Template
        </Button>
      </PageHeader>

      <div className="flex-1 overflow-auto p-6 space-y-6">
        {/* Instruções */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <FileText className="h-5 w-5" />
              Instruções de Importação
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <h4 className="font-medium mb-2">Formatos Aceitos</h4>
                <ul className="text-sm text-muted-foreground space-y-1">
                  <li>• CSV (Comma Separated Values)</li>
                  <li>• XLSX (Excel)</li>
                  <li>• TXT (Texto separado por vírgulas)</li>
                </ul>
              </div>
              <div>
                <h4 className="font-medium mb-2">Campos Obrigatórios (nesta ordem)</h4>
                <ul className="text-sm text-muted-foreground space-y-1">
                  <li>• <strong>Prioridade:</strong> Crítica, Alta, Moderada ou Baixa</li>
                  <li>• <strong>Número:</strong> Código único do chamado</li>
                  <li>• <strong>Aberto(a) por:</strong> Nome de quem abriu o chamado</li>
                  <li>• <strong>Aberto(a):</strong> Data de abertura (será ignorada, usará data atual)</li>
                  <li>• <strong>Atribuído(a):</strong> Nome do responsável (opcional)</li>
                  <li>• <strong>Atualizado:</strong> Data de atualização (será ignorada, usará data atual)</li>
                  <li>• <strong>Descrição resumida:</strong> Descrição do chamado</li>
                </ul>
              </div>
            </div>
            
            <Alert>
              <AlertCircle className="h-4 w-4" />
              <AlertDescription>
                <strong>Importante:</strong> Certifique-se de que os números dos chamados sejam únicos. 
                Chamados com números duplicados serão ignorados durante a importação. As datas serão ignoradas e substituídas pela data atual.
              </AlertDescription>
            </Alert>
          </CardContent>
        </Card>

        {/* Upload de Arquivo */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Upload className="h-5 w-5" />
              Selecionar Arquivo
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="file">Arquivo para Importação</Label>
              <Input
                id="file"
                type="file"
                accept=".csv,.xlsx,.xls,.txt"
                onChange={handleFileChange}
                disabled={importing}
              />
            </div>
            
            {file && (
              <div className="flex items-center gap-2 p-3 bg-muted rounded-lg">
                <FileText className="h-4 w-4" />
                <span className="text-sm font-medium flex-1">{file.name}</span>
                <Badge variant="outline">
                  {(file.size / 1024).toFixed(1)} KB
                </Badge>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => {
                    setFile(null)
                    setPreview([])
                    setImportResult(null)
                    // Limpar o input file
                    const fileInput = document.getElementById('file') as HTMLInputElement
                    if (fileInput) fileInput.value = ''
                  }}
                  className="h-6 w-6 p-0 hover:bg-red-100 hover:text-red-600"
                  title="Remover arquivo"
                >
                  <X className="h-4 w-4" />
                </Button>
              </div>
            )}

            {file && (
              <Button 
                onClick={handleImport} 
                disabled={importing}
                className="w-full"
              >
                {importing ? (
                  <>
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                    Importando...
                  </>
                ) : (
                  <>
                    <Upload className="h-4 w-4 mr-2" />
                    Importar Chamados
                  </>
                )}
              </Button>
            )}
          </CardContent>
        </Card>

        {/* Preview dos Dados */}
        {preview.length > 0 && (
          <Card>
            <CardHeader>
              <CardTitle>Preview dos Dados</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full border-collapse border border-gray-300">
                  <thead>
                    <tr className="bg-muted">
                      <th className="border border-gray-300 p-2 text-left">Linha</th>
                      <th className="border border-gray-300 p-2 text-left">Dados</th>
                    </tr>
                  </thead>
                  <tbody>
                    {preview.map((row) => (
                      <tr key={row.line}>
                        <td className="border border-gray-300 p-2">{row.line}</td>
                        <td className="border border-gray-300 p-2">
                          <div className="flex flex-wrap gap-1">
                            {row.data.map((cell: string, index: number) => (
                              <Badge key={index} variant="outline" className="text-xs">
                                {cell || '(vazio)'}
                              </Badge>
                            ))}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <p className="text-sm text-muted-foreground mt-2">
                Mostrando apenas as primeiras 5 linhas do arquivo.
              </p>
            </CardContent>
          </Card>
        )}

        {/* Resultado da Importação */}
        {importResult && (
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle className="h-5 w-5 text-green-500" />
                Resultado da Importação
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid gap-4 md:grid-cols-3">
                <div className="text-center p-4 bg-green-50 rounded-lg">
                  <div className="text-2xl font-bold text-green-600">{importResult.success}</div>
                  <div className="text-sm text-green-700">Importados com Sucesso</div>
                </div>
                
                <div className="text-center p-4 bg-red-50 rounded-lg">
                  <div className="text-2xl font-bold text-red-600">{importResult.errors.length}</div>
                  <div className="text-sm text-red-700">Erros</div>
                </div>
                
                <div className="text-center p-4 bg-yellow-50 rounded-lg">
                  <div className="text-2xl font-bold text-yellow-600">{importResult.duplicates.length}</div>
                  <div className="text-sm text-yellow-700">Duplicados Ignorados</div>
                </div>
              </div>

              {importResult.errors.length > 0 && (
                <div>
                  <h4 className="font-medium text-red-600 mb-2">Erros Encontrados:</h4>
                  <div className="space-y-1">
                    {importResult.errors.map((error, index) => (
                      <div key={index} className="text-sm text-red-600 bg-red-50 p-2 rounded">
                        {error}
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {importResult.duplicates.length > 0 && (
                <div>
                  <h4 className="font-medium text-yellow-600 mb-2">Códigos Duplicados Ignorados:</h4>
                  <div className="flex flex-wrap gap-1">
                    {importResult.duplicates.map((duplicate, index) => (
                      <Badge key={index} variant="outline" className="text-yellow-600">
                        {duplicate}
                      </Badge>
                    ))}
                  </div>
                </div>
              )}

              <div className="flex gap-2">
                <Button 
                  onClick={() => router.push('/tickets')} 
                  className="flex-1"
                >
                  Ver Chamados Importados
                </Button>
                <Button 
                  variant="outline" 
                  onClick={() => {
                    setFile(null)
                    setImportResult(null)
                    setPreview([])
                  }}
                >
                  Nova Importação
                </Button>
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  )
}