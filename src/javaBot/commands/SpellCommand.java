package javaBot.commands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javaBot.DNDApi;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SpellCommand implements Command {
	JDA api;
	int rangeDistanceAmount = 0;

	public SpellCommand(JDA api) {
		this.api = api;
	}

	@Override
	public void execute(SlashCommandInteractionEvent interaction) {
		String spell = interaction.getOption("name").getAsString();

		DNDApi dndAPI = new DNDApi();
		JSONObject spellInfo = dndAPI.getSpellInfo(spell);

		JSONArray entries = spellInfo.getJSONArray("entries");

		String source = spellInfo.getString("source");
		JSONObject timeArr = spellInfo.getJSONArray("time").getJSONObject(0);
		int timeNumber = timeArr.getInt("number");
		String timeAction = timeArr.getString("unit");

		JSONObject range = spellInfo.getJSONObject("range");
		String rangeType = range.getString("type");
		JSONObject rangeDistance = range.getJSONObject("distance");
		String rangeDistanceType = rangeDistance.getString("type");

		try {
			rangeDistanceAmount = rangeDistance.getInt("amount");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String entriesDescription = "";

		for (int i = 0; i < entries.length() - 1; i++) {
			entriesDescription += "- `[" + (i + 1) + "]` " + entries.getString(i) + "\n";
		}

		EmbedBuilder basicInformationEmbed;

		if (!rangeDistanceType.equals("self")) {
			basicInformationEmbed = new EmbedBuilder().setTitle("Basic Information").setDescription("`" + spell + "`")
					.addField("Source", source, true).addField("Casting Time", timeNumber + " " + timeAction, true)
					.addField("Range", rangeType + " " + rangeDistanceAmount, true);
		} else {
			basicInformationEmbed = new EmbedBuilder().setTitle("Basic Information").setDescription("`" + spell + "`")
					.addField("Source", source, true).addField("Casting Time", timeNumber + " " + timeAction, true)
					.addField("Range", rangeType + " " + rangeDistanceType, true);
		}

		EmbedBuilder entriesEmbed = new EmbedBuilder().setTitle("Entries").setDescription(entriesDescription);

		try {
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
			interaction.replyEmbeds(basicInformationEmbed.build(), entriesEmbed.build()).queue();
		} else {
			interaction.getHook().sendMessageEmbeds(basicInformationEmbed.build(), entriesEmbed.build()).queue();
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
		return new OptionData[] { new OptionData(OptionType.STRING, "name", "The name of the spell", true, true) };
	}
}
