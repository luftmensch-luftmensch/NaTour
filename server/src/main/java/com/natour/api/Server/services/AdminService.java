/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.AdminDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.*;
import com.natour.api.Server.repository.*;
import com.natour.api.Server.servicesInterface.AdminServiceInterface;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Classe che implementa l'interfaccia {@link com.natour.api.Server.servicesInterface.AdminServiceInterface} */
@Service("mainAdminService")
public class AdminService implements AdminServiceInterface{

    /** Oggetto repository necessario per le request inerenti ad {@link Admin} */
    @Autowired
    private AdminRepository adminRepository;

    // Di seguito vengono riportate tutte le repository necessaria ad ottenere le statistiche (i record totali) di utilizzo dell'applicazione
    /** Oggetto repository necessario per le request inerenti ad {@link Admin} */
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    /** Oggetto repository necessario per le request inerenti ad {@link Admin} */
    @Autowired
    private ItinerarioRepository itinerarioRepository;

    /** Oggetto repository necessario per le request inerenti ad {@link Admin} */
    @Autowired
    private MessaggioRepository messaggioRepository;

    /** Oggetto repository necessario per le request inerenti ad {@link Admin} */
    @Autowired
    private UtenteRepository utenteRepository;

    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntityToDTO})*/
    @Autowired
    private ModelMapper modelMapperAdmin;


    /** Metodo per la conversione da DTO a Entity
     * @param adminDTO: parametro da convertire
     * @return Singola admin (oggetto convertito)
     */
    private Admin convertDTOtoEntity(AdminDTO adminDTO){
        modelMapperAdmin.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Admin admin;
        admin = modelMapperAdmin.map(adminDTO, Admin.class);
        return admin;
    }

    /** Metodo per la conversione da DTO a Entity
     * @param admin: parametro da convertire
     * @return Singolo adminDTO (oggetto convertito)
     */
    private AdminDTO convertEntityToDTO(Admin admin){
        modelMapperAdmin.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        AdminDTO adminDTO = new AdminDTO();
        adminDTO = modelMapperAdmin.map(admin, AdminDTO.class);
        return adminDTO;
    }

    /** Metodo per ottenere la lista degli admin totali presenti sul database
     * @return lista degli admin totali
     */
    @Override
    public List<AdminDTO> getAdmins() {
        List<Admin> admins = this.adminRepository.findAll();
        List<AdminDTO> adminDTOS = new ArrayList<>();
        for (Admin a: admins){
            adminDTOS.add(convertEntityToDTO(a));
        }
        return adminDTOS;
    }

    /** Metodo per ottenere le informazioni di un singolo admin
     * @param username: Identificativo dell'admin
     * @return Singolo admin
     */
    @Override
    public AdminDTO getAdmin(String username) {
        if(this.adminRepository.findById(username).isEmpty()){
            throw new ApiRequestException("Admin non trovato!", HttpStatus.NOT_FOUND);
        }
        Admin admin = this.adminRepository.findSingoloAdminByUsername(username);
        return convertEntityToDTO(admin);
    }

    /** Implementazione del metodo per l'aggiunta di un nuovo admin
     * @param adminDTO: Oggetto che contiene tutti i parametri necessari alla creazione di un nuovo itinerario
     * @throws ApiRequestException In caso di errore lancia una eccezione che viene gestita nei metodi che ne faranno uso
     */
    @Override
    public void aggiungiAdmin(AdminDTO adminDTO) throws ApiRequestException{
        Admin admin = convertDTOtoEntity(adminDTO);
        if(this.adminRepository.existsById(admin.getUsername())){
            throw new ApiRequestException("Admin già esistente", HttpStatus.BAD_REQUEST);
        }
        this.adminRepository.save(admin);
    }

    /** Implementazione del metodo per l'eliminazione di un admin esistente
     * @param username: Identificativo necessario all'eliminazione
     * @throws ApiRequestException In caso di errore lancia una eccezione che viene gestita nei metodi che ne faranno uso
     */
    @Override
    public void eliminaAdmin(String username) throws ApiRequestException{

        if(!this.adminRepository.existsById(username)){
            throw new ApiRequestException("Admin non trovato!", HttpStatus.NOT_FOUND);
        }
        this.adminRepository.deleteById(username);
    }

    /** Implementazione del metodo per la modifica di un admin esistente
     * @param adminDTO: parametro necessario alla modifica
     */
    @Override
    public void aggiornaAdmin(AdminDTO adminDTO) {
        Admin admin = convertDTOtoEntity(adminDTO);
        if(this.adminRepository.findById(admin.getUsername()).isEmpty()){
            throw new ApiRequestException("Admin non trovato!", HttpStatus.NOT_FOUND);
        }
        this.adminRepository.save(admin);
    }

    /** Metodo per ottenere i record dei dati necessari all'admin per visualizzare le statistiche dell'applicazione mobile
     * @return lista di interi (da utilizzare all'interno del grafico)
     */
    @Override
    public List<Integer> getRecordTotali() {
        List<ChatRoom> chatRooms = this.chatRoomRepository.findAll();
        List<Itinerario> itinerarios = this.itinerarioRepository.findAll();
        List<Messaggio> messaggios = this.messaggioRepository.findAll();
        List<Utente> utentes = this.utenteRepository.findAll();

        // Una volta ottenuti tutti i record dei dati presenti sul db, ne prendo la size e la inserisco nell'arraylist creato
        List<Integer> recordTotali = new ArrayList<>();

        recordTotali.add(chatRooms.size());
        recordTotali.add(itinerarios.size());
        recordTotali.add(messaggios.size());
        recordTotali.add(utentes.size());

        return recordTotali;
    }
}