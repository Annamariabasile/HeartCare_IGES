import React from "react";
import "../css/style.css";
import "../css/PazientiCss.css";
import axios from "axios";
import {useState, useEffect} from "react";
import CardPaziente from "./CardPaziente";
import CardUtente from "./CardUtente";
import CardAdminMedico from "./CardAdminMedico";
import "../css/HomeAdmin.css"
import CardAdminCaregiver from "./CardAdminCaregiverNonRegistrato";
import CardAdminCaregiverNonRegistrato from "./CardAdminCaregiverNonRegistrato";

function ListaUtenti(props) {

    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const token = localStorage.getItem("token");

    let config = {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        withCredentials: true,
        Authorization: `Bearer ${token}`,
        "Content-Type" : "application/json"
    };

    const fetchData = async () =>{
        setLoading(true);
        try {
            const response = await fetch("http://localhost:8080/searchBarAdmin",{
                method : "POST",
                headers : config,
                body : JSON.stringify({
                    txt : props.text
                })
            }).then(response => response.json());
            setData(response);
        } catch (error) {
            console.error(error.message);
        }
        setLoading(false);
    }

    useEffect(() => {
        fetchData();
    }, []);

    useEffect( () => {
        fetchData();
    },[props.text])



    return(

        <div>
            <br/><br/><span className="iTuoiUtenti">I Pazienti :</span><br/><br/>
            <div className="contenitoreCardPazienti">

                {data.map(function (medico, idx) {
                    if (medico.ruolo == "PAZIENTE")
                        return (
                            <CardUtente key={idx} idUtente={medico.id} nomeUtente={medico.nome}
                                        cognomeUtente={medico.cognome} dataNascita={medico.dataDiNascita}
                                        genere={medico.genere} numero={medico.numeroTelefono} email={medico.email}/>
                        )
                })}

            </div>
            <br/><br/><span className="iTuoiUtenti">I Medici :</span><br/><br/>
            <div className="contenitoreCardPazienti">
                {data.map(function (medico, idx) {
                    if (medico.ruolo == "MEDICO")
                        return (
                            <CardAdminMedico key={idx} idUtente={medico.id} nomeUtente={medico.nome}
                                             cognomeUtente={medico.cognome} dataNascita={medico.dataDiNascita}
                                             genere={medico.genere} numero={medico.numeroTelefono}
                                             email={medico.email}/>
                        )
                })}
            </div>

            <br/><br/><span className="iTuoiUtenti">I caregiver :</span><br/><br/>
            <div className="contenitoreCardPazienti">
                {data.map(function (medico, idx) {
                    if (medico.ruolo == "CAREGIVER")
                        return (
                            <CardAdminMedico key={idx} idUtente={medico.id} nomeUtente={medico.nome}
                                             cognomeUtente={medico.cognome} dataNascita={medico.dataDiNascita}
                                             genere={medico.genere} numero={medico.numeroTelefono}
                                             email={medico.email}/>
                        )
                })}
            </div>

            <br/><br/><span className="iTuoiUtenti">I caregiver non registrati :</span><br/><br/>
            <div className="contenitoreCardPazienti">
                {data.map(function (medico, idx) {
                    if (medico.ruolo == "CAREGIVER_NON_REGISTRATO")
                        return (
                            <CardAdminCaregiverNonRegistrato key={idx} idUtente={medico.id} nomeUtente={medico.nome}
                                             cognomeUtente={medico.cognome} dataNascita={medico.dataDiNascita}
                                             genere={medico.genere} numero={medico.numeroTelefono}
                                             email={medico.email}/>
                        )
                })}
            </div>

        </div>

    );
}

export default ListaUtenti;