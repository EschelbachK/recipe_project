import axios from "axios"
import {useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router-dom"
import type {Recipe} from "../types/types"
import {routerConfig} from "../router/routerConfig"
import RecipeDetailCard from "../components/RecipeDetailCard"

export default function RecipeDetailPage() {
    const {id} = useParams<{ id: string }>()
    const navigate = useNavigate()

    const [recipe, setRecipe] = useState<Recipe | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    async function loadRecipe() {
        if (!id) return
        setLoading(true)
        setError(null)
        try {
            const res = await axios.get<Recipe>(routerConfig.API.RECIPE_ID(id), {withCredentials: true})
            setRecipe(res.data)
        } catch (e: any) {
            const s = e?.response?.status
            if (s === 401) setError("Nicht eingeloggt.")
            else if (s === 404) setError("Rezept nicht gefunden.")
            else setError("Laden fehlgeschlagen.")
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
            navigate(routerConfig.URL.RECIPES)
        } catch {
            alert("Löschen fehlgeschlagen.")
        }
    }

    function handleEdit() {
        if (!recipe) return
        navigate(routerConfig.URL.RECIPE_EDIT_ID(recipe.id))
    }

    function handleFavorite() {
        alert("Favoriten sind bald verfügbar!")
    }

    function handleAddToShopping() {
        alert("Einkaufsliste ist bald verfügbar!")
    }

    if (loading) return <p>Lade...</p>
    if (error) return (
        <div>
            <p>{error}</p>
            <button onClick={() => navigate(routerConfig.URL.RECIPES)}>Zur Übersicht</button>
        </div>
    )
    if (!recipe) return <p>Kein Rezept.</p>

    return (
        <RecipeDetailCard
            recipe={recipe}
            onEdit={handleEdit}
            onDelete={handleDelete}
            onFavorite={handleFavorite}
            onAddToShopping={handleAddToShopping}
        />
    )
}
