type RouterConfig = {
    URL: URL
    API: API
}

type URL = {
    HOME: string
    RECIPES: string
    RECIPE_NEW: string
    RECIPE_EDIT: string
    RECIPE_ID: (id: string) => string
    RECIPE_EDIT_ID: (id: string) => string
    LOGIN: string
}

type API = {
    RECIPES: string
    RECIPE_ID: (id: string) => string
    AUTH_USER: string
    GITHUB_AUTH: string
}

export const routerConfig: RouterConfig = {
    URL: {
        HOME: "/",
        RECIPES: "/recipes",
        RECIPE_NEW: "/recipes/new",
        RECIPE_EDIT: "/recipes/:id/edit",
        RECIPE_ID: (id: string) => `/recipes/${id}`,
        RECIPE_EDIT_ID: (id: string) => `/recipes/${id}/edit`,
        LOGIN: "/login"
    },
    API: {
        RECIPES: "/api/recipes",
        RECIPE_ID: (id: string) => `/api/recipes/${id}`,
        AUTH_USER: "/api/auth/me",
        GITHUB_AUTH: "/oauth2/authorization/github"
    }
}
