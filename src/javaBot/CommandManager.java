package javaBot;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javaBot.commands.ClassesCommand;
import javaBot.commands.Command;
import javaBot.commands.DiceCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(JDA api) {
        // Register commands
        registerCommand(new DiceCommand(api));
        registerCommand(new ClassesCommand(api));

        // Register slash commands with Discord

        api.updateCommands()
                .addCommands(commands.values().stream()
                        .map(cmd -> Commands.slash(cmd.getName(), cmd.getDescription()).addOptions(
                            cmd.getOptions()
                        ))
                        .collect(Collectors.toList())) // Use Collectors.toList() instead
                .queue();

    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    // Execute slash command
    public void handleSlashCommand(SlashCommandInteractionEvent interaction) {
        Command command = commands.get(interaction.getName());
        if (command != null) {
            command.execute(interaction);
        } else {
            interaction.reply("Unknown command!").setEphemeral(true).queue();
        }
    }
}
