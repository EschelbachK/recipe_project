import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import type { Recipe, Ingredient } from "../types/types";

export default function RecipeEditPage() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const isNew = !id || id === "new";

    const [recipe, setRecipe] = useState<Recipe>({
        id: "",
        name: "",
        description: "",
        servings: 1,
        ingredients: [],
    });

    const [currentIng, setCurrentIng] = useState<Ingredient>({
        id: Date.now().toString(),
        name: "",
        amount: 0,
        unit: "G",
    });

    useEffect(() => {
        if (!isNew && id) {
            axios.get<Recipe>(`/api/recipes/${id}`, { withCredentials: true })
                .then(res => setRecipe(res.data))
                .catch(() => {});
        }
    }, [id, isNew]);

    function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = e.target;
        setRecipe(prev => ({ ...prev, [name]: name === "servings" ? Number(value) : value }));
    }

    function handleIngredient(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
        const { name, value } = e.target;
        setCurrentIng(prev => ({ ...prev, [name]: name === "amount" ? Number(value) : value }));
    }

    function addIngredient() {
        setRecipe(prev => ({ ...prev, ingredients: [...prev.ingredients, { ...currentIng, id: Date.now().toString() }] }));
        setCurrentIng({ id: Date.now().toString(), name: "", amount: 0, unit: "G" });
    }

    function removeIngredient(id: string) {
        setRecipe(prev => ({ ...prev, ingredients: prev.ingredients.filter(i => i.id !== id) }));
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        const dto = { name: recipe.name, servings: recipe.servings, description: recipe.description, ingredients: recipe.ingredients };

        if (isNew) {
            await axios.post("/api/recipes", dto, { withCredentials: true });
        } else {
            await axios.put(`/api/recipes/${id}`, dto, { withCredentials: true });
        }
        navigate("/recipes");
    }

    return (
        <div>
            <h2>{isNew ? "Neues Rezept" : "Rezept bearbeiten"}</h2>
            <form onSubmit={handleSubmit}>
                <input name="name" value={recipe.name} onChange={handleChange} placeholder="Name" required />
                <input type="number" name="servings" value={recipe.servings} min={1} onChange={handleChange} placeholder="Portionen" required />
                <textarea name="description" value={recipe.description} onChange={handleChange} placeholder="Beschreibung" required />

                <h3>Zutaten</h3>
                {recipe.ingredients.map(ing => (
                    <div key={ing.id}>
                        {ing.name} {ing.amount} {ing.unit}
                        <button type="button" onClick={() => removeIngredient(ing.id)}>x</button>
                    </div>
                ))}

                <div>
                    <input name="name" value={currentIng.name} onChange={handleIngredient} placeholder="Zutat" />
                    <input type="number" name="amount" value={currentIng.amount} onChange={handleIngredient} placeholder="Menge" />
                    <select name="unit" value={currentIng.unit} onChange={handleIngredient}>
                        <option>KG</option>
                        <option>G</option>
                        <option>ML</option>
                        <option>L</option>
                        <option>PIECE</option>
                    </select>
                    <button type="button" onClick={addIngredient}>+</button>
                </div>

                <button type="submit">Speichern</button>
                <button type="button" onClick={() => navigate("/recipes")}>Abbrechen</button>
            </form>
        </div>
    );
}
