import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

const proxyTarget = process.env.VITE_PROXY_TARGET || 'http://localhost:8082'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5175,
    proxy: {
      '/api': {
        target: proxyTarget,
        changeOrigin: true,
        xfwd: true,
      },
    },
  },
  preview: {
    port: 5175,
    proxy: {
      '/api': {
        target: proxyTarget,
        changeOrigin: true,
        xfwd: true,
      },
    },
  },
})
