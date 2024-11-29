package javaBot.commands;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javaBot.DNDApi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SpellCommand implements Command {
	JDA api;

	public SpellCommand(JDA api) {
		this.api = api;
	}

	@Override
	public void execute(SlashCommandInteractionEvent interaction) {
		String spell = interaction.getOption("name").getAsString(); 
		
		DNDApi dndAPI = new DNDApi();
		JSONObject spellInfo = dndAPI.getSpellInfo(spell);
		JSONArray descriptionArray = spellInfo.getJSONArray("desc");
		
		String descriptionString = "";
		
		for (int i = 0; i < descriptionArray.length() - 1; i++) {
			String description = descriptionArray.getString(i);
			descriptionString += "- " + description + "\n";
		}
		
		if (!interaction.isAcknowledged()) {
		    interaction.reply(descriptionString).queue();
		} else {
		    interaction.getHook().sendMessage(descriptionString).queue();
		}

	}

	@Override
	public String getName() {
		return "spell";
	}

	@Override
	public String getDescription() {
		return "Lookup a spell detail";
	}

	@Override
	public OptionData[] getOptions() {
		return new OptionData[] {
			new OptionData(
				OptionType.STRING,
				"name",
				"The name of the spell",
				true,
				true
			)
		};
	}
}
