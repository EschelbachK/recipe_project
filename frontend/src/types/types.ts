
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
    unit: Unit | string
};

export type Recipe = {
    id: string;
    name: string;
    description: string;
    servings: number;
    ingredients: Ingredient[] };
