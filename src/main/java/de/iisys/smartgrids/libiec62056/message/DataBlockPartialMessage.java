package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.annotation.Callback;

/**
 * Used for long messages of block oriented data transfer.
 * 
 */
@MessageTemplate("<STX>~[{address}]\\([{value}][*{unit}]\\)<EOT>:bcc~!bcc!")
@Callback("processDataSet")
public class DataBlockPartialMessage extends DataBlockMessage {
}
