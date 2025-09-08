import type { Recipe } from "../types/types"
import RecipeCover from "./RecipeCover"
import FavButton from "./buttons/FavButton"
import EditButton from "./buttons/EditButton"
import DeleteButton from "./buttons/DeleteButton"
import ShoppingButton from "./buttons/ShoppingButton"
import "./RecipeDetailCard.css"

type Props = {
    recipe: Recipe
    isFav: boolean
    onEdit: () => void
    onDelete: () => void
    onFavorite: () => void
    onAddToShopping: () => void
}

export default function RecipeDetailCard({
                                             recipe,
                                             isFav,
                                             onEdit,
                                             onDelete,
                                             onFavorite,
                                             onAddToShopping,
                                         }: Readonly<Props>) {
    return (
        <div className="detail-card">
            <RecipeCover name={recipe.name} />
            <h2 className="detail-title">{recipe.name}</h2>

            <div className="detail-section">
                <span className="detail-section-title">Portionen:</span>
                <span className="detail-plain">{recipe.servings}</span>
            </div>

            <div className="detail-section">
                <h3 className="detail-section-title">Zutaten:</h3>
                <div className="ingredients-box">
                    {recipe.ingredients.length > 0 ? (
                        <ul>
                            {recipe.ingredients.map(i => (
                                <li key={i.id}>
                                    {i.name} — {i.amount} {i.unit}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>Keine Zutaten vorhanden.</p>
                    )}
                </div>
            </div>

            <div className="detail-section">
                <h3 className="detail-section-title">Zubereitung:</h3>
                <div className="description-box">
                    {recipe.description ? (
                        <p>{recipe.description}</p>
                    ) : (
                        <p>Keine Beschreibung vorhanden.</p>
                    )}
                </div>
            </div>

            <div className="divider" />

            <div className="button-row">
                <FavButton isFav={isFav} onClick={onFavorite} />
                <EditButton onClick={onEdit} />
                <DeleteButton onClick={onDelete} />
                <ShoppingButton onClick={onAddToShopping} />
            </div>
        </div>
    )
}
