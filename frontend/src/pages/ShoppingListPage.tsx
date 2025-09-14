import {useEffect, useState} from "react"
import axios from "axios"
import {routerConfig} from "../router/routerConfig"
import type {ShoppingListItem} from "../types/types"
import "./ShoppingListPage.css"

export default function ShoppingListPage() {
    const [items, setItems] = useState<ShoppingListItem[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    async function loadShoppingList() {
        setLoading(true)
        setError(null)
        try {
            const res = await axios.get<ShoppingListItem[]>(routerConfig.API.SHOPPING_LIST, {
                withCredentials: true,
            })
            setItems(res.data)
        } catch {
            setError("Einkaufsliste laden fehlgeschlagen.")
        } finally {
            setLoading(false)
        }
    }

    async function removeItem(id: string) {
        try {
            await axios.delete(routerConfig.API.SHOPPING_LIST_REMOVE(id), {withCredentials: true})
            setItems(prev => prev.filter(i => i.id !== id))
        } catch {
            alert("Entfernen fehlgeschlagen.")
        }
    }

    useEffect(() => {
        void loadShoppingList()
    }, [])

    function shareList() {
        const text = items.map(i => `${i.amount} ${i.unit} ${i.name}`).join("\n")
        const url = `https://wa.me/?text=${encodeURIComponent(text)}`
        window.open(url, "_blank")
    }

    if (loading) return <p>Lade Einkaufsliste...</p>
    if (error) return <p>{error}</p>

    return (
        <div className="overview-page">
            <div className="overview-header">
                <h2>Meine Einkaufsliste</h2>
            </div>

            <div className="shopping-list-box">
                {items.length === 0 ? (
                    <p>Keine Zutaten in der Einkaufsliste.</p>
                ) : (
                    <ul className="shopping-list">
                        {items.map(i => (
                            <li key={i.id} className="shopping-item">
                                <span>{i.amount} {i.unit} {i.name}</span>
                                <button className="remove-btn" onClick={() => removeItem(i.id)}>❌</button>
                            </li>
                        ))}
                    </ul>
                )}

                <div className="shopping-list-footer">
                    <button className="share-btn" onClick={shareList}>In WhatsApp teilen?</button>
                </div>
            </div>
        </div>
    )
}