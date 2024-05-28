import React from "react";
import "../css/scheduleStyle.css";
import "../css/style.css";
import "../css/visita-style.css";
import axios from "axios";
import { useState, useEffect } from "react";
import VisitaCard from "./VisitaCard";
import { json } from "react-router-dom";

function ListaVisita(props) {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const [map, setMap] = useState({});
    const token = localStorage.getItem("token");

    let config = {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        withCredentials: true,
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
    };

    const fetchData = async () => {
        setLoading(true);
        try {
            const response = await fetch("http://localhost:8080/visite/getVisiteCaregiver", {
                method: "POST",
                headers: config,
            }).then(response => response.json());
            setData(response);
            const mappedData = divideByNomeCognome(response);
            setMap(mappedData);
        } catch (error) {
            console.error(error.message);
        }
        setLoading(false);
    };

    function divideByNomeCognome(lista) {
        let map = {};

        lista.forEach((item) => {
            const key = `${item.nomePaziente} ${item.cognomePaziente}`;
            if (!map[key]) {
                map[key] = [];
            }
            map[key].push(item);
        });

        return map;
    }

    useEffect(() => {
        fetchData();
    }, []);

    return (
        <>
            {Object.keys(map).map((key, index) => (
                <div key={index} >
                    <h2>{key}</h2>
                    <div style={{ display: 'flex', gap: '10px'}}>
                    {map[key].map((item, idx) => (
                        <VisitaCard
                            classe={props.classe}
                            key={`${index}-${idx}`}
                            id={item.idPaziente}
                            dataVisita={item.data}
                            statoVisita={item.statoVisita}
                            nomePaziente={item.nomePaziente}
                            cognomePaziente={item.cognomePaziente}
                            numero={item.numeroTelefono}
                            genere={item.genere}
                            comune={item.comune}
                            via={item.viaIndirizzo}
                            ncivico={item.ncivico}
                            provincia={item.provincia}
                            idVisita={item.idVisita}
                            ruolo={props.ruolo}
                        />
                    ))}
                    </div>
                </div>
            ))}
        </>
    );
}

export default ListaVisita;
