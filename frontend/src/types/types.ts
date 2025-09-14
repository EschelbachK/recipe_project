export type AppUser = {
    id: string;
    username: string;
    avatarUrl: string
};

export type Unit = "KG" | "G" | "ML" | "L" | "PIECE";

export type Ingredient = {
    id: string;
    name: string;
    amount: number;
    unit: Unit;
};

export type Recipe = {
    id: string;
    name: string;
    description: string;
    servings: number;
    ingredients: Ingredient[];
    coverUrl?: string;
}
export type ShoppingListItem = {
    id: string;
    recipeId: string;
    ingredientId: string;
    name: string;
    amount: number;
    unit: Unit;
    done: boolean;
};