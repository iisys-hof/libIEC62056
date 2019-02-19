package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;

/**
 * Repeats a request if the transmission was faulty.
 * 
 */
@MessageTemplate("<NAK>")
public class RepeatRequestMessage implements Message {
}
