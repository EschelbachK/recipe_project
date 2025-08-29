// src/router/Router.tsx
import {Routes, Route, Navigate} from "react-router-dom"
import RecipesOverviewPage from "../pages/RecipesOverviewPage"
import RecipeEditPage from "../pages/RecipeEditPage"
import RecipeDetailPage from "../pages/RecipeDetailPage"
import LoginPage from "../pages/LoginPage"
import ProtectedRoute from "./ProtectedRoute"
import type {AppUser} from "../types/types"
import {routerConfig} from "./routerConfig"

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
            </Route>
            <Route path="*" element={<Navigate to={routerConfig.URL.RECIPES} replace/>}/>
        </Routes>
    )
}
