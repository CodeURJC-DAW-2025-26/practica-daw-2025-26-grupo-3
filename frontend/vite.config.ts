import { reactRouter } from "@react-router/dev/vite";
import tailwindcss from "@tailwindcss/vite";
import { defineConfig } from "vite";

const isProd = process.env.NODE_ENV === "production";

export default defineConfig({
  base: isProd ? "/new/" : "/",
  plugins: [tailwindcss(), reactRouter()],
  resolve: {
    tsconfigPaths: true,
  },
  server: {
    middlewareMode: false,
    proxy: {
      "/api": {
        target: "https://localhost:8443/api",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
        secure: false,
      },
    },
  },
});
