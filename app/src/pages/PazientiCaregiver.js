import React, {useEffect, useState} from "react";
import "../css/style.css";
import "../css/PazientiCss.css";
import ListaPazienti from "../components/ListaPazienti";
import HamburgerMenu from "../components/HamburgerMenu";
import { Chart } from "react-google-charts";
import jwt from "jwt-decode";
import { FaTrashRestore } from "react-icons/fa";
import ListaPazientiCaregiver from "../components/ListaPazientiCaregiver";
function PazientiCaregiver() {
    const [utente, setUtente] = useState([]);
    const token = localStorage.getItem("token");
    const [testo,setTesto] = useState(" ");

    let config = {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        withCredentials: true,
        Authorization: `Bearer ${token}`,
        "Content-Type" : "application/json"
    };

    const fetchHome = async () => {
        try {
            const response = await fetch("http://localhost:8080/Home/"+jwt(token).id, {
                method : "POST",
                headers : config,
            }).then(response => response.json());
            setUtente(response);

        } catch (error) {
            console.error(error.message);
        }
    };

    useEffect( () => {
        fetchHome();
    }, [])

    useEffect(() => {
        if(utente.length > 0) {

        }
    }, [utente])


    return (
        <div className="contenitorePazientiContent">
            <div className="searchbar">
                <input id="search" type="text" placeholder=" 🔍Cerca Paziente..."  onChange={(e) => setTesto(e.target.value)}/>
            </div>
            {
                (utente["sesso"] === "M") ? <span className="bentornato">Bentornato, Sig. {utente["cognome"]} 👋🏻</span> : <span className="bentornato">Bentornata, Sig.ra {utente["cognome"]} 👋🏻</span>
            }
            <span className="iTuoiPazienti">I tuoi pazienti: </span>
            <ListaPazientiCaregiver txt={testo} ruolo={utente["ruolo"]} />
        </div>
    );
}

export default PazientiCaregiver;
