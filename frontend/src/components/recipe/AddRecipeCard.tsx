import "./AddRecipeCard.css"
import { useNavigate } from "react-router-dom"
import { routerConfig } from "../../router/routerConfig.ts"

export default function AddRecipeCard() {
    const navigate = useNavigate()

    function handleAction() {
        navigate(routerConfig.URL.RECIPE_NEW)
    }

    return (
        <button
            type="button"
            className="recipe-card add-card"
            onClick={handleAction}
        >
            <div className="card-image">
                <span className="add-symbol">+</span>
            </div>
            <div className="card-content">
                <span>Neues Rezept erstellen</span>
            </div>
        </button>
    )
}
