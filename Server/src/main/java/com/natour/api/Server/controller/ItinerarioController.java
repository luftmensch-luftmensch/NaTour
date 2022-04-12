/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.ItinerarioDTO;
import com.natour.api.Server.services.ItinerarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/itinerario
 * Le request ricevono dei json
*/

@RestController
@RequestMapping(path = "api/itinerario", produces = {"application/json"})
public class ItinerarioController {

    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto itinerarioService)
     * eliminiamo il problema del bean che deve essere injected
    */
    @Autowired
    @Qualifier("mainItinerarioService")
    private ItinerarioService itinerarioService;

    /** Metodo che permette di ottenere la lista degli itinerari totali presenti sul database
     * @return lista degli itinerari totali
     */
    @GetMapping(path = "ItinerariTotali")
    @ResponseBody
    public List<ItinerarioDTO> getAllItinerari(){ return this.itinerarioService.getItinerari(); }

    /** Metodo che permetta di ottenere le informazioni di un singolo itinerario presente sul database
     * @param nomeItinerario: Parametro necessario alla richiesta
     * @return Singolo itinerario
     */
    @GetMapping(path = "getItinerario/{itinerarioID}")
    @ResponseBody
    public ItinerarioDTO getSingoloItinerario(@PathVariable("itinerarioID") String nomeItinerario){
        return this.itinerarioService.getSingoloItinerario(nomeItinerario);
    }

    // Getter con filtri (più recente, nome itinerario, zona geografica, durata, stesso autore, città, dislivello, lunghezza)

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari recenti)
     * @return lista degli itinerari
     */
    @GetMapping(path = "ItinerariRecenti")
    @ResponseBody
    public List<ItinerarioDTO> getAllItinerariDescOrder(){ return this.itinerarioService.ricercaPerRecenti(); }

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari per nome)
     * @param nomeItinerario: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "Itinerari/NomeItinerario/{filtroNomeItinerario}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByNomeItinerari(@PathVariable("filtroNomeItinerario") String nomeItinerario){
        return this.itinerarioService.ricercaPerNomeItinerario(nomeItinerario);
    }

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari per città)
     * @param citta: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "Itinerari/Citta/{filtroCitta}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByCitta(@PathVariable("filtroCitta") String citta){
        return this.itinerarioService.ricercaByCitta(citta);
    }

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari per difficoltà)
     * @param diffolta: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "Itinerari/Difficolta/{filtroDifficolta}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByDifficolta(@PathVariable("filtroDifficolta") String diffolta){
        return this.itinerarioService.ricercaByDifficolta(diffolta);
    }

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari per dislivello)
     * @param dislivello: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "Itinerari/Dislivello/{filtroDislivello}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByDislivello(@PathVariable("filtroDislivello") Double dislivello){
        return this.itinerarioService.ricercaByDislivello(dislivello);
    }

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari per lunghezza)
     * @param lunghezza: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "Itinerari/Lunghezza/{filtroLunghezza}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByLunghezza(@PathVariable("filtroLunghezza") Double lunghezza){
        return this.itinerarioService.ricercaByLunghezza(lunghezza);
    }

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari per zona geografica)
     * @param zonaGeografica: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "Itinerari/ZonaGeografica/{filtroZonaGeografica}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByZonaGeografica(@PathVariable("filtroZonaGeografica") String zonaGeografica){
        return this.itinerarioService.ricercaByZonaGeografica(zonaGeografica);
    }

    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari per durata)
     * @param durata: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "Itinerari/Durata/{filtroDurata}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByDurata(@PathVariable("filtroDurata") String durata){
        return this.itinerarioService.ricercaByDurata(durata);
    }
    /** Metodo che permette di ottenere tutti gli itinerari che matchano con la richiesta (itinerari con lo stesso utente proprietario)
     * @param username: Parametro necessario per completare la richiesta
     * @return lista degli itinerari
     */
    @GetMapping(path = "getItinerariByUtente/{utenteID}")
    @ResponseBody
    public List<ItinerarioDTO> getItinerariByUtente(@PathVariable("utenteID") String username){
        return this.itinerarioService.getItinerariStessoUtente(username);
    }


    /** Metodo che permette l'aggiunta di un nuovo itinerario
     * @param itinerarioDTO: Oggetto che contiene tutte i parametri necessari all'inserimento di un nuovo itinerario
     * @return Restituisce una response che informi il frontend del corretto inserimento
     */
    @PostMapping(path = "aggiungiItinerario")
    @ResponseBody
    public ResponseEntity<String> aggiungiItinerario(@RequestBody ItinerarioDTO itinerarioDTO) {
        boolean response = this.itinerarioService.aggiungiItinerario(itinerarioDTO);
        if (response){
            // Nel caso in cui sia andato tutto a buon fine ritorniamo lo status 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /** Metodo che permette l'eliminazione di uno specifico itinerario
     * @param nomeItinerario: Stringa che identifica l'itinerario che si vuole eliminare
     */
    @DeleteMapping(path = "eliminaItinerario/{itinerarioID}")
    @ResponseBody
    public void eliminaItinerario(@PathVariable("itinerarioID") String nomeItinerario){
        this.itinerarioService.eliminaItinerario(nomeItinerario);
    }

    /** Metodo che permetta la modifica di un itinerario già esistente
     *
     * @param itinerarioDaAggiornare: Oggetto che contiene tutte i parametri necessari alla modifica dell'itinerario esistente
     */
    @PutMapping(path = "aggiornaItinerario")
    @ResponseBody
    public void aggiornaItinerario (@RequestBody ItinerarioDTO itinerarioDaAggiornare){
        this.itinerarioService.aggiornaItinerario(itinerarioDaAggiornare);
    }
}
