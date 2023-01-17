import React from "react";
import '../css/style.css';
import { useState } from "react";
import axios from "axios";
import { ReactSession }  from 'react-client-session';




function LoginForm(){
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const aggiornaEmail = (event) => {
        setEmail(event.target.value);
    }

    const aggiornaPassword = (event) => {
        setPassword(event.target.value);
    }

    const handleSubmit = (event) => {
        event.preventDefault()


        axios.post('http://localhost:8080/login', {
            email: email,
            password: password
        })
            .then((response) => {
                console.log(ReactSession.set("utenteLoggato"));
                console.log(response);
            }, (error) => {
                console.log(error);
            });
    }


    return (
        <div className="contenitoreForm">
            <input type="text" placeholder=" E-mail" className="formEditText" onChange={aggiornaEmail}/>
            <input type="password" placeholder=" Password" className="formEditText" onChange={aggiornaPassword}/>
            <span className="formLink">Ho dimenticato la password</span>
            <button className="formButton" onClick={handleSubmit}>Accedi</button>
            <span className="formLink centerFlexItem">Non hai un account? Registrati</span>
        </div>
    );
}
export default LoginForm;