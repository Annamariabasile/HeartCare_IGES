import React from "react";
import "../css/cardPaziente.css";
import { BiUser } from "react-icons/bi";
import { BsCalendarEvent } from "react-icons/bs";
import { BsGenderFemale, BsGenderMale } from "react-icons/bs";
import { IoCallOutline } from "react-icons/io5";
import { SlArrowDown } from "react-icons/sl";
import {useRef} from 'react';
import {useEffect, useState} from "react";
import Modal from "react-responsive-modal";
import jwt from "jwt-decode";
import {FaNotesMedical} from "react-icons/fa";
import {IoMdPin} from "react-icons/io";

function VisitaCard(props) {
    const [open, setOpen] = useState(false);
    const container = useRef();
    const linea = useRef();
    const informazioniPaziente = useRef();
    const bottoniPaziente = useRef();

    let iconaGenere = < BsGenderMale/>;

    // flag per capire se deve o meno ri-renderizzare le card
    let stopDoingThis = 0;
    const handleResize = () => {

        if(window.innerWidth > 767 && stopDoingThis == 0){
            forzaAperturaCard();
            stopDoingThis = 1;
        }

        if(window.innerWidth <= 767){
            stopDoingThis = 0;
        }

    }
    React.useEffect(() => {
        window.addEventListener("resize", handleResize, false);
    }, []);


    const mostraInformazioni = () => {
        linea.current.style.display = "flex";
        informazioniPaziente.current.style.display = "flex";
        bottoniPaziente.current.style.display = "flex";
    }
    // apre la card quando
    const forzaAperturaCard = () => {
        mostraInformazioni();
        container.current.style.height = "250px";
        container.current.style.flexDirection = "column";
        container.current.style.justifyContent = "normal";
    }

    const forzaChiusuraCard = () => {
        container.current.style.height = "50px";
        container.current.style.flexDirection = "row";
        container.current.style.justifyContent = "space-between";
    }

    const mostraInfoAggiuntive = () => {

        if(container.current.style.height != "250px"){
            cambiaDimensioneCard();
            mostraInformazioni();
        }else{
            cambiaDimensioneCard();
            nascondiInfoAggiuntive();
            forzaChiusuraCard();
        }
    }

    const nascondiInfoAggiuntive = () =>{
        linea.current.style.display = "none";
        informazioniPaziente.current.style.display = "none";
        bottoniPaziente.current.style.display = "none";
    }


    const cambiaDimensioneCard = () => {
        // se aperta, chiudila
        if (container.current.style.height == "250px") {
            forzaChiusuraCard();
        } else {  // se chiusa, aprila
            forzaAperturaCard();
        }
    }

    const [Categorie,setCategorie] = useState([]);
    const [Misurazioni,setMisurazioni] = useState([]);
    const token = localStorage.getItem("token");

    let config = {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        withCredentials: true,
        Authorization: `Bearer ${token}`,
        "Content-Type" : "application/json"
    };

    useEffect(() => {
        const getCategorie = async () =>{
            const response = await fetch("http://localhost:8080/getCategorie",{
                method : "POST",
                headers : config,
                body : JSON.stringify({
                    id : props.id
                })
            }).then(response => {
                return response.json()
            })
            setCategorie(response);
        }

        const fetchData = async() => {
            const response = await fetch("http://localhost:8080/getAllMisurazioniByPaziente",{
                method : "POST",
                headers : config,
                body : JSON.stringify({
                    id: props.id
                })
            }).then(response => {
                return response.json()
            })
            setMisurazioni(response);
        }
        getCategorie();
        fetchData();
    }, []);





    const onOpenModal = () => setOpen(true);
    const onCloseModal = () => setOpen(false);

    const [opendata, setOpendata] = useState(false);
    const OpenModal = () => setOpendata(true);
    const CloseModal = () => setOpendata(false);

    const [nuovaData,setDataSelect] = useState();

    const aggiornaDataSelect = (event) => {
        setDataSelect(event.target.value);
    }

    const aggiornaData = async () => {
        return await fetch("http://localhost:8080/visite/modificaDataVisita",{
            method: "POST",// *GET, POST, PUT, DELETE, etc.
                mode: "cors", // no-cors, *cors, same-origin
                cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
                credentials: "same-origin", // include, *same-origin, omit
                headers: {
                Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json"
            },
            body: JSON.stringify({
                idVisita: props.idVisita,
                nuovaData: nuovaData

            }),

                withCredentials: true,
                redirect: "follow", // manual, *follow, error
                referrerPolicy: "no-referrer",
        }).then(async response => {
            response = await response.json();
        })


    }
    const returnByCategoria = (categoria) => {
        return Misurazioni.filter( mis =>
            mis["categoria"] == categoria
        )
    }
    return(
        <div className={props.classe} ref={container}>
            <BiUser className="iconaPaziente"/>
            <span className="nomePaziente">{props.nomePaziente} {props.cognomePaziente}</span>
            <SlArrowDown className="altreInfoPaziente"  onClick={mostraInfoAggiuntive}/>
            <hr className="linea-visita"  ref={linea} ></hr>
            <div className="contenitoreInformazioniPazienteCard" ref={informazioniPaziente}>
                <div className="informazionePaziente">
                    <BsCalendarEvent/> <span>{props.dataVisita}</span>
                </div>
                <div className="informazionePaziente">
                    {iconaGenere} <span>{props.genere}</span>
                </div>
                <div className="informazionePaziente">
                    <IoCallOutline/> <span>{props.numero}</span>
                </div>
                <div className="informazionePaziente">
                    <IoMdPin/> <span>{props.comune}({props.provincia}), {props.via}, {props.ncivico}</span>
                </div>
            </div>

            <Modal open={open} onClose={onCloseModal} center>
                <h2>Fascicolo Sanitario del Paziente {props.nomePaziente} {props.cognomePaziente}</h2>
                <div className="contenitoreMisurazioni">
                    {Categorie.map( cat => {
                        return(
                            <div>
                                <h2>{cat}</h2>
                                <div className="bloccoMisurazione">
                                    {returnByCategoria(cat).map( mis => {
                                        return (
                                            <div className="contenitoreMisurazione">
                                                <div className="contenitoreIcona"><FaNotesMedical className="iconaMisurazione"/></div>
                                                <div className="contenitoreDati">
                                                    {
                                                        Object.entries(mis["misurazione"]).map(chiave => {
                                                                if (chiave[0] != "id") {
                                                                    return(
                                                                        <div>
                                                                            <span className="nomeCampo">{chiave[0]} :</span> <span className="valoreCampo">{chiave[1]}</span>
                                                                        </div>
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    }
                                                </div>
                                            </div>
                                        )
                                    })}
                                </div>
                            </div>
                        )
                    })}
                </div>
            </Modal>

            <div className="contenitorePulsantiPazienteCard" ref={bottoniPaziente}>
                <button className="buttonVisualizzaFascicolo" onClick={onOpenModal}>
                    Fascicolo Paziente
                </button>
                <Modal open={opendata} onClose={CloseModal}>
                    Aggiungi nuova data:
                    <input id ="calendar" className="dataPicker" type="date" min={new Date().getFullYear()} onChange={e => aggiornaDataSelect(e)}/>
                    <button className="buttonVisualizzaFascicolo" onClick={() => {aggiornaData(); setOpendata(false) ;  document.location.reload()}}> Conferma</button>
                </Modal>
                <button className="buttonVisualizzaFascicolo" onClick={OpenModal}>
                    Modifica data visita
                </button>
            </div>
        </div>
    );
}
export default VisitaCard;
