import "./Header.css"
import type {AppUser} from "../types/types"
import {routerConfig} from "../router/routerConfig"
import logo from "../assets/logo.png"

type Props = {
    user: AppUser | null | undefined
}

export default function Header({ user }: Readonly<Props>) {
    function handleLogout() {
        window.location.href = routerConfig.API.LOGOUT
    }

    return (
        <header className="header">
            <div className="header-left" />
            <div className="header-center">
                <img src={logo} alt="RecipelyApp Logo" className="header-logo" />
            </div>
            <div className="header-right">
                {user ? (
                    <>
                        <span className="greeting">Hallo, {user.username}! 👋</span>
                        {user.avatarUrl && (
                            <img src={user.avatarUrl} alt="avatar" className="avatar" />
                        )}
                        <button className="logout-btn" onClick={handleLogout}>
                            Logout
                        </button>
                    </>
                ) : null}
            </div>
        </header>
    )
}
