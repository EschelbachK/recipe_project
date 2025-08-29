import {Routes, Route, Navigate} from "react-router-dom"
import RecipesOverviewPage from "../pages/RecipesOverviewPage"
import RecipeEditPage from "../pages/RecipeEditPage"
import RecipeDetailPage from "../pages/RecipeDetailPage"
import LoginPage from "../pages/LoginPage"
import ProtectedRoute from "./ProtectedRoute"
import type {AppUser} from "../types/types"
import {routerConfig} from "./routerConfig"
import FavoritesPage from "../pages/FavoritesPage"
import ShoppingListPage from "../pages/ShoppingListPage"

export function Router({user}: Readonly<{ user: AppUser | null | undefined }>) {
    return (
        <Routes>
            <Route path={routerConfig.URL.LOGIN} element={<LoginPage/>}/>
            <Route element={<ProtectedRoute user={user}/>}>
                <Route path={routerConfig.URL.HOME} element={<Navigate to={routerConfig.URL.RECIPES} replace/>}/>
                <Route path={routerConfig.URL.RECIPES} element={<RecipesOverviewPage/>}/>
                <Route path={routerConfig.URL.RECIPE_NEW} element={<RecipeEditPage/>}/>
                <Route path={routerConfig.URL.RECIPE_EDIT} element={<RecipeEditPage/>}/>
                <Route path={routerConfig.URL.RECIPE_ID(":id")} element={<RecipeDetailPage/>}/>
                <Route path={routerConfig.URL.FAVORITES} element={<FavoritesPage/>}/>
                <Route path={routerConfig.URL.SHOPPING_LIST} element={<ShoppingListPage/>}/>
            </Route>
            <Route path="*" element={<Navigate to={routerConfig.URL.RECIPES} replace/>}/>
        </Routes>
    )
}
