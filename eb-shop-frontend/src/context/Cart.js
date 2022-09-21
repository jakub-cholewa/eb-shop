import React from 'react';
import {cartReducer} from "./CartReducer";

const Cart = React.createContext();
export const useCart = () => React.useContext(Cart);

export const CartProvider = ({children}) => {

    const [state, dispatch] = React.useReducer(cartReducer, {products: [],
    });

    return (
        <Cart.Provider value={{state, dispatch}}>{children}</Cart.Provider>
    )
}