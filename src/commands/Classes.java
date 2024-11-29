package commands;

import org.json.JSONObject;
import org.json.JSONArray;

import javaBot.DNDApi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Classes extends ListenerAdapter {
	JDA api;

	public Classes(JDA api) {
		this.api = api;
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		
		DNDApi dndAPI = new DNDApi();
		
		JSONObject responseObject = dndAPI.getClasses();
		
		JSONArray classes = responseObject.getJSONArray("results");
		
		String description = "";
		
		for (int i = 0; i < classes.length() - 1; i++) {
			JSONObject _class = classes.getJSONObject(i);
			
			description += (_class.getString("name") + "\n");
		}
		
		event.reply(description).queue();
		
	}
}
