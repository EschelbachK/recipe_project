import axios from "axios";
import {useEffect, useState} from "react";
import {useParams, useNavigate} from "react-router-dom";
import type {Recipe} from "../types/types";

export default function RecipeDetailPage() {
    const {id} = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [recipe, setRecipe] = useState<Recipe | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    async function loadRecipe() {
        if (!id) return;
        setLoading(true);
        setError(null);
        try {
            const res = await axios.get<Recipe>(`/api/recipes/${id}`, {withCredentials: true});
            setRecipe(res.data);
        } catch (e: any) {
            const status = e?.response?.status;
            if (status === 401) setError("Nicht eingeloggt (401).");
            else if (status === 404) setError("Rezept wurde nicht gefunden (404).");
            else setError("Fehler beim Laden des Rezepts.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadRecipe();
    }, [id]);

    async function deleteRecipe() {
        if (!recipe) return;
        if (!confirm("Dieses Rezept wirklich löschen?")) return;
        try {
            await axios.delete(`/api/recipes/${recipe.id}`, {withCredentials: true});
            navigate("/recipes");
        } catch {
            alert("Löschen fehlgeschlagen.");
        }
    }

    function editRecipe() {
        if (!recipe) return;
        navigate(`/recipes/${recipe.id}/edit`);
    }

    function backToOverview() {
        navigate("/recipes");
    }

    if (loading) return <p>Laden…</p>;
    if (error) return (
        <div>
            <p>{error}</p>
            <button onClick={backToOverview}>Zur Übersicht</button>
        </div>
    );
    if (!recipe) return <p>Kein Rezept gefunden.</p>;

    const ingredients = recipe.ingredients ?? [];

    return (
        <div>
            <div>
                <h2>{recipe.name || "Rezept"}</h2>
                <div>
                    <button onClick={editRecipe}>Bearbeiten</button>{" "}
                    <button onClick={deleteRecipe}>Löschen</button>
                </div>
            </div>

            <div>
                <p>Portionen: {recipe.servings}</p>
                {recipe.description && <p>{recipe.description}</p>}

                <h3>Zutaten</h3>
                {ingredients.length ? (
                    <ul>
                        {ingredients.map(i => (
                            <li key={i.id}>
                                {i.name} — {i.amount} {i.unit}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>Keine Zutaten vorhanden.</p>
                )}
            </div>

            <div>
                <button onClick={backToOverview}>Zur Übersicht</button>
            </div>
        </div>
    );
}
