import { useEffect, useState } from "react"
import axios from "axios"
import type { AppUser } from "./types/types"
import { Router } from "./router/Router"
import Header from "./components/Header"
import Footer from "./components/Footer"
import Sidebar from "./components/Sidebar"
import "./App.css"

export default function App() {
    const [user, setUser] = useState<AppUser | null | undefined>(undefined)

    useEffect(() => {
        const loadUser = async () => {
            try {
                const res = await axios.get<AppUser>("/api/auth/me", { withCredentials: true })
                setUser(res.data)
            } catch {
                setUser(null)
            }
        }
        void loadUser()
    }, [])

    return (
        <div className="app-container">
            {user && <Header user={user} />}

            <div className="app-content">
                <Sidebar />
                <main style={{ flex: 1, padding: "1rem", overflowY: "auto" }}>
                    <Router user={user} />
                </main>
            </div>

            <Footer />
        </div>
    )
}
