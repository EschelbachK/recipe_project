import {useEffect, useState} from "react"
import axios from "axios"
import {Router} from "./router/Router"
import type {AppUser} from "./types/types"

export default function App() {
    const [user, setUser] = useState<AppUser | null | undefined>(undefined)

    useEffect(() => {
        const loadUser = async () => {
            try {
                const res = await axios.get<AppUser>("/api/auth/me", {withCredentials: true})
                setUser(res.data)
            } catch {
                setUser(null)
            }
        }

        void loadUser()
    }, [])

    return <Router user={user}/>
}
