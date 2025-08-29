import { BrowserRouter } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Router } from "./router/Router";
import type { AppUser } from "./types/types";

export default function App() {
    const [user, setUser] = useState<AppUser | null | undefined>(undefined);

    useEffect(() => {
        axios
            .get<AppUser>("/api/auth/me", { withCredentials: true })
            .then((r) => setUser(r.data))
            .catch((e) => {
                if (e?.response?.status === 401) setUser(null);
                else setUser(null);
            });
    }, []);

    return (
        <BrowserRouter>
            <Router user={user} />
        </BrowserRouter>
    );
}
