import type {Recipe} from "../../types/types.ts"
import RecipeCard from "./RecipeCard.tsx"
import AddRecipeCard from "./AddRecipeCard.tsx"
import "./RecipeGallery.css"

type Props = {
    recipes: (Recipe & { isFav?: boolean; inShopping?: boolean })[]
    onDelete?: (id: string) => void
    onEdit?: (id: string) => void
    onFavorite: (id: string) => void
    onAddToShopping: (id: string) => void
    showAddCard?: boolean
}

export default function RecipeGallery({
                                          recipes,
                                          onDelete,
                                          onEdit,
                                          onFavorite,
                                          onAddToShopping,
                                          showAddCard = false,
                                      }: Readonly<Props>) {
    return (
        <div className="gallery">
            {showAddCard && <AddRecipeCard/>}
            {recipes.length === 0 ? (
                <p>Keine Rezepte vorhanden.</p>
            ) : (
                recipes.map(r => (
                    <RecipeCard
                        key={r.id}
                        recipe={r}
                        isFav={r.isFav ?? false}
                        inShopping={r.inShopping ?? false}
                        onDelete={() => onDelete?.(r.id)}
                        onEdit={() => onEdit?.(r.id)}
                        onFavorite={() => onFavorite(r.id)}
                        onAddToShopping={() => onAddToShopping(r.id)}
                    />
                ))
            )}
        </div>
    )
}