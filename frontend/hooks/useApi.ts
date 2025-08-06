'use client'

import { useAuth } from '@/contexts/AuthContext'
import { useCallback } from 'react'

interface ApiOptions {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH'
  body?: any
  headers?: Record<string, string>
}

export function useApi() {
  const { token, logout } = useAuth()

  const apiCall = useCallback(async (endpoint: string, options: ApiOptions = {}) => {
    const { method = 'GET', body, headers = {} } = options

    const config: RequestInit = {
      method,
      headers: {
        'Content-Type': 'application/json',
        ...headers,
      },
    }

    // Adicionar token de autorização se disponível
    if (token) {
      config.headers = {
        ...config.headers,
        'Authorization': `Bearer ${token}`,
      }
    }

    // Adicionar headers para pular avisos de túneis
    const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'
    if (baseUrl.includes('ngrok-free.app')) {
      config.headers = {
        ...config.headers,
        'ngrok-skip-browser-warning': 'true',
      }
    }
    
    // Adicionar headers para LocalTunnel
    if (baseUrl.includes('loca.lt')) {
      config.headers = {
        ...config.headers,
        'Bypass-Tunnel-Reminder': 'true',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
      }
    }

    // Adicionar body se fornecido
    if (body && method !== 'GET') {
      config.body = JSON.stringify(body)
    }

    try {
      // Quando acessado via ngrok, usar caminho relativo para aproveitar o proxy do Next.js
      const baseUrl = typeof window !== 'undefined' && window.location.hostname.includes('ngrok-free.app') 
        ? '' 
        : (process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081')
      const response = await fetch(`${baseUrl}${endpoint}`, config)

      // Se o token expirou ou é inválido, fazer logout
      if (response.status === 401) {
        logout()
        throw new Error('Sessão expirada. Faça login novamente.')
      }

      if (!response.ok) {
        const errorData = await response.text()
        throw new Error(errorData || `Erro ${response.status}: ${response.statusText}`)
      }

      // Se a resposta estiver vazia, retornar null
      const contentType = response.headers.get('content-type')
      if (!contentType || !contentType.includes('application/json')) {
        return null
      }

      return await response.json()
    } catch (error) {
      console.error('Erro na requisição API:', error)
      throw error
    }
  }, [token, logout])

  return { apiCall }
}