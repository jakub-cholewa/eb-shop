import React from "react"
import {Link} from "react-router-dom"
import {CATEGORIES} from "../const/Categories";
import {HOSTNAME} from "../const/Hostname";

export const Navbar = (props) => {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-success">
            <Link to="/" className="navbar-brand"> YourFurnitures < /Link>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav">
                        {CATEGORIES.map(category => (
                            <li className="nav-item">
                                <Link to={category.url} className="nav-link">{category.name}</Link>
                            </li>
                        ))}
                    </ul>
                </div>
            {props.name && props.lastname && <div className="collapse navbar-collapse navbar-expand-lg" id="navbarNav">
                <ul className="navbar-nav nav-fill">
                    <li className="nav-item">
                        <Link to="/cart" className="nav-link">Cart</Link>
                    </li>
                    <li className="nav-item">
                        <span className="nav-link active">{props.name} {props.lastname}</span>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href={HOSTNAME.concat("/signout")}>Logout</a>
                    </li>
                </ul>
            </div>}
        </nav>
    )
}