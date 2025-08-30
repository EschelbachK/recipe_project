import type {Recipe} from "../types/types"

type Props = {
    recipe: Recipe
    onFavorite: () => void
    onEdit: () => void
    onDelete: () => void
    onAddToShopping: () => void
}

export default function RecipeCard({recipe, onFavorite, onEdit, onDelete, onAddToShopping}: Readonly<Props>) {
    return (
        <div style={{border: "1px solid #ccc", padding: "1rem", marginBottom: "1rem"}}>
            <h3>{recipe.name}</h3>
            <p>Portionen: {recipe.servings}</p>
            <div style={{display: "flex", gap: "0.5rem"}}>
                <button onClick={onFavorite}>FAV❤️</button>
                <button onClick={onEdit}>EDIT✏️</button>
                <button onClick={onDelete}>DEL🗑️</button>
                <button onClick={onAddToShopping}>EL🛒</button>
            </div>
        </div>
    )
}
