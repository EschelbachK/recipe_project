import {Navigate, Outlet} from "react-router-dom"
import type {AppUser} from "../types/types"
import {routerConfig} from "./routerConfig"

type ProtectedRouteProps = {
    user: AppUser | null | undefined
}

export default function ProtectedRoute({user}: Readonly<ProtectedRouteProps>) {
    if (user === undefined) {
        return <div>Laden…</div>
    }

    if (!user) {
        return <Navigate to={routerConfig.URL.LOGIN} replace/>
    }

    return <Outlet/>
}
