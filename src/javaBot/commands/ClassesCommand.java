package javaBot.commands;

import org.json.JSONObject;
import org.json.JSONArray;

import javaBot.DNDApi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClassesCommand implements Command {
	JDA api;

	public ClassesCommand(JDA api) {
		this.api = api;
	}

	@Override
	public void execute(SlashCommandInteractionEvent interaction) {
		
		DNDApi dndAPI = new DNDApi();
		
		JSONObject responseObject = dndAPI.getClasses();
		
		JSONArray classes = responseObject.getJSONArray("results");
		
		String description = "";
		
		for (int i = 0; i < classes.length() - 1; i++) {
			JSONObject _class = classes.getJSONObject(i);
			
			description += (_class.getString("name") + "\n");
		}

		interaction.reply(description).queue();
	}

	@Override
	public String getName() {
		return "classes";
	}

	@Override
	public String getDescription() {
		return "Get the DND classes";
	}
}
