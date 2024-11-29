package javaBot;

import java.io.File;
import java.nio.file.Files;

import org.json.JSONObject;

import commands.Dice;
import commands.Classes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
	JDA api;

	void botInit() throws Exception {
		// Read the contents of the file into a String

		File file = new File("config.json");
		String content = new String(Files.readAllBytes(file.toPath()));

		// Parse the file content as JSONQ
		JSONObject tokenFile = new JSONObject(content);
		String token = tokenFile.getString("BOT_TOKEN");

		// Initialize the bot
		api = JDABuilder
				.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,
						GatewayIntent.GUILD_MEMBERS)
				.addEventListeners(new MessageReceivedListener(), 
					new Dice(api), 
					new Classes(api)
				).build();

		api.updateCommands().addCommands(
			Commands.slash("dice", "Roll a dice")
				.addOptions(
					new OptionData(
						OptionType.STRING, 
						"type", 
						"The type of dice to roll", true).addChoice("d4", "d4")
						.addChoice("d6", "d6").addChoice("d8", "d8").addChoice("d10", "d10").addChoice("d12", "d12")
						.addChoice("d20", "d20").addChoice("d100", "d100")
					).addOption(OptionType.INTEGER, "times", "How many dice you want to roll")
				.addOption(OptionType.INTEGER, "modifier", "Modifier to roll"),
				
			Commands.slash("classes", "View the classes")
			
		).queue();
				
		
	}

	public static void main(String[] arguments) {
		try {
			new Main().botInit(); // Corrected invocation
		} catch (Exception e) {
			System.err.println("Error initializing the bot: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
