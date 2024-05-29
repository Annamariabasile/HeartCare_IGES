import React from "react";
import jwt from "jwt-decode";
import { useState, useEffect, useNavigate } from "react"
import { BiPlusCircle, BiPlusMedical } from "react-icons/bi";
import 'react-responsive-modal/styles.css';
import { Modal } from 'react-responsive-modal';
import "../css/note-style.css";

function NoteContainerCaregiver() {
    const token = localStorage.getItem("token");
    const [idPazienteMittente, setIdPazienteMittente] = useState('');
    const [idDestinatario, setIdDestinatario] = useState('');
    const idMittente = jwt(token).id;

    const [note, setNote] = useState([]);
    const [pazienti, setPazienti] = useState([]);

    useEffect(() => {
        const fetchAllNote = async () => {
            try {
                const response = await fetch("http://localhost:8080/comunicazione/getNoteCaregiver", {
                    method: "POST",
                    mode: "cors",
                    cache: "no-cache",
                    credentials: "same-origin",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    },
                });
                const data = await response.json();
                setNote(data);
            } catch (error) {
                console.error('Errore nel fetch delle note:', error);
            }
        };

        fetchAllNote();
    }, [token]);

    useEffect(() => {
        const fetchPazienti = async () => {
            try {
                const response = await fetch("http://localhost:8080/getPazientiByCaregiver/" + idMittente, {
                    method: "GET",
                    mode: "cors",
                    cache: "no-cache",
                    credentials: "same-origin",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                    redirect: "follow",
                    referrerPolicy: "no-referrer",
                });
                const data = await response.json();
                setPazienti(data);
            } catch (error) {
                console.error('Errore nel fetch dei pazienti:', error);
            }
        };

        fetchPazienti();
    }, [idMittente, token]);

    const handleSubmit = async (event) => {
        if (nota !== "") {
            setOpen(false);
            try {
                const response = await fetch("http://localhost:8080/comunicazione/invioNota", {
                    method: "POST",
                    mode: "cors",
                    cache: "no-cache",
                    credentials: "same-origin",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        idMittente: idPazienteMittente,
                        idDestinatario: idDestinatario,
                        nota: nota
                    }),
                    withCredentials: true,
                    redirect: "follow",
                    referrerPolicy: "no-referrer",
                });
                await response.json();
            } catch (error) {
                console.error('Errore nell\'invio della nota:', error);
            }
        } else {
            document.getElementsByClassName("erroreCompilazione")[0].style.display = "block";
        }
    };

    const onChangeHandler = async (event) => {
        const newIdPazienteMittente = event.target.value;
        setIdPazienteMittente(newIdPazienteMittente);
        try {
            const response = await fetch('http://localhost:8080/comunicazione/getMedico', {
                method: 'POST',
                mode: "cors",
                cache: "no-cache",
                credentials: "same-origin",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                redirect: "follow",
                referrerPolicy: "no-referrer",
                body: JSON.stringify({ idMittente: newIdPazienteMittente }),
            });

            if (response.ok) {
                const data = await response.json();
                console.log('Fetched data:', data);
                setIdDestinatario(data);
            } else {
                console.error('Errore nella richiesta:', response.statusText);
            }
        } catch (error) {
            console.error('Errore nella richiesta:', error);
        }
    };

    useEffect(() => {
        console.log('Updated idDestinatario:', idDestinatario);
    }, [idDestinatario]);

    useEffect(() => {
        console.log('Updated idPazienteMittente:', idPazienteMittente);
    }, [idPazienteMittente]);

    const [nota, setNota] = useState("")
    const onNotaChange = (event) => {
        setNota(event.target.value);
    }

    const [open, setOpen] = useState(false);

    const onOpenModal = async () => {
        setOpen(true);
        if (pazienti.length > 0) {
            const newIdPazienteMittente = pazienti[0].id;
            setIdPazienteMittente(newIdPazienteMittente);

            try {
                const response = await fetch('http://localhost:8080/comunicazione/getMedico', {
                    method: 'POST',
                    mode: "cors",
                    cache: "no-cache",
                    credentials: "same-origin",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                    redirect: "follow",
                    referrerPolicy: "no-referrer",
                    body: JSON.stringify({ idMittente: newIdPazienteMittente }),
                });

                if (response.ok) {
                    const data = await response.json();
                    console.log('Fetched data:', data);
                    setIdDestinatario(data);
                } else {
                    console.error('Errore nella richiesta:', response.statusText);
                }
            } catch (error) {
                console.error('Errore nella richiesta:', error);
            }
        }
    };

    const onCloseModal = () => setOpen(false);


    return (
        <div className="container-note">
            <div className="intestazione-note">
                <div className="div-intestazione-sx">
                    <span className="testo-intestazione-note">Note</span>
                </div>
                <div className="container-icona">
                    <BiPlusCircle className="icona-aggiunta-nota" onClick={onOpenModal} />
                    <Modal open={open} onClose={onCloseModal} center>
                        <h2>Scegli il paziente</h2>
                        <select className="selectPaziente" onChange={onChangeHandler} >
                            {
                                pazienti.map((paziente) =>
                                    <option value={paziente.id}>{paziente.nome} {paziente.cognome}</option>
                                )
                            };
                        </select>
                        <br />
                        <h2>Testo della nota</h2>
                        <textarea className="textAreaTestoNote" type="text" placeholder=" Inserire qui la nota" cols={40} rows={20} onChange={onNotaChange}></textarea>
                        <span className="erroreCompilazione">Aggiungi una nota</span>
                        <button className="bottoneInviaNota" onClick={() => { handleSubmit(); }}>Invia nota</button>
                    </Modal>
                </div>
            </div>
            <div className="singola-nota-container">
                <div className="nota-div">
                    {note.map((nota) =>
                        <>
                            <span className="autore-nota" value={nota.nome}>Da: {nota.nomeMittente}</span>
                            <span className="autore-nota" value={nota.nome}>A: {nota.nomeDestinatario}</span>
                            <span className="" value={nota.nome}>{nota.messaggio}</span>
                        </>
                    )}
                </div>
                <hr className="linea-tra-note"></hr>
            </div>
        </div>
    );
}

export default NoteContainerCaregiver;