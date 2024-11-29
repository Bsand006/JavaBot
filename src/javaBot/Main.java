package javaBot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main extends ListenerAdapter {
	public JDA api;
	public CommandManager commandManager;

	Main() {

		// Get the bot token
		String token;
		try {
			token = getBotToken();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Initialize the bot
		api = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,
				GatewayIntent.GUILD_MEMBERS).addEventListeners(new MessageReceivedListener(), this).build();

		this.commandManager = new CommandManager(api);
	}

	@Override // Pass slash command to slashcommandmanager
	public void onSlashCommandInteraction(SlashCommandInteractionEvent interaction) {
		this.commandManager.handleSlashCommand(interaction);
	}
	
	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent interaction) {
	    if (interaction.getName().equals("spell") && interaction.getFocusedOption().getName().equals("name")) {
	        // Create an instance of your DNDApi class
	        DNDApi dndAPI = new DNDApi();
	
	        // Fetch the spells data
	        JSONObject responseObject = dndAPI.getSpells();
	        
	        // Extract the spells array from the response
	        JSONArray spells = responseObject.getJSONArray("results");
	        ArrayList<String> formattedSpells = new ArrayList<>();
	
	        // Populate the formattedSpells list
	        for (int i = 0; i < spells.length(); i++) { // Corrected the loop to include all elements
	            JSONObject spellObject = spells.getJSONObject(i);
	            formattedSpells.add(spellObject.getString("name").toLowerCase());
	        }
	
	        // Filter the spells and create choices
	        List<Command.Choice> options = formattedSpells.stream()
	                .filter(word -> word.toLowerCase().startsWith(interaction.getFocusedOption().getValue().toLowerCase())) // Filter words based on user's input, case-insensitive
	                .map(word -> new Command.Choice(word, word)) // Map words to choices
	                .collect(Collectors.toList());
	                
	        while (options.size() > 25) { // Limit option limit to 25
				options.remove(options.size() - 1);
			}
	
	        // Reply with the choices
	        interaction.replyChoices(options).queue();
	    }
	}


	private String getBotToken() throws IOException {

		// Read the contents of the file into a String
		File file = new File("config.json");
		String content = new String(Files.readAllBytes(file.toPath()));

		// Parse the file content as JSON
		JSONObject tokenFile = new JSONObject(content);
		String token = tokenFile.getString("BOT_TOKEN");

		return token;
	}

	public static void main(String[] arguments) { // Run program
		try {
			new Main();
		} catch (Exception e) {
			System.err.println("Error initializing the bot: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
