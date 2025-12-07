import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    global: 'globalThis',  // Fix for sockjs-client "global is not defined"
  },
  server: {
    proxy: {
      '/maze': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/game-socket': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true,
      }
    }
  }
})
