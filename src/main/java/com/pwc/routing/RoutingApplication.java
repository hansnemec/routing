package com.pwc.routing;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

import java.nio.file.Path;

import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Executable class with the main() method, also used as configuration.
 *
 * @author hans
 */
@SpringBootApplication
@Configuration
public class RoutingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoutingApplication.class, args);
	}

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		// fresh new dir for every run - faster than cleaning on startup
		// nice to have - delete the files on shutdown
		Path dir = Path.of(System.getProperty("java.io.tmpdir"), "routing_db_" + System.currentTimeMillis());
		
		DatabaseManagementService managementService = new DatabaseManagementServiceBuilder(dir).build();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				managementService.shutdown();
			}
		});
		
		return managementService.database(DEFAULT_DATABASE_NAME);
	}
}
