package javaBot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DNDApi {
	final private String SPELL_DATA_PATH = "data/spell.json";

	// Reads data from a file and returns a JSONObject with all the data
	private JSONObject readDataFromJSON(String path) throws IOException {

		// Read the contents of the file into a String
		File file = new File(path);
		String content = new String(Files.readAllBytes(file.toPath()));

		// Parse the file content as JSON
		return new JSONObject(content);
	}

	// Get the spell list 
	public JSONObject getSpells() {
		try {
			return readDataFromJSON(SPELL_DATA_PATH);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Get data about a specific spell
	public JSONObject getSpellInfo(String spellName) {
		try {
			JSONObject data = readDataFromJSON(SPELL_DATA_PATH);

			JSONArray spells = data.getJSONArray("spells");

			for (int i = 0; i < spells.length() - 1; i++) {
				JSONObject spellData = spells.getJSONObject(i);
				String spellDataName = spellData.getString("name");

				if (spellDataName.equals(spellName)) {
					return spellData;
				}
			}

			return new JSONObject("{\"error\": \"Spell not found\"}");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
