/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
/*
	Il Tag @SpringBootApplication include queste features:
	1. @EnableAutoConfiguration
	2. @Configuration
	3. @ComponentScan (permette a Spring Boot di ricercare nel package per componenti come Service, Contorller, Repository etc)
*/

/**
 * La classe ServerApplication è la main class dell'applicativo
 * Auto configura i beans (oggetti che costituiscono lo scheletro dell'applicazione)
 * presenti nel class-path e li configura per eseguirli con metodi
*/

@SpringBootApplication
public class ServerApplication {

	// Necessaria al DTO -> https://www.javaguides.net/2021/02/spring-boot-dto-example-entity-to-dto.html

	/** Necessario all'utilizzo dei modelMapper per la conversione nei controller
	 * {@see  com.natour.api.Server.controller.ChatRoomController }
	 * {@see  com.natour.api.Server.controller.GalleriaFotoItinerarioController }
	 * {@see  com.natour.api.Server.controller.ItinerarioController }
	 * {@see  com.natour.api.Server.controller.MessaggioController }
	 * {@see  com.natour.api.Server.controller.SegnalazioneFotoItinerarioController }
	 * {@see  com.natour.api.Server.controller.TagRicercaController }
	 * {@see  com.natour.api.Server.controller.TappaController }
	 * {@see  com.natour.api.Server.controller.UtenteController }
	 * @return restituisce un ModelMapper
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	/** Metodo main che inizializza il backend e setta tutta la ServerApplication
	 * @param args: argomenti
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
