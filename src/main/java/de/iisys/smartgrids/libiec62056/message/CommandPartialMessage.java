package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.smartgrids.libiec62056.message.content.CommandMessageIdentifier;
import de.iisys.smartgrids.libiec62056.message.content.ExecuteCommand;
import de.iisys.smartgrids.libiec62056.message.content.ExitCommand;
import de.iisys.smartgrids.libiec62056.message.content.PasswordCommand;
import de.iisys.smartgrids.libiec62056.message.content.ReadCommand;
import de.iisys.smartgrids.libiec62056.message.content.WriteCommand;
import de.iisys.libinterface.message.annotation.Callback;

/**
 * Class to identify the command messages using partial blocks.
 * Used for long messages.
 * 
 */
@MessageTemplate("<SOH>~{1:commandMessageIdentifier}{1:commandType}<STX>[{address}]\\([{value}][*{unit}]\\)<EOT>:bcc~!bcc!")
@Callback("prepareCommand")
public class CommandPartialMessage extends CommandMessage {

    /**
     * Identifies the command message as password command and sets the 
     * password command.
     * 
     * @param commandType the comand type
     * @param passwordCommand the password command
     */
    public CommandPartialMessage(char commandType, PasswordCommand passwordCommand) {
        super(CommandMessageIdentifier.PASSWORD_COMMAND, commandType, passwordCommand);
    }
    
    /**
     * Identifies the command message as write command and sets the command type.
     * @param commandType the command type
     * @param writeCommand the write command
     */
    public CommandPartialMessage(char commandType, WriteCommand writeCommand) {
        super(CommandMessageIdentifier.WRITE_COMMAND, commandType, writeCommand);
    }
    
    /**
     * Identifies the command message as read command and sets teh command type.
     * @param commandType the command type
     * @param readCommand the read command
     */
    public CommandPartialMessage(char commandType, ReadCommand readCommand) {
        super(CommandMessageIdentifier.READ_COMMAND, commandType, readCommand);
    }
    
    /**
     * Identifies the command message as execute command and sets the command type.
     * @param commandType the command type
     * @param executeCommand the execute command
     */
    public CommandPartialMessage(char commandType, ExecuteCommand executeCommand) {
        super(CommandMessageIdentifier.EXECUTE_COMMAND, commandType, executeCommand);
    }
    
    /**
     * Identifies the command message as exit command and sets the command type.
     * @param commandType the command type
     * @param exitCommand the execute command
     */
    public CommandPartialMessage(char commandType, ExitCommand exitCommand) {
        super(CommandMessageIdentifier.EXIT_COMMAND, commandType, exitCommand);
    }

}
