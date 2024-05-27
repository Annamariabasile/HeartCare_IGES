import React, {useEffect, useState} from "react";
import HamburgerMenu from "../components/HamburgerMenu";
import ListaVisita from "../components/ListaVisita";
import "../css/scheduleStyle.css";
import Modal from "react-responsive-modal";
import { BiPlusCircle, BiPlusMedical } from "react-icons/bi";
import jwt from "jwt-decode";
import pathGif from "../images/Ripple-1s-200px_1.gif";
import { FaRegHospital } from "react-icons/fa";
import VisiteCaregiverContainer from "../components/VisiteCaregiverContainer";

function ScheduleCaregiver() {
    const [open, setOpen] = useState(false);
    const [openErrore, setOpenErrore] = useState(false);
    const [openRiepilogoVisita, setOpenRiepilogoVisita] = useState(false);
    const [pazienti,setPazienti] = useState([{}]);
    const [indirizzi,setIndirizzi] = useState([{}]);
    const [utente,setUtente] = useState({});
    const [pazienteSelect,setPazienteSelect] = useState();
    const [indirizzoSelect,setindirizzoSelect] = useState();
    const [dataSelect,setDataSelect] = useState();
    const onOpenModalErrore = () => setOpenErrore(true);
    const onCloseModalErrore = () => setOpenErrore(false);
    const onOpenModal = () => setOpen(true);
    const onCloseModal = () => setOpen(false);
    const onOpenModalRiepilogo = () => setOpenRiepilogoVisita(true);
    const onCloseModalRiepilogo = () => {
        setOpenRiepilogoVisita(false)
        document.location.reload();
    };
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
        const fetchPazienti = async () => {
            try {
                const response = await fetch("http://localhost:8080/getPazientiByCaregiver/" + jwt(token).id, {
                    method: "GET",
                    headers: config,
                }).then(response => response.json());
                setPazienti(response)
            } catch (error) {
                console.error(error.message);
            }
        }
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
        fetchPazienti();
        fetchHome();
    },[])


    useEffect(() => {
        if(utente.length > 0) {

        }
    }, [utente])

    const aggiornaPazienteSelect = (event) => {
        setPazienteSelect(event.target.value);
    }

    return (
        <div className="contenitoreScheduleContent">
            {
                (utente["sesso"] === "M") ? <span className="bentornato">Bentornato, Dr. {utente["cognome"]} 👋🏻</span> : <span className="bentornato">Bentornata, Drs. {utente["cognome"]} 👋🏻</span>
            }
            <span className="iTuoiPazienti">Le tue visite in programma: </span>

            <div className="box-visite box-visiteHomeMedico">
                <VisiteCaregiverContainer classe="cardPazienteHomeMedico" ruolo={utente["ruolo"]}/>
            </div>
        </div>
    );
}

export default ScheduleCaregiver;
