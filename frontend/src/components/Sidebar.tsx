import {NavLink} from "react-router-dom"
import {routerConfig} from "../router/routerConfig"
import "./Sidebar.css"

export default function Sidebar() {
    return (
        <aside className="sidebar">
            <ul className="sidebar-list">
                <li>
                    <NavLink to={routerConfig.URL.RECIPES}>📖 - Meine Rezepte - </NavLink>
                </li>
                <li>
                    <NavLink to={routerConfig.URL.FAVORITES}>❤️ - Meine Favoriten - </NavLink>
                </li>
                <li>
                    <NavLink to={routerConfig.URL.SHOPPING_LIST}>🛒 - Meine Einkaufsliste - </NavLink>
                </li>
            </ul>
        </aside>
    )
}