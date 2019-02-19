package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;

/**
 * If the command meets protocol requirements but is not executed due to tariff 
 * device functionality (e.g. memory write protect, illegal command, etc.) an 
 * error message is returned.
 * 
 */
@MessageTemplate("<STX>~{error}<ETX>:bcc~!bcc!")
public class ErrorMessage implements Message {

    private String error;

    public String getError() {
        return error;
    }

}
