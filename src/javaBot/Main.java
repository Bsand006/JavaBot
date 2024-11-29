package javaBot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

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
