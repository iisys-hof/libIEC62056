package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.smartgrids.libiec62056.message.content.CommandType;
import de.iisys.smartgrids.libiec62056.message.content.ExitCommand;

/**
 * Used for block oriented data transfer. Exits the command.
 * 
 */
@MessageTemplate("<SOH>~{1:commandMessageIdentifier}{1:commandType}<ETX>:bcc~!bcc!")
public class BreakMessage extends CommandMessage {

    /**
     * Exits the command.
     */
    public BreakMessage() {
        super(CommandType.ExitCommand.COMPLETE_SIGN_OFF, new ExitCommand());
    }

}
