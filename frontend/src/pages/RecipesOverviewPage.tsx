import axios from "axios"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import type { Recipe } from "../types/types"
import RecipeGallery from "../components/RecipeGallery"
import { routerConfig } from "../router/routerConfig"
import "./RecipesOverviewPage.css"

export default function RecipesOverviewPage() {
    const [recipes, setRecipes] = useState<Recipe[]>([])
    const [favoriteIds, setFavoriteIds] = useState<string[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate()

    async function loadRecipes() {
        setLoading(true)
        setError(null)
        try {
            const response = await axios.get<Recipe[]>(routerConfig.API.RECIPES, { withCredentials: true })
            setRecipes(response.data)

            const favRes = await axios.get<Recipe[]>(routerConfig.API.FAVORITES, { withCredentials: true })
            setFavoriteIds(favRes.data.map(r => r.id))
        } catch {
            setError("Fehler beim Laden der Rezepte!")
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        void loadRecipes()
    }, [])

    async function toggleFavorite(id: string) {
        // Optimistisches Update
        setFavoriteIds(prev =>
            prev.includes(id) ? prev.filter(f => f !== id) : [...prev, id]
        )

        try {
            await axios.post(routerConfig.API.FAVORITES_TOGGLE(id), {}, { withCredentials: true })
        } catch {
            alert("Favorit konnte nicht geändert werden.")
            await loadRecipes()
        }
    }

    async function deleteRecipe(id: string) {
        const confirmed = confirm("Rezept wirklich löschen?")
        if (!confirmed) return
        try {
            await axios.delete(routerConfig.API.RECIPE_ID(id), { withCredentials: true })
            void loadRecipes()
        } catch {
            alert("Rezept konnte nicht gelöscht werden.")
        }
    }

    function editRecipe(id: string) {
        navigate(routerConfig.URL.RECIPE_EDIT_ID(id))
    }

    function addToShopping(_id: string) {
        alert("Einkaufsliste ist bald verfügbar!")
    }

    if (loading) return <p className="loading">Lade Rezepte...</p>
    if (error) return <p className="error">{error}</p>

    return (
        <div className="overview-page">
            <div className="overview-header">
                <h2>Meine Rezepte</h2>
            </div>
            <RecipeGallery
                recipes={recipes.map(r => ({
                    ...r,
                    isFav: favoriteIds.includes(r.id),
                }))}
                onDelete={deleteRecipe}
                onEdit={editRecipe}
                onFavorite={toggleFavorite}
                onAddToShopping={addToShopping}
                showAddCard={true}
            />
        </div>
    )
}
