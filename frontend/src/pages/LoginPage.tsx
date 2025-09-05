import "./LoginPage.css"

export default function LoginPage() {
    function login() {
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
                <button className="login-btn" onClick={login}>
                    GitHub Login
                </button>
            </div>
        </div>
    )
}
