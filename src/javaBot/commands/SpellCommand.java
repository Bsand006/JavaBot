package javaBot.commands;

import org.json.JSONException;
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

		try  {
			spellInfo.getString("name");
		} catch (JSONException e) {
			if (!interaction.isAcknowledged()) {
				interaction.reply("Spell not found").queue();
			} else {
				interaction.getHook().sendMessage("Spell not found").queue();
			}
			return;
		}

		if (!interaction.isAcknowledged()) {
		    interaction.reply(spellInfo.getString("name")).queue();
		} else {
		    interaction.getHook().sendMessage(spellInfo.getString("name")).queue();
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
