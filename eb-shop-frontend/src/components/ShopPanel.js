import React from "react"
import {Route, Switch} from "react-router-dom";
import {Products} from "./Product";
import {Cart} from "./Cart";
import {CATEGORIES} from "../const/Categories";

export const ShopPanel = () => {
    return (
        //Switch - renderuje tylko pierwszy z Route
        <Switch>
            {CATEGORIES.map(category => (
                <Route exact path = {category.url}>
                    <Products url={category.url}/>
                </Route>
            ))}
            <Route exact path ="/cart" component={Cart}/>
        </Switch>
    );
}