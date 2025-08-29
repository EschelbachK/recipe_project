export default function LoginPage() {
    function login() {
        const host = window.location.host === "localhost:5173" ? "http://localhost:8080" : window.location.origin;

        window.location.href = host + "/oauth2/authorization/github";
    }

    return (
        <div>
            <h2>Login</h2>
            <button onClick={login}>Anmeldung mit Github</button>
        </div>
    );
}
