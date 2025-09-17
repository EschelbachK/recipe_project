import axios from "axios"
import {useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router-dom"
import type {Recipe, ShoppingListItem} from "../types/types"
import {routerConfig} from "../router/routerConfig"
import RecipeDetailCard from "../components/RecipeDetailCard"
import {addToShoppingList, removeFromShoppingList} from "../services/shoppingService"
import LoadingSpinner from "../components/LoadingSpinner"
import "./RecipeDetailPage.css"
import {useToast} from "../components/ToastContext.tsx";

export default function RecipeDetailPage() {
    const {id} = useParams<{ id: string }>()
    const navigate = useNavigate()

    const [recipe, setRecipe] = useState<Recipe | null>(null)
    const [isFav, setIsFav] = useState(false)
    const [inShopping, setInShopping] = useState(false)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const {showToast} = useToast()

    async function loadRecipe() {
        if (!id) return
        setLoading(true)
        setError(null)
        try {
            const res = await axios.get<Recipe>(routerConfig.API.RECIPE_ID(id), {withCredentials: true})
            setRecipe(res.data)

            const favRes = await axios.get<Recipe[]>(routerConfig.API.FAVORITES, {withCredentials: true})
            setIsFav(favRes.data.some(r => r.id === id))

            const shoppingRes = await axios.get<ShoppingListItem[]>(routerConfig.API.SHOPPING_LIST, {withCredentials: true})
            setInShopping(shoppingRes.data.some(r => r.recipeId === id))
        } catch (e: any) {
            const s = e?.response?.status
            if (s === 401) setError("Nicht eingeloggt.")
            else if (s === 404) setError("Rezept nicht gefunden.")
            else setError("Laden fehlgeschlagen.")
            showToast("Fehler beim Laden des Rezepts", "error")
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        void loadRecipe()
    }, [id])

    async function handleDelete() {
        if (!recipe) return
        if (!confirm("Rezept löschen?")) return
        try {
            await axios.delete(routerConfig.API.RECIPE_ID(recipe.id), {withCredentials: true})
            showToast("Rezept gelöscht", "success")
            navigate(routerConfig.URL.RECIPES)
        } catch {
            showToast("Rezept konnte nicht gelöscht werden!", "error")
        }
    }

    function handleEdit() {
        if (!recipe) return
        navigate(routerConfig.URL.RECIPE_EDIT_ID(recipe.id))
    }

    async function handleFavorite() {
        if (!recipe) return
        const wasFav = isFav
        setIsFav(prev => !prev)
        try {
            await axios.post(routerConfig.API.FAVORITES_TOGGLE(recipe.id), {}, {withCredentials: true})
            showToast(wasFav ? "Favorit wurde entfernt!" : "Favorit wurde hinzugefügt!", "success")
        } catch {
            showToast("Favorit konnte nicht geändert werden!", "error")
            await loadRecipe()
        }
    }

    async function handleAddToShopping() {
        if (!recipe) return
        try {
            if (inShopping) {
                await removeFromShoppingList(recipe.id)
                setInShopping(false)
                showToast("Von der Einkaufsliste entfernt!", "info")
            } else {
                await addToShoppingList(recipe)
                setInShopping(true)
                showToast("Zur Einkaufsliste hinzugefügt!", "success")
            }
        } catch {
            showToast("Einkaufsliste konnte nicht aktualisiert werden!", "error")
        }
    }

    if (loading) {
        return (
            <div className="page-center">
                <LoadingSpinner size={50}/>
            </div>
        )
    }

    if (error) {
        return (
            <div className="error-box">
                <p>{error}</p>
                <button onClick={() => navigate(routerConfig.URL.RECIPES)}>Zur Übersicht</button>
            </div>
        )
    }

    if (!recipe) return <p>Kein Rezept.</p>

    return (
        <div className="detail-page">
            <RecipeDetailCard
                recipe={recipe}
                isFav={isFav}
                inShopping={inShopping}
                onEdit={handleEdit}
                onDelete={handleDelete}
                onFavorite={handleFavorite}
                onAddToShopping={handleAddToShopping}
            />
        </div>
    )
}