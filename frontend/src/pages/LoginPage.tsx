import {useState} from "react"
import "./LoginPage.css"
import {useToast} from "../components/ToastContext.tsx";

export default function LoginPage() {
    const [loading, setLoading] = useState(false)
    const {showToast} = useToast()

    function login() {
        if (loading) return
        setLoading(true)
        showToast("Weiterleitung zu GitHub…", "info")

        const host =
            window.location.host === "localhost:5173"
                ? "http://localhost:8080"
                : window.location.origin

        window.location.href = host + "/oauth2/authorization/github"
    }

    return (
        <div className="login-page">
            <div className="login-box">
                <p>Bitte melde dich mit GitHub an!</p>
                <button className="login-btn" onClick={login} disabled={loading}>
                    {loading ? "Bitte warten…" : "GitHub Login"}
                </button>
            </div>
        </div>
    )
}
