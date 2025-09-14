import {Link} from "react-router-dom"
import {routerConfig} from "../router/routerConfig"
import type {Recipe} from "../types/types"
import RecipeCover from "./RecipeCover"
import FavButton from "./buttons/FavButton"
import EditButton from "./buttons/EditButton"
import DeleteButton from "./buttons/DeleteButton"
import ShoppingButton from "./buttons/ShoppingButton"
import "./RecipeCard.css"

type Props = {
    recipe: Recipe
    isFav: boolean
    inShopping: boolean
    onFavorite: () => void
    onEdit: () => void
    onDelete: () => void
    onAddToShopping: () => void
}

export default function RecipeCard({
                                       recipe,
                                       isFav,
                                       inShopping,
                                       onFavorite,
                                       onEdit,
                                       onDelete,
                                       onAddToShopping,
                                   }: Readonly<Props>) {
    return (
        <article className="card">
            <Link
                to={routerConfig.URL.RECIPE_ID(recipe.id)}
                className="card-click-area"
            >
                <RecipeCover name={recipe.name}/>
                <div className="card-body">
                    <h3>{recipe.name}</h3>
                    <p>Servings: {recipe.servings}</p>
                </div>
            </Link>
            <div className="card-actions">
                <FavButton
                    isFav={isFav}
                    onClick={(e) => {
                        e.preventDefault()
                        e.stopPropagation()
                        onFavorite()
                    }}
                />
                <EditButton
                    onClick={(e) => {
                        e.preventDefault()
                        e.stopPropagation()
                        onEdit()
                    }}
                />
                <DeleteButton
                    onClick={(e) => {
                        e.preventDefault()
                        e.stopPropagation()
                        onDelete()
                    }}
                />
                <ShoppingButton
                    isActive={inShopping}
                    onClick={(e) => {
                        e.preventDefault()
                        e.stopPropagation()
                        onAddToShopping()
                    }}
                />
            </div>
        </article>
    )
}
