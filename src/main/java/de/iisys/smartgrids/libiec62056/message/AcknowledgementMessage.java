package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;

/**
 * Negotiation of advanced features.
 * 
 */
@MessageTemplate("<ACK>")
public class AcknowledgementMessage implements Message {
}
