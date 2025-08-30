type RouterConfig = {
    URL: URL
    API: API
}

type URL = {
    HOME: string
    RECIPES: string
    RECIPE_NEW: string
    RECIPE_EDIT: string
    RECIPE_DETAIL: string
    RECIPE_ID: (id: string) => string
    RECIPE_EDIT_ID: (id: string) => string
    LOGIN: string
    FAVORITES: string
    SHOPPING_LIST: string
}

type API = {
    RECIPES: string
    RECIPE_ID: (id: string) => string
    CREATE_RECIPE: string
    UPDATE_RECIPE: (id: string) => string
    AUTH_USER: string
    GITHUB_AUTH: string
}

export const routerConfig: RouterConfig = {
    URL: {
        HOME: "/",
        RECIPES: "/recipes",
        RECIPE_NEW: "/recipes/new",
        RECIPE_EDIT: "/recipes/:id/edit",
        RECIPE_DETAIL: "/recipes/:id",
        RECIPE_ID: (id: string) => `/recipes/${id}`,
        RECIPE_EDIT_ID: (id: string) => `/recipes/${id}/edit`,
        LOGIN: "/login",
        FAVORITES: "/favorites",
        SHOPPING_LIST: "/shopping-list"
    },
    API: {
        RECIPES: "/api/recipes",
        RECIPE_ID: (id: string) => `/api/recipes/${id}`,
        CREATE_RECIPE: "/api/recipes",
        UPDATE_RECIPE: (id: string) => `/api/recipes/${id}`,
        AUTH_USER: "/api/auth/me",
        GITHUB_AUTH: "/oauth2/authorization/github"
    }
}
