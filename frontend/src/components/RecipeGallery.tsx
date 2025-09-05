import type { Recipe } from "../types/types"
import RecipeCard from "./RecipeCard"
import AddRecipeCard from "./AddRecipeCard"
import "./RecipeGallery.css"

type Props = {
    recipes: Recipe[]
    onDelete: (id: string) => void
    onEdit: (id: string) => void
    onFavorite: (id: string) => void
    onAddToShopping: (id: string) => void
}

export default function RecipeGallery({
                                          recipes,
                                          onDelete,
                                          onEdit,
                                          onFavorite,
                                          onAddToShopping,
                                      }: Readonly<Props>) {
    return (
        <div className="gallery">
            <AddRecipeCard />
            {recipes.length === 0 ? (
                <p>Keine Rezepte vorhanden.</p>
            ) : (
                recipes.map(r => (
                    <RecipeCard
                        key={r.id}
                        recipe={r}
                        onDelete={() => onDelete(r.id)}
                        onEdit={() => onEdit(r.id)}
                        onFavorite={() => onFavorite(r.id)}
                        onAddToShopping={() => onAddToShopping(r.id)}
                    />
                ))
            )}
        </div>
    )
}
