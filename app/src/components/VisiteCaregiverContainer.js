import React from "react";
import "../css/scheduleStyle.css";
import "../css/style.css";
import "../css/visita-style.css";
import axios from "axios";
import { useState, useEffect } from "react";
import VisitaCard from "./VisitaCard";
import {json} from "react-router-dom";

function ListaVisita(props) {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const token = localStorage.getItem("token");
    let map = {};

    let config = {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        withCredentials: true,
        Authorization: `Bearer ${token}`,
        "Content-Type" : "application/json"
    };

    const fetchData = async () => {
        setLoading(true);
        try {
            const response = await fetch("http://localhost:8080/visite/getVisiteCaregiver", {
                method : "POST",
                headers : config,
            }).then(response => response.json());
            setData(response);
            map=divideByNomeCognome(data)
            console.log("MAPPA2: "+map)
        } catch (error) {
            console.error(error.message);
        }
        setLoading(false);
    };

    function divideByNomeCognome(lista) {
        let map = {};

        lista.forEach((item) => {
            const key = `${item.nomePaziente}${item.cognomePaziente}`;

            if (!map[key]) {
                map[key] = [];
            }

            map[key].push(item);
        });
        console.log("mappa1 "+ map)
        return Object.values(map);
    }

    useEffect( () => {
        fetchData();
    }, [])

    useEffect(() => {
        if(data.length > 0) {

        }
    }, [data])

    return (
        <>
            {Object.keys(data).map(function(el, index){
                return (
                    <VisitaCard classe={props.classe}
                                key={index}
                                id={data[el]["idPaziente"]}
                                dataVisita={data[el]["data"]}
                                statoVisita={data[el]["statoVisita"]}
                                nomePaziente={data[el]["nomePaziente"]}
                                cognomePaziente={data[el]["cognomePaziente"]}
                                numero={data[el]["numeroTelefono"]}
                                genere={data[el]["genere"]}
                                comune = {data[el]["comune"]}
                                via = {data[el]["viaIndirizzo"]}
                                ncivico = {data[el]["ncivico"]}
                                provincia = {data[el]["provincia"]}
                                idVisita = {data[el]["idVisita"]}
                                ruolo={props.ruolo}
                    />
                )
            })}
        </>
    );
}

export default ListaVisita;
