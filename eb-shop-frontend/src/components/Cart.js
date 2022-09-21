import React from "react"
import {useCart} from "../context/Cart";
import {addProduct, deleteProduct, emptyCart} from "../context/CartReducer";

export const Cart = () => {

    const {state, dispatch} = useCart()
    const products = state.products

    const totalPrice = products.reduce(
        (prevValue, currentValue) => prevValue + currentValue.quantity * currentValue.item.price, 0);
    const itemPrices = products.map(product => product.item.price * product.quantity)

    return (
        <main>
            <section>
                <div className="spacer">
                    <div className="container">
                        <div className="row mt-5">
                            <div className="col-lg-9">
                                <div className="row shop-listing">
                                    <table className="table shop-table">
                                        <tbody>
                                        <tr>
                                            <th className="b-0">Name</th>
                                            <th className="b-0">Price</th>
                                            <th className="b-0">Quantity</th>
                                            <th className="b-0">Sum</th>
                                        </tr>
                                        {products.map((product, i) => (
                                            <tr>
                                                <td>{product.item.name}</td>
                                                <td>{product.item.price}</td>
                                                <td>{product.quantity}</td>
                                                <td>{itemPrices[i]}</td>
                                                <td>
                                                    <button
                                                        onClick={(e) => dispatch(addProduct(product.item))}
                                                        className="btn btn-primary btn-sm btn-success">+
                                                    </button>
                                                </td>
                                                <td>
                                                    <button onClick={(e => dispatch(deleteProduct(product, 1)))}
                                                            className="btn btn-primary btn-sm btn-success">-</button>
                                                </td>
                                                <td>
                                                    <button onClick={() => dispatch(deleteProduct(product, product.quantity))}
                                                            className="btn btn-primary btn-sm btn-danger">Remove item
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                        <tr>
                                            <td colspan="3" align="right">
                                                Total price: {totalPrice}
                                            </td>
                                            <td colSpan="4" align="right">
                                                <button
                                                    className="btn btn-success ml-2"
                                                    onClick={(e) => dispatch(emptyCart())}>
                                                    Order
                                                </button>
                                                <button
                                                    className="btn btn-danger ml-2"
                                                    onClick={(e) => dispatch(emptyCart())}>
                                                    Empty
                                                </button>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    );
}