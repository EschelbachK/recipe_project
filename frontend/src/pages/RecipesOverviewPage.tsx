import axios from "axios";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import type {Recipe} from "../types/types";

export default function RecipesOverviewPage() {
    const [recipes, setRecipes] = useState<Recipe[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();

    async function loadRecipes() {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.get<Recipe[]>("/api/recipes", {withCredentials: true});
            setRecipes(response.data);
        } catch {
            setError("Fehler beim Laden der Rezepte!");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadRecipes();
    }, []);

    async function deleteRecipe(id: string) {
        const confirmed = confirm("Rezept wirklich löschen?");
        if (!confirmed) return;

        try {
            await axios.delete(`/api/recipes/${id}`, {withCredentials: true});
            loadRecipes();
        } catch {
            alert("Rezept konnte nicht gelöscht werden.");
        }
    }

    function logout() {
        fetch("/logout", {method: "POST", credentials: "include"})
            .finally(() => (window.location.href = "/login"));
    }

    if (loading) {
        return <p>Lade Rezepte...</p>;
    }

    if (error) {
        return (
            <div>
                <p>{error}</p>
                <button onClick={loadRecipes}>Erneut versuchen</button>
            </div>
        );
    }

    return (
        <div>
            <h2>Meine Rezepte</h2>

            <button onClick={() => navigate("/recipes/new")}>
                Neues Rezept hinzufügen
            </button>

            {recipes.length === 0 ? (
                <p>Keine Rezepte vorhanden.</p>
            ) : (
                <ul>
                    {recipes.map((recipe) => (
                        <li key={recipe.id}>
                            <b>{recipe.name}</b>{" "}
                            <button onClick={() => navigate(`/recipes/${recipe.id}`)}>Öffnen</button>
                            {" "}
                            <button onClick={() => navigate(`/recipes/${recipe.id}/edit`)}>Bearbeiten</button>
                            {" "}
                            <button onClick={() => deleteRecipe(recipe.id)}>Löschen</button>
                        </li>
                    ))}
                </ul>
            )}

            <button onClick={logout}>Logout</button>
        </div>
    );
}
