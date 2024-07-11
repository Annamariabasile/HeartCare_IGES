import React, { useEffect, useState } from "react";
import VisitaCard from "../components/VisitaCard";
import NoteContainer from "../components/NoteContainer";
import "../css/style.css";
import "../css/home-main-content.css";
import "../css/homeMedico_style.css";
import { useNavigate } from "react-router";
import { Navigate } from "react-router-dom";
import ListaVisita from "../components/ListaVisita";
import jwt from "jwt-decode"
import SockJS from 'sockjs-client';
import {Stomp} from "@stomp/stompjs"
import addNotification from 'react-push-notification';
import NoteContainerCaregiver from "../components/NoteContainerCaregiver";
import VisiteCaregiverContainer from "../components/VisiteCaregiverContainer";


function HomeCaregiver() {
    const [utente, setUtente] = useState([]);
    const [note, setNote] = useState([]);
    const [visite, setVisite] = useState([]);
    let nav = useNavigate();
    const token = localStorage.getItem("token");

    let config = {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        withCredentials: true,
        Authorization: `Bearer ${token}`,
        "Content-Type" : "application/json"
    };


    useEffect( () => {
        const fetchHome = async () => {
            try {
                const response = await fetch("http://localhost:8080/Home/"+jwt(token).id, {
                    method : "POST",
                    headers : config,
                }).then(response => response.json());
                setUtente(response);
                console.log("Ciao: "+utente)
            } catch (error) {
                console.error(error.message);
            }
        };

        const fetchNote = async () => {
            const response = await fetch("http://localhost:8080/comunicazione/getNoteCaregiver", {
                method : "POST",
                headers : config,
                body: JSON.stringify({
                    id: jwt(token).id
                })
            }).then(response => {
                console.log(response)
                return response.json();
            })
            setNote(response);
        }

        const fetchVisite = async () => {
            const response = await fetch("http://localhost:8080/visite/getVisiteCaregiver",{
                method : "POST",
                headers : config,
                body: JSON.stringify({
                    id: jwt(token).id
                })
            }).then(response =>{
                return response.json();
            })
            setVisite(response);
        }

        fetchHome();
        fetchNote();
        fetchVisite();
    }, [])

    useEffect(() => {
        if(utente.length > 0) {

        }
    }, [utente])

    return  (
        <div className="contenitoreMainContent-Home">
            {
                (utente["sesso"] === "M") ? <span className="testo-bentornat">Bentornato, Caregiver {utente["cognome"]} 👋🏻</span> : <span className="testo-bentornat">Bentornata, Drs. {utente["cognome"]} 👋🏻</span>
            }

            <div className="full-container">
                <div className="container-sinistra">
                    <div className="banner">
                        <div className="blocco-testo-banner">
                            <span className="testo-banner" >Pazienti totali</span>
                            <span className="testo-banner-numero">{utente.pazientiTotali}</span>
                        </div>

                        <div className="blocco-testo-banner">
                            <span className="testo-banner">Appuntamenti in programma</span>
                            <span className="testo-banner-numero">{visite.length}</span>
                        </div>

                        <div className="blocco-testo-banner">
                            <span className="testo-banner">Nuove note</span>
                            <span className="testo-banner-numero">{note.length}</span>
                        </div>
                    </div>

                    <div className="visite-container">
            <span className="visite-in-programma">
              Le prossime visite in programma
            </span>

                        <div className="box-visite box-visiteHomeMedico">
                            <VisiteCaregiverContainer classe="cardPazienteHomeMedico" ruolo={utente["ruolo"]}/>
                        </div>
                    </div>
                </div>
                <div className="note-container" >
                    <NoteContainerCaregiver />
                </div>
            </div>
        </div>
    );
}

export default HomeCaregiver;