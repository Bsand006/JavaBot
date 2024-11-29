package commands;

import java.util.Random;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Dice extends ListenerAdapter {
	JDA api;
	Random rand;

	public Dice(JDA api) {
		this.api = api;
		rand = new Random();
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		int result = 0;
		int modifier = 0;
		int diceAmount;
		
		if (event.getName().equals("dice")) {
			String diceType = event.getOption("type").getAsString(); // Get the dice type
			
			try {
				modifier = event.getOption("modifier").getAsInt();
			} catch(NullPointerException e) {
				modifier = 0;
			}
			

			/*
			 * If no amount of dice is specified then diceAmount will be null. Try catch
			 * block gets around this. First it tries to generate a number with diceAmount.
			 * If it gets a nullpointerexception, it assumes diceAmount is 1 and generates a
			 * number with that.
			 */

			try {
				diceAmount = event.getOption("times").getAsInt(); // Get diceAmount

				if (diceAmount > 100) { // Dice limit
					event.reply("Cannot role more than 100 dice at at time!").queue();
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

			event.reply("" + result).queue();

		}
	}
}
