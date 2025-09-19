import axios from "axios"
import {useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router-dom"
import type {Recipe, Ingredient, ShoppingListItem} from "../types/types"
import {routerConfig} from "../router/routerConfig"
import {addToShoppingList} from "../services/shoppingService"
import LoadingSpinner from "../components/LoadingSpinner"
import "./RecipeEditPage.css"
import {useToast} from "../components/ToastContext.tsx";

export default function RecipeEditPage() {
    const {id} = useParams<{ id: string }>()
    const navigate = useNavigate()
    const isNew = !id || id === "new"

    const [recipe, setRecipe] = useState<Recipe>({
        id: "",
        name: "",
        description: "",
        servings: 1,
        ingredients: [],
    })

    const [currentIng, setCurrentIng] = useState<Ingredient>({
        id: crypto.randomUUID(),
        name: "",
        amount: 0,
        unit: "G",
    })

    const [editIndex, setEditIndex] = useState<number | null>(null)
    const [inShopping, setInShopping] = useState(false)
    const [loading, setLoading] = useState(!isNew)
    const {showToast} = useToast()

    useEffect(() => {
        if (!isNew && id) {
            setLoading(true)
            axios
                .get<Recipe>(routerConfig.API.RECIPE_ID(id), {withCredentials: true})
                .then(res => setRecipe(res.data))
                .finally(() => setLoading(false))
        }
    }, [id, isNew])

    useEffect(() => {
        if (!isNew && id) {
            axios
                .get<ShoppingListItem[]>(routerConfig.API.SHOPPING_LIST, {withCredentials: true})
                .then(res => setInShopping(res.data.some(i => i.recipeId === id)))
                .catch(() => setInShopping(false))
        }
    }, [id, isNew])

    function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const {name, value} = e.target
        if (name === "servings") {
            const newServings = Number(value)
            if (newServings < 1) return
            const factor = newServings / recipe.servings
            const scaledIngredients = recipe.ingredients.map(i => ({
                ...i,
                amount: i.amount * factor,
            }))
            setRecipe(prev => ({...prev, servings: newServings, ingredients: scaledIngredients}))
        } else {
            setRecipe(prev => ({...prev, [name]: value}))
        }
    }

    function handleIngredient(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
        const {name, value} = e.target
        setCurrentIng(prev => ({...prev, [name]: name === "amount" ? Number(value) : value}))
    }

    function addOrUpdateIngredient() {
        if (editIndex !== null) {
            const updatedIngredients = [...recipe.ingredients]
            updatedIngredients[editIndex] = {...currentIng}
            setRecipe(prev => ({...prev, ingredients: updatedIngredients}))
            setEditIndex(null)
        } else {
            setRecipe(prev => ({
                ...prev,
                ingredients: [...prev.ingredients, {...currentIng, id: crypto.randomUUID()}],
            }))
        }
        setCurrentIng({id: crypto.randomUUID(), name: "", amount: 0, unit: "G"})
    }

    function removeIngredient(ingId: string) {
        setRecipe(prev => ({
            ...prev,
            ingredients: prev.ingredients.filter(i => i.id !== ingId),
        }))
    }

    function editIngredient(index: number) {
        setCurrentIng(recipe.ingredients[index])
        setEditIndex(index)
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        const dto = {
            name: recipe.name,
            servings: recipe.servings,
            description: recipe.description,
            ingredients: recipe.ingredients,
        }

        try {
            if (isNew) {
                await axios.post(routerConfig.API.CREATE_RECIPE, dto, {withCredentials: true})
                showToast("Rezept wurde gespeichert!", "success")
            } else if (id) {
                await axios.put(routerConfig.API.UPDATE_RECIPE(id), dto, {withCredentials: true})
                showToast("Rezept wurde gespeichert!", "success")
                if (inShopping) {
                    try {
                        await addToShoppingList({...recipe, id})
                    } catch {
                        showToast("Einkaufsliste konnte nicht aktualisiert werden!", "error")
                    }
                }
            }
            navigate(routerConfig.URL.RECIPES)
        } catch {
            showToast("Rezept konnte nicht gespeichert werden!", "error")
        }
    }

    if (loading) {
        return (
            <div className="page-center">
                <LoadingSpinner size={50}/>
            </div>
        )
    }

    return (
        <div className="edit-page-container">
            <div className="edit-page">
                <h2 className="page-title">{isNew ? "Neues Rezept" : "Rezept bearbeiten"}</h2>
                <div className="edit-box">
                    <form onSubmit={handleSubmit} className="edit-form">
                        <div className="form-row-inline">
                            <div className="input-group wide">
                                <label htmlFor="name">Rezeptname:</label>
                                <input id="name" name="name" value={recipe.name} onChange={handleChange} required/>
                            </div>
                            <div className="input-group narrow">
                                <label htmlFor="servings">Portionen:</label>
                                <input
                                    id="servings"
                                    type="number"
                                    name="servings"
                                    value={recipe.servings}
                                    min={1}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>

                        <div className="form-row-inline">
                            <div className="input-group wide">
                                <label htmlFor="ingredient-name">Eine Zutat hinzufügen:</label>
                                <input
                                    id="ingredient-name"
                                    name="name"
                                    value={currentIng.name}
                                    onChange={handleIngredient}
                                    placeholder="Zutat"
                                    className="ingredient-name-input"
                                />
                            </div>
                            <div className="input-group narrow">
                                <label htmlFor="amount">Menge (g/ml):</label>
                                <div className="amount-fields">
                                    <input
                                        id="amount"
                                        type="number"
                                        name="amount"
                                        value={currentIng.amount}
                                        onChange={handleIngredient}
                                    />
                                    <select
                                        id="unit"
                                        name="unit"
                                        value={currentIng.unit}
                                        onChange={handleIngredient}
                                    >
                                        <option value="G">Gramm (g)</option>
                                        <option value="ML">Milliliter (ml)</option>
                                    </select>
                                    <button type="button" onClick={addOrUpdateIngredient}>
                                        {editIndex !== null ? "✔" : "+"}
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div className="form-row">
                            <h3 className="section-title">Zutatenliste:</h3>
                            {recipe.ingredients.map((ing, index) => (
                                <div key={ing.id} className="ingredient">
                                    <span>{ing.name} {ing.amount} {ing.unit}</span>
                                    <div className="ingredient-actions">
                                        <button type="button" className="edit-btn"
                                                onClick={() => editIngredient(index)}>✏️
                                        </button>
                                        <button type="button" className="remove-btn"
                                                onClick={() => removeIngredient(ing.id)}>
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>

                        <div className="form-row">
                            <h3 className="section-title">Zubereitung:</h3>
                            <textarea
                                id="description"
                                name="description"
                                value={recipe.description}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-actions">
                            <button type="submit">Speichern und schließen</button>
                            <button type="button" onClick={() => navigate(routerConfig.URL.RECIPES)}>Abbrechen</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}