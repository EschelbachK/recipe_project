import {createContext, useContext, useState, useMemo, type ReactNode} from "react"
import ToastContainer from "../components/ToastContainer"

type ToastType = "success" | "error" | "info" | "warning"

type Toast = {
    id: string
    message: string
    type: ToastType
}

type ToastContextType = {
    showToast: (message: string, type?: ToastType) => void
}

const ToastContext = createContext<ToastContextType | undefined>(undefined)

export function ToastProvider({children}: Readonly<{ children: ReactNode }>) {
    const [toasts, setToasts] = useState<Toast[]>([])

    function removeToast(id: string) {
        setToasts(prev => prev.filter(t => t.id !== id))
    }

    function showToast(message: string, type: ToastType = "info") {
        const id = crypto.randomUUID()
        setToasts(prev => [...prev, {id, message, type}])
        setTimeout(() => removeToast(id), 3000)
    }

    const value = useMemo(() => ({showToast}), [])

    return (
        <ToastContext.Provider value={value}>
            {children}
            <ToastContainer toasts={toasts}/>
        </ToastContext.Provider>
    )
}

export function useToast() {
    const context = useContext(ToastContext)
    if (!context) throw new Error("useToast must be used inside ToastProvider")
    return context
}