export const ADD_PRODUCT = 'ADD_PRODUCT';
export const DELETE_PRODUCT = 'DELETE_PRODUCT';
export const EMPTY_CART = 'EMPTY_CART';

export const addProduct = (product) => ({
    type: ADD_PRODUCT,
    product
});
export const deleteProduct = (product, quantity) => ({
    type: DELETE_PRODUCT,
    product,
    quantity
});
export const emptyCart = () => ({
    type: EMPTY_CART
});

function handleAddingProduct(state, newProduct) {
    const productFoundIndex = state.products.findIndex(p => p.item.id === newProduct.id && p.item.name === newProduct.name)

    if (productFoundIndex !== -1) {
        const productFound = state.products[productFoundIndex]
        return {
            ...state,
            products: state.products.filter(p => !(p.item.id === productFound.item.id && p.item.name === productFound.item.name))
                .concat({item: newProduct, quantity: productFound.quantity + 1})
                .sort(((a, b) => a.item.name > b.item.name ? 1 : -1))
        }
    } else {
        return {
            ...state,
            products: state.products.concat({item: newProduct, quantity: 1})
                .sort(((a, b) => a.item.name > b.item.name ? 1 : -1))
        }
    }
}

function handleDeletingProduct(state, product, productQuantity) {
    const {item, quantity} = product
    if (quantity === 1 || (quantity === productQuantity)) {
        return {...state,
            products:state.products.filter(p => !(p.item.id === item.id && p.item.name === item.name))};
    }
    else {
        return {
            ...state,
            products: state.products.filter(p => !(p.item.id === item.id && p.item.name === item.name))
                .concat({item: item, quantity: quantity - 1})
                .sort(((a, b) => a.item.name > b.item.name ? 1 : -1))
        }
    }
}

export const cartReducer = (state, action) => {
    switch (action.type) {
        case ADD_PRODUCT:
            return handleAddingProduct(state, action.product)
        case DELETE_PRODUCT:
            return handleDeletingProduct(state, action.product, action.quantity)
        case EMPTY_CART:
            return {...state,
                products:[]}
        default:
            return state;
    }
}

