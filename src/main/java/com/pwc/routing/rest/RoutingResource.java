package com.pwc.routing.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.routing.service.RoutingService;
import com.pwc.routing.to.Route;

import lombok.RequiredArgsConstructor;

/**
 * REST controller to handle requests to path /routing
 *
 * @author hans
 */
@RestController
@RequiredArgsConstructor
public class RoutingResource {

	final RoutingService routingService;
	
	@GetMapping("/routing/{from}/{to}")
	public ResponseEntity<Route> findRoute(@PathVariable("from") String from, @PathVariable("to") String to) {
		// nice to have - first check if both countries exist - and return 404 if not
		
		Route route = routingService.findRoute(from, to);
		
		if (route == null) {
			// as required by the spec
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Route>(route, HttpStatus.OK);
	}
}
