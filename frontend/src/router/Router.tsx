import {Routes, Route, Navigate, Outlet} from "react-router-dom"
import RecipesOverviewPage from "../pages/RecipesOverviewPage"
import RecipeEditPage from "../pages/RecipeEditPage"
import RecipeDetailPage from "../pages/RecipeDetailPage"
import LoginPage from "../pages/LoginPage"
import ProtectedRoute from "./ProtectedRoute"
import type {AppUser} from "../types/types"
import {routerConfig} from "./routerConfig"
import FavoritesPage from "../pages/FavoritesPage"
import ShoppingListPage from "../pages/ShoppingListPage"
import Sidebar from "../components/Sidebar"

export function Router({user}: Readonly<{ user: AppUser | null | undefined }>) {
    return (
        <Routes>
            <Route path={routerConfig.URL.LOGIN} element={<LoginPage/>}/>

            <Route element={<ProtectedRoute user={user}/>}>
                <Route element={
                    <div style={{display: "flex"}}>
                        <Sidebar/>
                        <div style={{flex: 1, padding: "1rem"}}>
                            <Outlet/>
                        </div>
                    </div>
                }>
                    <Route path={routerConfig.URL.HOME} element={<Navigate to={routerConfig.URL.RECIPES} replace/>}/>
                    <Route path={routerConfig.URL.RECIPES} element={<RecipesOverviewPage/>}/>
                    <Route path={routerConfig.URL.RECIPE_NEW} element={<RecipeEditPage/>}/>
                    <Route path={routerConfig.URL.RECIPE_EDIT} element={<RecipeEditPage/>}/>
                    <Route path={routerConfig.URL.RECIPE_DETAIL} element={<RecipeDetailPage/>}/>
                    <Route path={routerConfig.URL.FAVORITES} element={<FavoritesPage/>}/>
                    <Route path={routerConfig.URL.SHOPPING_LIST} element={<ShoppingListPage/>}/>
                </Route>
            </Route>

            <Route path="*" element={<Navigate to={routerConfig.URL.RECIPES} replace/>}/>
        </Routes>
    )
}