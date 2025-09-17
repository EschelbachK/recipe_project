import "./ToastContainer.css"

type ToastType = "success" | "error" | "info" | "warning"

type Toast = {
    id: string
    message: string
    type: ToastType
}

export default function ToastContainer({toasts}: Readonly<{ toasts: Toast[] }>) {
    return (
        <div className="toast-container">
            {toasts.map(t => (
                <div key={t.id} className={`toast ${t.type}`}>
                    {t.message}
                </div>
            ))}
        </div>
    )
}