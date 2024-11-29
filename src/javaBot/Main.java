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
	JDA api;
	private final CommandManager commandManager;

	Main() {
		String token;

		try {
			token = getBotToken();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Initialize the bot
		JDA api = JDABuilder
				.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,
						GatewayIntent.GUILD_MEMBERS)
				.addEventListeners(new MessageReceivedListener(), this).build();

		this.commandManager = new CommandManager(api);
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent interaction) {
		commandManager.handleSlashCommand(interaction);
	}

	public String getBotToken() throws IOException {
		// Read the contents of the file into a String

		File file = new File("config.json");
		String content = new String(Files.readAllBytes(file.toPath()));

		// Parse the file content as JSON
		JSONObject tokenFile = new JSONObject(content);
		String token = tokenFile.getString("BOT_TOKEN");

		return token;
	}

	public static void main(String[] arguments) {
		try {
			new Main();
		} catch (Exception e) {
			System.err.println("Error initializing the bot: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
