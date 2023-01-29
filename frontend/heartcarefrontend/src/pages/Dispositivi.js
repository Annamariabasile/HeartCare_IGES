import React, { useState, useEffect }  from "react";
import jwt from "jwt-decode";
import CardDispositivo from "../components/CardDispositivo";
import NoteContainer from "../components/NoteContainer";
import "../css/home-main-content.css";
import 'react-responsive-modal/styles.css';
import { Modal } from 'react-responsive-modal';
import CardPaziente from "../components/CardPaziente";

export default function Dispositivi() {
  const [dispositivi, setDispositivi] = useState([]);
  const token = localStorage.getItem("token");
  const [open, setOpen] = useState(false);
  const onOpenModal = () => setOpen(true);
  const onCloseModal = () => setOpen(false);
  const [descrizione, setDescrizione] = useState("");
  const [categoria, setCategoria] = useState("");
  const [numeroSeriale, setNumeroSeriale] = useState("");

  //const [loading, setLoading] = useState(false);

  const aggiornaDispositivi = (disp) => {
    setDispositivi(disp);
  }

  const registraDispositivo = async () => {
    console.log(categoria)
    const myHeaders = new Headers();
    myHeaders.append("Authorization", `Bearer ${token}`);
    myHeaders.append("Content-Type", "application/json");
    
    const raw = JSON.stringify({
      "categoria": categoria,
      "numeroSeriale": numeroSeriale,
      "descrizione": descrizione
    });
    
    const requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };
    
    fetch("http://localhost:8080/dispositivo/registra", requestOptions)
      .then(response => response.text())
      .then(result => console.log(result))
      .catch(error => console.log('error', error));
  }

  const fetchDispositivi = async () => {
    if(token == null) {
      console.log("Il token è null quindi esco")
      return;
    }

    const idPaziente = jwt(token)["id"];
  
    return await fetch("http://localhost:8080/getDispositiviByUtente/"+idPaziente, {
      method: "POST", 
      mode: "cors", 
      cache: "no-cache", 
      credentials: "same-origin", 
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      redirect: "follow", 
      referrerPolicy: "no-referrer", 
    }).then((response) => {
      return response.json()
    }).then(data => {
      setDispositivi(data)
    });
  }
  
  /*
  2 useEffect per evitare di avere un array vuoto come variabile di stato.
  */
  useEffect( () => {
    fetchDispositivi();
  }, [])

  useEffect(() => {
    if(dispositivi.length > 0) {
      console.log(dispositivi)
    }
  }, [dispositivi])


  return (
    <div className="contenitoreMainContent-Home">
      <br></br>
      <br></br>
      <br></br>
      <br></br>

      <span className="testo-bentornat">I tuoi dispositivi 🩺</span>

      <div className="full-container">
        <div className="container-sinistra">
          <div className="banner">
            <div className="blocco-testo-banner">
              <span className="testo-banner">Dispositivi totali</span>
              <span className="testo-banner-numero">12</span>
            </div>
            <div className="blocco-testo-banner">
              <span className="testo-banner">Sfigmomanometri</span>
              <span className="testo-banner-numero">120</span>
            </div>
            <div className="blocco-testo-banner">
              <span className="testo-banner">Coagulometri</span>
              <span className="testo-banner-numero">120</span>
            </div>
          </div>

          <div className="visite-container">
            <br></br>
            <br></br>
            <div className="box-visite">
            {Object.keys(dispositivi).map(function(num, index){
                return (
                  <CardDispositivo key={index} 
                                  titolo={dispositivi[num]["categoria"]} 
                                  descrizione={dispositivi[num]["descrizione"]} />
                )
            })}
            </div>
          </div>
          
          <div className="containerBottoni">
            <button className="bottoneHomePaziente" onClick={onOpenModal}>Aggiungi Dispositivo</button>
            <Modal open={open} onClose={onCloseModal} center>
              <h2>Aggiungi Dispositivo</h2>
              <select name="categoria" onChange={e => setCategoria(e.target.value)}>
                <option value="" disabled selected>Seleziona categoria</option>
                <option value="Coagulometro">Coagulometro</option>
                <option value="Misuratore di pressione">Misuratore di pressione</option>
                <option value="ECG">ECG</option>
                <option value="Saturimetro">Saturimetro</option>
                <option value="Misuratore glicemico">Misuratore Glicemico</option>
                <option value="Enzimi Cardiaci">Misuratore Enzimi Cardiaci</option>
              </select>
              <br/>
              <h2></h2>
              <textarea className="textAreaTestoNote" name="desc" type="text" placeholder=" Inserire una breve descrizione" cols={40} rows={8} onChange={e => setDescrizione(e.target.value)}></textarea>
              <h2></h2>
              <textarea className="textAreaTestoNote" name="nSeriale" type="text" placeholder=" Inserire numero seriale dispositivo" cols={40} rows={8} onChange={e => setNumeroSeriale(e.target.value)} ></textarea>
              <button className="bottoneInviaNota" onClick={registraDispositivo}>Aggiungi</button>
          </Modal>
          </div>
        </div>
        <div className="note-container">
          <NoteContainer />
        </div>
      </div>
    </div>
  );
}
