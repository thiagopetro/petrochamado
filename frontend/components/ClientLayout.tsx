'use client'

import { usePathname } from 'next/navigation'
import { useAuth } from '@/contexts/AuthContext'
import { SidebarProvider } from '@/components/ui/sidebar'
import { AppSidebar } from '@/components/app-sidebar'
import ProtectedRoute from '@/components/ProtectedRoute'

interface ClientLayoutProps {
  children: React.ReactNode
}

export default function ClientLayout({ children }: ClientLayoutProps) {
  const pathname = usePathname()
  const { isAuthenticated, isLoading } = useAuth()
  
  // Páginas que não precisam de autenticação
  const publicRoutes = ['/login']
  const isPublicRoute = publicRoutes.includes(pathname)

  // Se estiver carregando, não renderizar nada ainda
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Carregando...</p>
        </div>
      </div>
    )
  }

  // Se for uma rota pública (como login), renderizar sem sidebar
  if (isPublicRoute) {
    return <>{children}</>
  }

  // Se não estiver autenticado e não for rota pública, proteger a rota
  if (!isAuthenticated) {
    return (
      <ProtectedRoute>
        {children}
      </ProtectedRoute>
    )
  }

  // Se estiver autenticado, renderizar com sidebar
  return (
    <SidebarProvider>
      <AppSidebar />
      <main className="flex-1 overflow-hidden">
        {children}
      </main>
    </SidebarProvider>
  )
}