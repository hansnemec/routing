package com.pwc.routing.to;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Transfer object representing a route from source to destination.
 *
 * @author hans
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Route {

	List<String> route; // 3 letter country codes - see Country
	
}
