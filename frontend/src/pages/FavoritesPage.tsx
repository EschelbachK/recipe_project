import { useEffect, useState } from "react"
import axios from "axios"
import type { Recipe } from "../types/types"
import { routerConfig } from "../router/routerConfig"
import RecipeGallery from "../components/RecipeGallery"
import "./RecipesOverviewPage.css"

export default function FavoritesPage() {
    const [recipes, setRecipes] = useState<Recipe[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    async function loadFavorites() {
        setLoading(true)
        setError(null)
        try {
            const res = await axios.get<Recipe[]>(routerConfig.API.FAVORITES, {
                withCredentials: true,
            })
            setRecipes(res.data)
        } catch {
            setError("Fehler beim Laden der Favoriten!")
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        void loadFavorites()
    }, [])

    async function toggleFavorite(id: string) {
        try {
            await axios.post(routerConfig.API.FAVORITES_TOGGLE(id), {}, { withCredentials: true })
            await loadFavorites()
        } catch {
            alert("Favorit konnte nicht geändert werden.")
        }
    }

    function addToShopping(_id: string) {
        alert("Einkaufsliste bald verfügbar!")
    }

    if (loading) return <p>Lade Favoriten...</p>
    if (error) return <p>{error}</p>

    return (
        <div className="overview-page">
            <div className="overview-header">
                <h2>Meine Favoriten</h2>
            </div>
            <RecipeGallery
                recipes={recipes.map(r => ({
                    ...r,
                    isFav: true,
                }))}
                onFavorite={toggleFavorite}
                onAddToShopping={addToShopping}
            />
        </div>
    )
}
