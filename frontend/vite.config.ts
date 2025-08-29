import {defineConfig} from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            "/api": {target: "http://localhost:8080", changeOrigin: true},
            "/oauth2": {target: "http://localhost:8080", changeOrigin: true},
            "/logout": {target: "http://localhost:8080", changeOrigin: true}
        },
    },
});