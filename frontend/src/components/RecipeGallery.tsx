import type {Recipe} from "../types/types"
import RecipeCard from "./RecipeCard"

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
                                          onAddToShopping
                                      }: Readonly<Props>) {
    if (recipes.length === 0) {
        return <p>Keine Rezepte vorhanden.</p>
    }

    return (
        <div style={{display: "grid", gap: "1rem"}}>
            {recipes.map(r => (
                <RecipeCard
                    key={r.id}
                    recipe={r}
                    onDelete={() => onDelete(r.id)}
                    onEdit={() => onEdit(r.id)}
                    onFavorite={() => onFavorite(r.id)}
                    onAddToShopping={() => onAddToShopping(r.id)}
                />
            ))}
        </div>
    )
}
