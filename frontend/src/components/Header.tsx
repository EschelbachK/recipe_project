import "./Header.css"
import type { AppUser } from "../types/types"
import { routerConfig } from "../router/routerConfig"

type Props = {
    user: AppUser
}

export default function Header({ user }: Readonly<Props>) {
    function handleLogout() {
        window.location.href = routerConfig.API.LOGOUT
    }

    return (
        <header className="header">
            <div className="header-left" />
            <div className="header-center">
                <h1>Meine Rezeptübersicht</h1>
            </div>
            <div className="header-right">
                <span className="greeting">Hallo, {user.username}! 👋</span>
                {user.avatarUrl && (
                    <img src={user.avatarUrl} alt="avatar" className="avatar" />
                )}
                <button className="logout-btn" onClick={handleLogout}>
                    Logout
                </button>
            </div>
        </header>
    )
}
