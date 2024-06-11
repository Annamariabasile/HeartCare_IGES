import React, { useEffect } from "react";
import '../css/styleModificaDatiForm.css';
import { useState } from "react";
import jwt from "jwt-decode";
import { ReactSession }  from 'react-client-session';
import userPath from "../images/user.png";
import { Modal } from 'react-responsive-modal';

function ModificaDatiFormCaregiver(){
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
    const [password, setPassword] = useState("");
    const [confermaPassword, setConfermaPassword] = useState("");


    const aggiornaVecchiaPassowrd = (event) => {
        setVecchiaPassword(event.target.value);
    }

    const aggiornaNuovaPassword = (event) => {
        setNuovaPassword(event.target.value);
    }

    const aggiornaConfermaPassword = (event) => {
        setConfermaPassword(event.target.value);
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
                <input type="password" id = "confermaPassword" placeholder="Conferma nuova password" className="formEditText" onChange={aggiornaConfermaPassword}/>
                <span id = "errore"style={{display:"none"}}>Le password non corrispondono</span>
                <button className="formButton" onClick={modificaPassword}>Salva</button>
            </div>
        </div>
    );
}

export default ModificaDatiFormCaregiver;