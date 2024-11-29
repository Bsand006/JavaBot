package javaBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Command interface for handling bot slash commands.
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param interaction The SlashCommandInteractionEvent triggered by the user.
     */
    void execute(SlashCommandInteractionEvent interaction);

    /**
     * Returns the name of the command (e.g., "ping").
     *
     * @return The name of the command.
     */
    String getName();

    /**
     * Returns a description of what the command does.
     *
     * @return The description of the command.
     */
    String getDescription();

    /**
     * Returns option data for the command;
     *
     * @return Option data for the command
     */
    OptionData getOptions();
}
