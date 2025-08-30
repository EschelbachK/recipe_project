import {NavLink} from "react-router-dom"
import {routerConfig} from "../router/routerConfig"

export default function Sidebar() {
    return (
        <aside style={{width: "200px", padding: "1rem", borderRight: "1px solid #ccc", minHeight: "100vh"}}>
            <ul style={{listStyle: "none", padding: 0}}>
                <li>
                    <NavLink to={routerConfig.URL.RECIPES}>Meine Rezepte</NavLink>
                </li>
                <li>
                    <NavLink to={routerConfig.URL.FAVORITES}>Meine Favoriten</NavLink>
                </li>
                <li>
                    <NavLink to={routerConfig.URL.SHOPPING_LIST}>Meine Einkaufsliste</NavLink>
                </li>
            </ul>
        </aside>
    )
}
