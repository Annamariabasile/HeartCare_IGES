import React, {useEffect} from "react";
import '../css/RegistrazioneStyle.css';
import { useState } from "react";
import { useForm } from "react-hook-form";
import Modal from "react-responsive-modal";
import { Navigate } from "react-router";
import moment from 'moment';
import jwt from "jwt-decode";




function RegistrazioneCaregiverForm() {
    const [nome, setNome] = useState("");
    const [cognome, setCognome] = useState("");
    const [nTelefono, setNtelefono] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [confermaPassword, setConfermaPassword] = useState("");
    const [genere, setGenere] = useState("");
    const [dataNascita, setDataNascita] = useState("");
    const [codiceFiscale, setCodiceFiscale] = useState("");
    let params = new URLSearchParams(window.location.search);
    let idPaziente = params.get('idPaziente');

    const aggiornaNome = (event) => {
        setNome(event.target.value);
    }

    const aggiornaCognome = (event) => {
        setCognome(event.target.value);
    }

    const aggiornaEmail = (event) => {
        setEmail(event.target.value);
    }

    const aggiornaNtelefono = (event) => {
        setNtelefono(event.target.value);
    }

    const aggiornaPassword = (event) => {
        setPassword(event.target.value);
    }

    const aggiornaConfermaPassword = (event) => {
        setConfermaPassword(event.target.value);
    }

    const aggiornaGenere = (event) => {
        setGenere(event.target.value);
    }

    const aggiornaDataNascita = (event) => {
        setDataNascita(event.target.value);
    }

    const aggiornaCodiceFiscale = (event) => {

        setCodiceFiscale(event.target.value);
    }

    const [openErrore, setOpenErrore] = useState(false);
    const onOpenModalErrore = () => setOpenErrore(true);
    const onCloseModalErrore = () => setOpenErrore(false);

    const { register, formState: { errors }, handleSubmit } = useForm();

    const onSubmit = (data) => fetch('http://localhost:8080/auth/registrazioneCaregiver', {
        method: "POST",// *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, *same-origin, omit
        headers: {
            "Content-Type": "application/json"
        },
        body:JSON.stringify({
            nome: data.nome,
            cognome: data.cognome,
            password: data.password,
            email: email,
            numeroTelefono: data.numeroTelefono,
            confermaPassword: data.confermaPassword,
            dataDiNascita: data.dataDiNascita,
            genere: data.genere,
            codiceFiscale: data.codiceFiscale,
            idPaziente:idPaziente
        })
    }).then(response =>{
        if(response.status != 200){
            onOpenModalErrore();
        }else{
            window.location.href = "/"
        }
    })

    const getCaregiver = async () => {
        return await fetch("http://localhost:8080/getCaregiver/"+idPaziente, {
            method: "POST", // *GET, POST, PUT, DELETE, etc.
            mode: "cors", // no-cors, *cors, same-origin
            cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
            credentials: "same-origin", // include, *same-origin, omit
            redirect: "follow", // manual, *follow, error
            referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
            //body: JSON.stringify(data), // body data type must match "Content-Type" header
        }).then(async (response) => {
            response = await response.json()
            setEmail(response.email);
        });
    };useEffect(() => {
        getCaregiver();
    }, []);

    return (
        <div className="contenitoreRegistrazioneForm">
            <span className="testo-bentornat max-width">I tuoi dati</span>
            <div className="contenitoreEditTextCorta">
                <span className="labelEditText">Nome</span>
                <input type="text" placeholder="Mario" className="registrazioneEditText" onChange={aggiornaNome} {...register("nome", { required: true })} />
                <error className="errore">
                    {errors.nome?.type === "required" && "Name is required"}
                </error>
            </div>
            <div className="contenitoreEditTextCorta">
                <span className="labelEditText">Cognome</span>
                <input type="text" placeholder="Rossi" className="registrazioneEditText" onChange={aggiornaCognome} {...register("cognome", { required: true })} />
                <error className="errore">
                    {errors.cognome?.type === "required" && "Cognome is required"}
                </error>
            </div>
            <div className="contenitoreEditTextCorta">
                <span className="labelEditText">Numero di telefono</span>
                <input type="text" placeholder="333333333" className="registrazioneEditText" onChange={aggiornaNtelefono} {...register("numeroTelefono", {
                    required: true,
                    minLength: 13,
                    maxLength: 13,
                    pattern: /^((00|\+)39[\. ]??)??3\d{2}[\. ]??\d{6,7}$/
                })} />
                <error className="errore">
                    {errors.numeroTelefono?.type === "minLength" &&
                        "Entered number is less than 13 digits"}
                    {errors.number?.type === "maxLength" &&
                        "Entered number is more than 13 digits"}
                    {errors.numeroTelefono?.type === "required" && "Inserire numero telefono"}
                    {errors.numeroTelefono?.type === "pattern" && "Pattern non corretto"}
                </error>
            </div>
            <div className="contenitoreEditTextCortissima">
                <span className="labelEditText">Genere</span>
                <input type="text" placeholder="M|F" className="registrazioneEditText" onChange={aggiornaGenere} {...register("genere", {
                    required: true,
                    pattern: /^M$|^F$/
                })} />
                <error className="errore">
                    {errors.genere?.type === "required" && "Inserire genere"}
                    {errors.genere?.type === "pattern" && "Inserire correttamente"}
                </error>
            </div>
            <div className="contenitoreEditTextCorta">
                <span className="labelEditText">Data di nascita</span>
                <input type="date" placeholder="Data Nascita" className="registrazioneEditText" max={moment().format("YYYY-MM-DD")} onChange={aggiornaDataNascita}{...register("dataDiNascita", {
                    required: true,
                    validate: (value) => {
                        return value < moment().format("YYYY-MM-DD");
                    }
                })} />
                <error className="errore">
                    {errors.dataNascita?.type === "required" && "Inserire data nascita"}
                    {errors.dataDiNascita?.type === "validate" && "Data troppo grande"}
                </error>
            </div>
            <div className="contenitoreEditTextCorta">
                <span className="labelEditText">Codice Fiscale</span>
                <input id="codiceFiscale" type="text" placeholder="Codice Fiscale" className="registrazioneEditText" onChange={aggiornaCodiceFiscale}{...register("codiceFiscale", {
                    required: true,
                    pattern: /^[A-Z]{6}[A-Z0-9]{2}[A-Z][A-Z0-9]{2}[A-Z][A-Z0-9]{3}[A-Z]$/,
                    validate: async (value) => {
                        let approva = undefined;
                        const data = await fetch('http://localhost:8080/auth/getByCodice', {
                            method: "POST",// *GET, POST, PUT, DELETE, etc.
                            mode: "cors", // no-cors, *cors, same-origin
                            cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
                            credentials: "same-origin", // include, *same-origin, omit
                            headers: {

                                "Content-Type": "application/json"
                            },
                            body:JSON.stringify({
                                codiceFiscale:value
                            })

                        }).then(async response =>{
                            approva = await response.json();

                        })
                        return !approva
                    }
                })} />
                <error className="errore">
                    {errors.codiceFiscale?.type === "required" && "Inserire codice fiscale"}
                    {errors.codiceFiscale?.type === "pattern" && "Inserire correttamente il codice fiscale"}
                    {errors.codiceFiscale?.type === "validate" && "Codice fiscale già presente"}

                </error>
            </div>
            <div className="contenitoreEditTextLunga">
                <span className="labelEditText">E-mail</span>
                <input type="text" className="registrazioneEditText" value={email} readOnly={true}/>
            </div>
            <div className="contenitoreEditTextCorta">
                <span className="labelEditText">Password</span>
                <input id="password" type="password" placeholder="Password" className="registrazioneEditText" onChange={aggiornaPassword}
                       {...register("password", {
                           required: true,
                           pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?^#()<>+&.])[A-Za-z\d@$!%*?^#()<>+&.]{8,16}$/i,
                       })} />
                <error className="errore">
                    {errors.password?.type === "required" && "Inserire password"}
                    {errors.password?.type === "pattern" &&
                        "Password non rispetta il formato almeno 8 caratteri una maiuscola un simbolo e un numero"}
                </error>
            </div>
            <div className="contenitoreEditTextCorta">
                <span className="labelEditText">Conferma password</span>
                <input type="password" placeholder="Conferma password" className="registrazioneEditText" onChange={aggiornaConfermaPassword}
                       {...register("confermaPassword", {
                           required: true,
                           validate: (value) => {
                               const pass = document.getElementById("password").value;

                               return value == pass
                           }
                       })} />
                <error className="errore">
                    {errors.confermaPassword?.type === "required" && "Inserire conferma password"}
                    {errors.confermaPassword?.type === "validate" && "Le password non corrispondono"}
                </error>
            </div>
            <hr className="lineaMenu" />
            <button className="formButton" onClick={handleSubmit(onSubmit)}>Registrati</button>
            <Modal open={openErrore} onClose={onCloseModalErrore} center>
                <h2>Registrazione non andata a buon fine controlla i parametri.</h2>
            </Modal>
        </div>
    );
}
export default RegistrazioneCaregiverForm;