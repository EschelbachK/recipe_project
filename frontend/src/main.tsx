import {StrictMode} from "react"
import {createRoot} from "react-dom/client"
import {BrowserRouter} from "react-router-dom"

import "./styles/variables.css"
import "./styles/global.css"

import App from "./App"
import {ToastProvider} from "./components/ToastContext.tsx";


createRoot(document.getElementById("root")!).render(
    <StrictMode>
        <BrowserRouter>
            <ToastProvider>
                <App/>
            </ToastProvider>
        </BrowserRouter>
    </StrictMode>
)