import React, { useEffect } from "react";
import '../css/styleModificaDatiForm.css';
import { useState } from "react";
import jwt from "jwt-decode";
import { ReactSession }  from 'react-client-session';
import userPath from "../images/user.png";
import { Modal } from 'react-responsive-modal';

function ModificaDatiForm(){
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([])
    const token = localStorage.getItem("token");
    const id = jwt(token).id;
    let config = {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        withCredentials: true,
        Authorization: `Bearer ${token}`,
    };

    const getDati = async () => {
        return await fetch("http://localhost:8080/utente/modifica/"+id, {
            method: "POST", // *GET, POST, PUT, DELETE, etc.
            mode: "cors", // no-cors, *cors, same-origin
            cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
            credentials: "same-origin", // include, *same-origin, omit
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            redirect: "follow", // manual, *follow, error
            referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
            //body: JSON.stringify(data), // body data type must match "Content-Type" header
        }).then(async (response) => {
            response = await response.json()
            setData(response);
            console.log(data);
        });
    };

    useEffect(()=>{
        getDati();
    },[])

    const [vecchiaPassword, setVecchiaPassword] = useState("");
    const [nuovaPassword, setNuovaPassword] = useState("");
    const [nTelefono, setNtelefono] = useState("");
    const [password, setPassword] = useState("");
    const [confermaPassword, setConfermaPassword] = useState("");
    const [nomeCaregiver, setNomeCaregiver] = useState("");
    const [cognomeCaregiver, setCognomeCaregiver] = useState("");
    const [emailCaregiver, setEmailCaregiver] = useState("");
    const [citta, setCitta] = useState("");
    const [provincia, setProvincia] = useState("");
    const [via, setVia] = useState("");
    const [numeroCivico, setNumeroCivico] = useState("");
    const [cap, setCap] = useState("");


    const aggiornaVecchiaPassowrd = (event) => {
        setVecchiaPassword(event.target.value);
    }

    const aggiornaNuovaPassword = (event) => {
        setNuovaPassword(event.target.value);
    }

    const aggiornaConfermaPassword = (event) => {
        setConfermaPassword(event.target.value);
    }

    const aggiornaNomeCaregiver = (event) => {
        setNomeCaregiver(event.target.value);
    }

    const aggiornaCognomeCaregiver = (event) => {
        setCognomeCaregiver(event.target.value);
    }

    const aggiornaEmailCaregiver = (event) => {
        setEmailCaregiver(event.target.value);
    }

    const aggiornaCitta = (event) => {
        setCitta(event.target.value);
    }

    const aggiornaProvincia = (event) => {
        setProvincia(event.target.value);
    }

    const aggiornaVia = (event) => {
        setVia(event.target.value);
    }

    const aggiornaNumeroCivico = (event) => {
        setNumeroCivico(event.target.value);
    }

    const aggiornaCap = (event) => {
        setCap(event.target.value);
    }

    const controllaPassword = (()=>{
        const nuova = document.getElementById("nuovaPassword").value;
        const conferma = document.getElementById("confermaPassword").value;

        return conferma==nuova;

    })

    const [open, setOpen] = useState(false);
    const [openConferma, setOpenConferma] = useState(false);

    const onOpenModal = () => setOpen(true);
    const onCloseModal = () => setOpen(false);
    const onOpenModalConferma = () => setOpenConferma(true);
    const onCloseModalConfermate = () => {
        document.location.reload();
        setOpenConferma(false)
    };

    const modificaPassword =  async ()=>{
        if(controllaPassword){
            document.getElementById("errore").style.display="none";
            return await fetch("http://localhost:8080/modifica/password",{
                method: "POST", // *GET, POST, PUT, DELETE, etc.
                mode: "cors", // no-cors, *cors, same-origin
                cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
                credentials: "same-origin", // include, *same-origin, omit
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                    // 'Content-Type': 'application/x-www-form-urlencoded',
                },
                body:JSON.stringify({
                    id:id,
                    vecchiaPassword:vecchiaPassword,
                    nuovaPassword:nuovaPassword
                })
            }).then(response => {

                if(response.status != 200){
                    onOpenModal();
                }else{
                    onOpenModalConferma();
                }
            })
        }else{
            document.getElementById("errore").style.display="block";
        }
    }



    const modificaIndirizzo = () =>{
        fetch("http://localhost:8080/modifica/indirizzo",{
            method: "POST", // *GET, POST, PUT, DELETE, etc.
            mode: "cors", // no-cors, *cors, same-origin
            cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
            credentials: "same-origin", // include, *same-origin, omit
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            body:JSON.stringify({
                id:id,
                citta:citta,
                provincia:provincia,
                via:via,
                cap:cap,
                numeroCivico:numeroCivico
            })
        }).then(response => {
            if(response.status != 200){
                onOpenModal();
            }else{
                onOpenModalConferma();
            }
        })
    }


    return (
        <div className="contenitoreFormGenerale">
            <Modal open={open} onClose={onCloseModal} center>
                <h2>Modifiche non andate a buon fine</h2>
            </Modal>
            <Modal open={openConferma} onClose={onCloseModalConfermate} center>
                <h2>Modifiche apportate correttamente</h2>
            </Modal>
            <div className="contenitoreForm-infoPersonali">
                <h1 className="titoloDati">Dati personali 👨🏻‍💻 </h1>
                <hr className="lineaMenuProfilo" />
                <img src={userPath} className="userImg"/>
                <input type="password"   placeholder="Vecchia password" className="formEditText" onChange={aggiornaVecchiaPassowrd}/>
                <input type="password" id = "nuovaPassword" placeholder="Nuova password" className="formEditText" onChange={aggiornaNuovaPassword}/>
                <input type="password" id = "confermaPassword" placeholder="Conferma nuova password" className="formEditText" onChange={aggiornaConfermaPassword}
                />
                <span id = "errore"style={{display:"none"}}>Le password non corrispondono</span>
                <button className="formButton" onClick={modificaPassword}>Salva</button>

            </div>
            <div className="contenitoreForm-Secondario">
                <div className="contenitoreForm-Indirizzo">
                    <h1 className="titoloDati">Indirizzo 🏡</h1>
                    <hr className="lineaMenuProfilo" />
                    <input type="text" placeholder={data.citta} className="formEditText"  onChange={aggiornaCitta}></input>
                    <input type="text" placeholder={data.provincia} className="formEditText" maxLength={2}  onChange={aggiornaProvincia}></input>
                    <input type="text" placeholder={data.via} className="formEditText" onChange={aggiornaVia}></input>
                    <input type="text" placeholder={data.numeroCivico} className="formEditText" onChange={aggiornaNumeroCivico}></input>
                    <input type="number" placeholder={data.cap} className="formEditText" maxLength={5}  onChange={aggiornaCap}></input>
                    <button className="formButton" onClick={modificaIndirizzo}>Salva</button>
                </div>
            </div>
        </div>
    );
}

export default ModificaDatiForm;