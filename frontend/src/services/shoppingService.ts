import axios from "axios"
import {routerConfig} from "../router/routerConfig"
import type {Recipe} from "../types/types"

export async function addToShoppingList(recipe: Recipe) {
    const items = recipe.ingredients.map(i => ({
        ingredientId: i.id,
        name: i.name,
        amount: i.amount,
        unit: i.unit
    }))

    await axios.post(
        routerConfig.API.SHOPPING_LIST_ADD,
        {recipeId: recipe.id, items},
        {withCredentials: true}
    )
}

export async function removeFromShoppingList(recipeId: string) {
    await axios.delete(
        routerConfig.API.SHOPPING_LIST_REMOVE(recipeId),
        {withCredentials: true}
    )
}