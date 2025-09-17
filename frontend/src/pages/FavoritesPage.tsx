import {useEffect, useState} from "react"
import {useNavigate} from "react-router-dom"
import axios from "axios"
import type {Recipe, ShoppingListItem} from "../types/types"
import {routerConfig} from "../router/routerConfig"
import RecipeGallery from "../components/recipe/RecipeGallery.tsx"
import {addToShoppingList, removeFromShoppingList} from "../services/shoppingService"
import LoadingSpinner from "../components/LoadingSpinner"
import {useToast} from "../components/ToastContext.tsx";

export default function FavoritesPage() {
    const [recipes, setRecipes] = useState<Recipe[]>([])
    const [shoppingIds, setShoppingIds] = useState<string[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate()
    const {showToast} = useToast()

    async function loadFavorites() {
        setLoading(true)
        setError(null)
        try {
            const [favRes, shoppingRes] = await Promise.all([
                axios.get<Recipe[]>(routerConfig.API.FAVORITES, {withCredentials: true}),
                axios.get<ShoppingListItem[]>(routerConfig.API.SHOPPING_LIST, {withCredentials: true})
            ])
            setRecipes(favRes.data)
            setShoppingIds(shoppingRes.data.map(r => r.recipeId))
        } catch {
            setError("Fehler beim Laden der Favoriten!")
            showToast("Favoriten konnten nicht geladen werden", "error")
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        void loadFavorites()
    }, [])

    async function toggleShopping(recipe: Recipe) {
        if (shoppingIds.includes(recipe.id)) {
            setShoppingIds(prev => prev.filter(s => s !== recipe.id))
            try {
                await removeFromShoppingList(recipe.id)
                showToast("Von der Einkaufsliste entfernt!", "info")
            } catch {
                showToast("Einkaufsliste konnte nicht aktualisiert werden!", "error")
                await loadFavorites()
            }
        } else {
            setShoppingIds(prev => [...prev, recipe.id])
            try {
                await addToShoppingList(recipe)
                showToast("Zur Einkaufsliste hinzugefügt!", "success")
            } catch {
                showToast("Einkaufsliste konnte nicht aktualisiert werden!", "error")
                await loadFavorites()
            }
        }
    }

    async function toggleFavorite(id: string) {
        setRecipes(prev => prev.filter(r => r.id !== id))
        try {
            await axios.post(routerConfig.API.FAVORITES_TOGGLE(id), {}, {withCredentials: true})
            showToast("Favorit wurde entfernt!", "success")
        } catch {
            showToast("Favorit konnte nicht geändert werden!", "error")
            await loadFavorites()
        }
    }

    function editRecipe(id: string) {
        navigate(routerConfig.URL.RECIPE_EDIT_ID(id))
    }

    return (
        <div className="overview-page">
            <div className="overview-header">
                <h2>Meine Favoriten</h2>
            </div>
            {loading && (
                <div className="page-center">
                    <LoadingSpinner size={50}/>
                </div>
            )}
            {error && <p className="error">{error}</p>}
            {!loading && !error && (
                <RecipeGallery
                    recipes={recipes.map(r => ({
                        ...r,
                        isFav: true,
                        inShopping: shoppingIds.includes(r.id),
                    }))}
                    onFavorite={toggleFavorite}
                    onAddToShopping={(id) => {
                        const recipe = recipes.find(r => r.id === id)
                        if (recipe) void toggleShopping(recipe)
                    }}
                    onEdit={editRecipe}
                    onDelete={() => {
                    }}
                />
            )}
        </div>
    )
}