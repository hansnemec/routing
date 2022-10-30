package com.pwc.routing.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Transfer object representing a country.
 * 
 * @author hans
 */
@Getter
@Setter
@ToString
public class Country {

	String cca3; // 3 letter code - e.g. CZE
	String[] borders; // 3 letter codes of border countries - may be empty
	
}
