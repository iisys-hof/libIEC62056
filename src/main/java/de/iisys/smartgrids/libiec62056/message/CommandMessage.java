package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.smartgrids.libiec62056.message.content.CommandMessageIdentifier;
import de.iisys.smartgrids.libiec62056.message.content.DataSet;
import de.iisys.smartgrids.libiec62056.message.content.ExecuteCommand;
import de.iisys.smartgrids.libiec62056.message.content.ExitCommand;
import de.iisys.smartgrids.libiec62056.message.content.PasswordCommand;
import de.iisys.smartgrids.libiec62056.message.content.ReadCommand;
import de.iisys.smartgrids.libiec62056.message.content.WriteCommand;
import de.iisys.libinterface.message.annotation.Callback;

/**
 * Class to identify the command message.
 * 
 */
@MessageTemplate("<SOH>~{1:commandMessageIdentifier}{1:commandType}<STX>[{address}]\\([{value}][*{unit}]\\)<ETX>:bcc~!bcc!")
@Callback("prepareCommand")
public class CommandMessage implements Message {

    private char commandMessageIdentifier;
    private char commandType;
    private DataSet dataSet;

    private String address;
    private String value;
    private String unit;

    /**
     * Default constructor.
     */
    public CommandMessage() {
    }

    /**
     * Identifies the command message as password command and sets the 
     * password command.
     * 
     * @param commandType the comand type
     * @param passwordCommand the password command
     */
    public CommandMessage(char commandType, PasswordCommand passwordCommand) {
        this(CommandMessageIdentifier.PASSWORD_COMMAND, commandType, passwordCommand);
    }

    /**
     * Identifies the command message as write command and sets the command type.
     * @param commandType the command type
     * @param writeCommand the write command
     */
    public CommandMessage(char commandType, WriteCommand writeCommand) {
        this(CommandMessageIdentifier.WRITE_COMMAND, commandType, writeCommand);
    }

    /**
     * Identifies the command message as read command and sets the command type.
     * @param commandType the command type
     * @param readCommand the read command
     */
    public CommandMessage(char commandType, ReadCommand readCommand) {
        this(CommandMessageIdentifier.READ_COMMAND, commandType, readCommand);
    }

    /**
     * Identifies the command message as execute command and sets the command type.
     * @param commandType the command type
     * @param executeCommand the execute command
     */
    public CommandMessage(char commandType, ExecuteCommand executeCommand) {
        this(CommandMessageIdentifier.EXECUTE_COMMAND, commandType, executeCommand);
    }

    /**
     * Identifies the command message as exit command and sets the command type.
     * @param commandType the command type
     * @param exitCommand the execute command
     */
    public CommandMessage(char commandType, ExitCommand exitCommand) {
        this(CommandMessageIdentifier.EXIT_COMMAND, commandType, exitCommand);
    }

    /**
     * Initializeses {@link #commandMessageIdentifier}, {@link #commandType}
     * and {@link #dataSet} of {@link CommandMessage} with the given parameters.
     * @param commandMessageIdentifier identifies the command message
     * @param commandType the command type
     * @param dataSet provides the adress and data for die message
     */
    public CommandMessage(char commandMessageIdentifier, char commandType, DataSet dataSet) {
        this.commandMessageIdentifier = commandMessageIdentifier;
        this.commandType = commandType;
        this.dataSet = dataSet;
    }

    /**
     * Prepares the command by getting the adress, value and unit.
     */
    protected void prepareCommand() {
        if (dataSet != null) {
            address = dataSet.getAddress();
            value = dataSet.getValue();
            unit = dataSet.getUnit();
        }
    }

    public char getCommandMessageIdentifier() {
        return commandMessageIdentifier;
    }

    public char getCommandType() {
        return commandType;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

}
