import React, {useState, useEffect} from "react"
import {useCart} from "../context/Cart";
import {addProduct} from "../context/CartReducer";
import {HOSTNAME} from "../const/Hostname";

export const Products = (url) => {
    const [products, setProducts] = useState([]);
    const {dispatch} = useCart();

    useEffect(() => {
        async function fetchData() {
            console.log(HOSTNAME)
            const res = await fetch(HOSTNAME.concat(url.url));
            res.json().then((data) => {
                console.log(data)
                setProducts(data)
            }).catch((error) => {
                console.log(error)
            })
        }
        fetchData();
    }, [url]);

    return (
        <main>
            <section>
                <div className="spacer">
                    <div className="container">
                        <div className="row mt-5">
                            <div className="col-lg-9">
                                <div className="row shop-listing">
                                    {products.map((product, i) => (
                                        <div className="col-lg-4">
                                            <div className="card border-0">
                                                <h6>
                                                    {product.name}
                                                </h6>
                                                <h5 className="font-medium m-b-30">
                                                    ${product.price} /{" "}
                                                     <del className="text-muted line-through">${Math.round(1.1 * product.price)}</del>
                                                </h5>
                                            </div>
                                            <button
                                                onClick={() => {dispatch(addProduct(product))}}
                                                className="btn btn-md btn-success">
                                                Add to cart
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    );
}
