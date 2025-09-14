import {useEffect, useState} from "react"
import {useNavigate} from "react-router-dom"
import axios from "axios"
import type {Recipe, ShoppingListItem} from "../types/types"
import {routerConfig} from "../router/routerConfig"
import RecipeGallery from "../components/RecipeGallery"
import {addToShoppingList, removeFromShoppingList} from "../services/shoppingService"
import "./RecipesOverviewPage.css"

export default function RecipesOverviewPage() {
    const [recipes, setRecipes] = useState<Recipe[]>([])
    const [favoriteIds, setFavoriteIds] = useState<string[]>([])
    const [shoppingIds, setShoppingIds] = useState<string[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate()

    async function loadRecipes() {
        setLoading(true)
        setError(null)
        try {
            const [recipesRes, favRes, shoppingRes] = await Promise.all([
                axios.get<Recipe[]>(routerConfig.API.RECIPES, {withCredentials: true}),
                axios.get<Recipe[]>(routerConfig.API.FAVORITES, {withCredentials: true}),
                axios.get<ShoppingListItem[]>(routerConfig.API.SHOPPING_LIST, {withCredentials: true})
            ])
            setRecipes(recipesRes.data)
            setFavoriteIds(favRes.data.map(r => r.id))
            setShoppingIds(shoppingRes.data.map(r => r.recipeId))
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
        // ✅ Optimistisch updaten
        setFavoriteIds(prev => prev.includes(id) ? prev.filter(f => f !== id) : [...prev, id])
        try {
            await axios.post(routerConfig.API.FAVORITES_TOGGLE(id), {}, {withCredentials: true})
        } catch {
            await loadRecipes()
        }
    }

    async function toggleShopping(recipe: Recipe) {
        if (shoppingIds.includes(recipe.id)) {
            setShoppingIds(prev => prev.filter(s => s !== recipe.id))
            try {
                await removeFromShoppingList(recipe.id)
            } catch {
                await loadRecipes()
            }
        } else {
            setShoppingIds(prev => [...prev, recipe.id])
            try {
                await addToShoppingList(recipe)
            } catch {
                await loadRecipes()
            }
        }
    }

    async function deleteRecipe(id: string) {
        if (!confirm("Rezept wirklich löschen?")) return
        try {
            await axios.delete(routerConfig.API.RECIPE_ID(id), {withCredentials: true})
            setRecipes(prev => prev.filter(r => r.id !== id))
        } catch {
            alert("Rezept konnte nicht gelöscht werden.")
        }
    }

    function editRecipe(id: string) {
        navigate(routerConfig.URL.RECIPE_EDIT_ID(id))
    }

    return (
        <div className="overview-page">
            <div className="overview-header">
                <h2>Meine Rezepte</h2>
            </div>
            {loading && <p className="loading">Lade Rezepte...</p>}
            {error && <p className="error">{error}</p>}
            {!loading && !error && (
                <RecipeGallery
                    recipes={recipes.map(r => ({
                        ...r,
                        isFav: favoriteIds.includes(r.id),
                        inShopping: shoppingIds.includes(r.id),
                    }))}
                    onFavorite={toggleFavorite}
                    onAddToShopping={(id) => {
                        const recipe = recipes.find(r => r.id === id)
                        if (recipe) void toggleShopping(recipe)
                    }}
                    onEdit={editRecipe}
                    onDelete={deleteRecipe}
                    showAddCard={true}
                />
            )}
        </div>
    )
}