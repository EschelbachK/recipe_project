import {Navigate, Outlet} from "react-router-dom";
import type {AppUser} from "../types/types";

export default function ProtectedRoute({user}: Readonly<{ user: AppUser | null | undefined }>) {
    if (user === undefined) {
        return <div>Laden…</div>
    }

    if (!user) {
        return <Navigate to="/login" replace/>;
    }
    return (<Outlet/>)
}