import React from 'react';
import {HOSTNAME} from "../const/Hostname";

export const Login = () => {
    return (
        <div className="container pt-4">
            <p className="m-t-0">Login with one of the services</p>
            <section>
                <a className="btn btn-outline-primary ml-2" href={HOSTNAME + '/authenticate/facebook'} role="button">FACEBOOK</a>
                <a className="btn btn-outline-success ml-2" href={HOSTNAME + '/authenticate/google'} role="button">GOOGLE</a>
            </section>
        </div>
    );
}