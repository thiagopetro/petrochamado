/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'standalone',
  
  // Configurações para produção
  serverExternalPackages: [],
  
  // Permitir origens de desenvolvimento para LocalTunnel
  allowedDevOrigins: [
    'chamadopetro-gateway.loca.lt',
    'petrochamado-gateway.loca.lt'
  ],
  
  eslint: {
    ignoreDuringBuilds: true,
  },
  typescript: {
    ignoreBuildErrors: true,
  },
  images: {
    domains: ['localhost'],
    unoptimized: true,
  },
  
  // Variables de ambiente públicas
  env: {
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL,
  },
  
  // Headers para contornar aviso do ngrok
  async headers() {
    return [
      {
        source: '/(.*)',
        headers: [
          {
            key: 'ngrok-skip-browser-warning',
            value: 'true',
          },
        ],
      },
    ]
  },
  
  // Proxy para API local quando acessado via ngrok
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:8081/api/:path*',
      },
    ]
  },
}

export default nextConfig
