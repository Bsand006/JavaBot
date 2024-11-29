package javaBot.commands;

import java.util.Random;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DiceCommand implements Command {
	JDA api;
	Random rand;

	public DiceCommand(JDA api) {
		this.api = api;
		rand = new Random();
	}

	@Override
	public void execute(SlashCommandInteractionEvent interaction) {
		int result = 0;
		int modifier = 0;
		int diceAmount;

		if (interaction.getName().equals("dice")) {
			String diceType = interaction.getOption("type").getAsString(); // Get the dice type

			try {
				modifier = interaction.getOption("modifier").getAsInt();
			} catch (NullPointerException e) {
				modifier = 0;
			}

			/*
			 * If no amount of dice is specified then diceAmount will be null. Try catch
			 * block gets around this. First it tries to generate a number with diceAmount.
			 * If it gets a nullpointerexception, it assumes diceAmount is 1 and generates a
			 * number with that.
			 */

			try {
				diceAmount = interaction.getOption("times").getAsInt(); // Get diceAmount

				if (diceAmount > 100) { // Dice limit
					interaction.reply("Cannot role more than 100 dice at at time!").queue();
					return;
				}

				// Generate a random number of the specified type, for the amount specified in
				// diceAmount
				for (int i = 0; i < diceAmount; i++) {
					int n = Integer.parseInt(diceType.substring(1));
					result += rand.nextInt(n) + 1;
				}

				result += modifier;

			} catch (NullPointerException e) { // If no diceAmount is specified
				int n = Integer.parseInt(diceType.substring(1));
				result = rand.nextInt(n) + 1 + modifier;
			}

			interaction.reply("" + result).queue(); // Send result

		}
	}

	@Override
	public String getName() {
		return "dice";
	}

	@Override
	public String getDescription() {
		return "Rolls dice";
	}

	@Override
	public OptionData[] getOptions() {
		return new OptionData[] {
				new OptionData(OptionType.STRING, "type", "The type of dice to roll", true).addChoice("d4", "d4")
						.addChoice("d6", "d6").addChoice("d8", "d8").addChoice("d10", "d10").addChoice("d12", "d12")
						.addChoice("d20", "d20").addChoice("d100", "d100"),
				new OptionData(OptionType.INTEGER, "times", "The amount of times to roll the dice"),
				new OptionData(OptionType.INTEGER, "modifier", "The amount to modify") };

	}
}
