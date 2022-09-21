import React from "react"

import { Navbar } from "./components/Navbar"
import {ShopPanel} from "./components/ShopPanel";
import {Login} from "./components/Login";
import {useCookies} from "react-cookie";

function App() {
    const [cookies] = useCookies();
    return (
      <div className='App'>
          <Navbar name={cookies.name} lastname={cookies.lastname}/>
          <ShopPanel/>
          {!cookies.name && !cookies.lastname && <Login/>}
      </div>
  );
}

export default App;
