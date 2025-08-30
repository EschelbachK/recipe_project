import type {Recipe} from "../types/types"

type Props = {
    recipe: Recipe
    onEdit: () => void
    onDelete: () => void
    onFavorite: () => void
    onAddToShopping: () => void
}

export default function RecipeDetailCard({
                                             recipe,
                                             onEdit,
                                             onDelete,
                                             onFavorite,
                                             onAddToShopping
                                         }: Readonly<Props>) {

    return (
        <div style={{border: "1px solid #ccc", padding: "1rem", marginBottom: "1rem", background: "black"}}>
            <h2>{recipe.name}</h2>
            <p>Portionen: {recipe.servings}</p>
            {recipe.description && <p>{recipe.description}</p>}

            <h3>Zutaten</h3>
            {recipe.ingredients && recipe.ingredients.length > 0 ? (
                <ul>
                    {recipe.ingredients.map(i => (
                        <li key={i.id}>
                            {i.name} — {i.amount} {i.unit}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>Keine Zutaten.</p>
            )}

            <div style={{marginTop: "1rem", display: "flex", gap: "0.5rem"}}>
                <button onClick={onFavorite}>FAV❤️</button>
                <button onClick={onEdit}>EDIT✏️</button>
                <button onClick={onDelete}>DEL🗑️</button>
                <button onClick={onAddToShopping}>EL🛒</button>
            </div>
        </div>
    )
}
