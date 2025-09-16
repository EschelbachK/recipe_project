import {useState} from "react"
import axios from "axios"
import type {Recipe} from "../types/types"
import RecipeCover from "./RecipeCover"
import FavButton from "./buttons/FavButton"
import EditButton from "./buttons/EditButton"
import DeleteButton from "./buttons/DeleteButton"
import ShoppingButton from "./buttons/ShoppingButton"
import {routerConfig} from "../router/routerConfig"
import {addToShoppingList, removeFromShoppingList} from "../services/shoppingService"
import "./RecipeDetailCard.css"

type Props = {
    recipe: Recipe
    isFav: boolean
    inShopping: boolean
    onEdit: () => void
    onDelete: () => void
    onFavorite: () => void
    onAddToShopping: () => void
}

export default function RecipeDetailCard({
                                             recipe,
                                             isFav,
                                             inShopping,
                                             onEdit,
                                             onDelete,
                                             onFavorite,
                                             onAddToShopping,
                                         }: Readonly<Props>) {
    const [localRecipe, setLocalRecipe] = useState<Recipe>(recipe)
    const [shopping, setShopping] = useState(inShopping)

    async function scaleServings(newServings: number) {
        if (newServings < 1) return
        const factor = newServings / localRecipe.servings
        const scaledIngredients = localRecipe.ingredients.map(i => ({
            ...i,
            amount: i.amount * factor,
        }))
        const updated = {...localRecipe, servings: newServings, ingredients: scaledIngredients}
        setLocalRecipe(updated)
        await axios.put(routerConfig.API.UPDATE_RECIPE(updated.id), updated, {withCredentials: true})
        if (shopping) {
            await addToShoppingList(updated)
        }
    }

    async function handleShoppingClick() {
        if (shopping) {
            await removeFromShoppingList(localRecipe.id)
            setShopping(false)
        } else {
            await addToShoppingList(localRecipe)
            setShopping(true)
        }
        onAddToShopping()
    }

    return (
        <div className="detail-card">
            <RecipeCover name={localRecipe.name}/>
            <h2 className="detail-title">{localRecipe.name}</h2>

            <div className="detail-section servings-control">
                <span className="detail-section-title">Portionen:</span>
                <div className="servings-box">
                    <button className="servings-btn" onClick={() => scaleServings(localRecipe.servings - 1)}>–</button>
                    <span className="servings-value">{localRecipe.servings}</span>
                    <button className="servings-btn" onClick={() => scaleServings(localRecipe.servings + 1)}>+</button>
                </div>
            </div>

            <div className="detail-section">
                <h3 className="detail-section-title">Zutaten:</h3>
                <div className="ingredients-box">
                    {localRecipe.ingredients.length > 0 ? (
                        <ul>
                            {localRecipe.ingredients.map(i => (
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
                    {localRecipe.description ? <p>{localRecipe.description}</p> : <p>Keine Beschreibung vorhanden.</p>}
                </div>
            </div>

            <div className="divider"/>

            <div className="button-row">
                <FavButton isFav={isFav} onClick={onFavorite}/>
                <EditButton onClick={onEdit}/>
                <DeleteButton onClick={onDelete}/>
                <ShoppingButton isActive={shopping} onClick={handleShoppingClick}/>
            </div>
        </div>
    )
}