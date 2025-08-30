import {useNavigate} from "react-router-dom"
import type {Recipe} from "../types/types"
import {routerConfig} from "../router/routerConfig"

type Props = {
    recipe: Recipe
    onFavorite: () => void
    onEdit: () => void
    onDelete: () => void
    onAddToShopping: () => void
}

export default function RecipeCard({
                                       recipe,
                                       onFavorite,
                                       onEdit,
                                       onDelete,
                                       onAddToShopping
                                   }: Readonly<Props>) {

    const navigate = useNavigate()

    function openDetail() {
        navigate(routerConfig.URL.RECIPE_ID(recipe.id))
    }

    return (
        <button
            onClick={openDetail}
            style={{
                border: "1px solid #ccc",
                padding: "1rem",
                marginBottom: "1rem",
                textAlign: "left",
                cursor: "pointer",
                background: "white",
                color: "black"
            }}
        >
            <h3>{recipe.name}</h3>
            <p>Portionen: {recipe.servings}</p>
            <div style={{display: "flex", gap: "0.5rem"}}>
                <button onClick={(e) => { e.stopPropagation(); onFavorite() }}>FAV❤️</button>
                <button onClick={(e) => { e.stopPropagation(); onEdit() }}>EDIT✏️</button>
                <button onClick={(e) => { e.stopPropagation(); onDelete() }}>DEL🗑️</button>
                <button onClick={(e) => { e.stopPropagation(); onAddToShopping() }}>EL🛒</button>
            </div>
        </button>
    )
}
