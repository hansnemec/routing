package com.pwc.routing.service;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwc.routing.to.Country;
import com.pwc.routing.to.Route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Business service to compute routes.
 *
 * @author hans
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoutingService implements InitializingBean {
	
	final ObjectMapper jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	final GraphDatabaseService graphDatabaseService;
	
	/**
	 * Load countries and border data into the graph database.
	 * 
	 * Called once - right after application start.
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
//		log.info("Cleaning up the graph database");
//		
//		graphDatabaseService.executeTransactionally("MATCH (n) DETACH DELETE n");
//		
//		log.info("Database cleanup done");
		
		try (InputStream in = getClass().getResourceAsStream("/data/countries.json")) {
			Country[] countries = jsonMapper.readValue(in, Country[].class);
			
			StringBuilder buf = new StringBuilder();
			
			for (Country c : countries) {
				appendCountry(buf, c);
			}
			
			for (Country c : countries) {
				appendBorderLinks(buf, c);
			}
			
			buf.setCharAt(0, ' '); // replace the leading comma
			buf.insert(0, "CREATE");
			
			log.info("Loading country and border data");
			
			graphDatabaseService.executeTransactionally(buf.toString());
			
			log.info("Country and border data loaded");
		}
	}
	
	/**
	 * Try to find a path from source to destination.
	 * 
	 * @param from Required - 3 letter code of the source country
	 * @param to Required - 3 letter code of the destination country
	 * @return The found path - null if no path was found
	 */
	public Route findRoute(String from, String to) {
		String command = "MATCH (from:Country{code:'" + from + "'}),(to:Country{code:'" + to + "'}),path = shortestPath((from)-[*]-(to)) RETURN path";
		
		try (Transaction tx = graphDatabaseService.beginTx()) {
			Result result = tx.execute(command);
			
			if (!result.hasNext()) {
				return null;
			}
			
			Map<String, Object> map = result.next();
			Path path = (Path) map.get("path");
			
			List<String> route = new LinkedList<>();
			for (Node node : path.nodes()) {
				route.add((String) node.getProperty("code"));
			}
			
			return new Route(route);
		}
	}
	
	void appendCountry(StringBuilder buf, Country c) {
		String countryCmd = String.format(",(%s:Country{code:'%s'})", c.getCca3(), c.getCca3());
		buf.append(countryCmd);
	}
	
	void appendBorderLinks(StringBuilder buf, Country c) {
		String[] borderCountries = c.getBorders();
		
		if (ObjectUtils.isEmpty(borderCountries)) {
			return;
		}
		
		for (String borderCountry : borderCountries) {
			String borderCmd = String.format(",(%s)-[:BORDER]->(%s)", c.getCca3(), borderCountry);
			buf.append(borderCmd);
		}
	}
}
