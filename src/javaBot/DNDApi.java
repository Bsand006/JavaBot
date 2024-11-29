package javaBot;

import org.json.JSONObject;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class DNDApi {
	final private String BASE_URL = "https://www.dnd5eapi.co/api/";

	public JSONObject getClasses() {
		String url = BASE_URL + "classes/";

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);

		// Make the GET request and retrieve the response
		Response rawResponse = target.request().get();

		try {
			// Check for a successful status code
			if (rawResponse.getStatus() == 200) {
				// Read the response as a String
				String jsonString = rawResponse.readEntity(String.class);

				// Parse the JSON string into a JSONObject
				return new JSONObject(jsonString);
			} else {
				throw new RuntimeException("Failed request with status: " + rawResponse.getStatus());
			}
		} finally {
			// Ensure resources are cleaned up
			rawResponse.close();
			client.close();
		}
	}
	
	public JSONObject getSpells() {
		String url = BASE_URL + "spells";

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);

		// Make the GET request and retrieve the response
		Response rawResponse = target.request().get();

		try {
			// Check for a successful status code
			if (rawResponse.getStatus() == 200) {
				// Read the response as a String
				String jsonString = rawResponse.readEntity(String.class);

				// Parse the JSON string into a JSONObject
				return new JSONObject(jsonString);
			} else {
				throw new RuntimeException("Failed request with status: " + rawResponse.getStatus());
			}
		} finally {
			// Ensure resources are cleaned up
			rawResponse.close();
			client.close();
		}
	}
	
	public JSONObject getSpellInfo(String spell) {
		String url = BASE_URL + "spells/" + spell.replaceAll(" ", "-");

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);

		// Make the GET request and retrieve the response
		Response rawResponse = target.request().get();

		try {
			// Check for a successful status code
			if (rawResponse.getStatus() == 200) {
				// Read the response as a String
				String jsonString = rawResponse.readEntity(String.class);

				// Parse the JSON string into a JSONObject
				return new JSONObject(jsonString);
			} else {
				throw new RuntimeException("Failed request with status: " + rawResponse.getStatus());
			}
		} finally {
			// Ensure resources are cleaned up
			rawResponse.close();
			client.close();
		}
	} 

}
