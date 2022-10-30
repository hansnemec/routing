package com.pwc.routing.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.RestTemplate;

import com.pwc.routing.to.Route;

/**
 * Integration test calling the real REST API.
 * 
 * START THE APP BEFORE RUNNING THIS TEST ;-)
 * 
 * Test expects the app to be running and just calls via HTTP.
 *
 * @author hans
 */
public class RoutingRestExercise {

	final RestTemplate restTemplate = new RestTemplate();
	final String baseUrl = "http://localhost:8080";
	
	@Test
	void testCzeSvk() {
		ResponseEntity<Route> resp = restTemplate.getForEntity(baseUrl + "/routing/CZE/SVK", Route.class);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCodeValue());
		assertNotNull(resp.getBody());
		assertEquals(List.of("CZE", "SVK"), resp.getBody().getRoute());
	}

	@Test
	void testCzeIta() {
		ResponseEntity<Route> resp = restTemplate.getForEntity(baseUrl + "/routing/CZE/ITA", Route.class);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCodeValue());
		assertNotNull(resp.getBody());
		assertEquals(List.of("CZE", "AUT", "ITA"), resp.getBody().getRoute());
	}
	
	@Test
	void testNonExistingPath() {
		assertThrows(BadRequest.class, () -> {
			restTemplate.getForEntity(baseUrl + "/routing/CZE/USA", Route.class);
		});
	}

	@Test
	void testNonExistingCountries() {
		assertThrows(BadRequest.class, () -> {
			restTemplate.getForEntity(baseUrl + "/routing/XXX/YYY", Route.class);
		});
	}
}
