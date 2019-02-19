package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;

/**
 * Opening message to the tariff device.
 * 
 */
@MessageTemplate("/?[{deviceAddress}]\\!<CR><LF>")
public class RequestMessage implements Message {

    private String deviceAddress;

    /**
     * Default constructor.
     */
    public RequestMessage() {
    }

    /**
     * Initializes the {@link #deviceAddress} with the given device adress.
     * @param deviceAddress the device adress
     */
    public RequestMessage(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    /**
     * Gets the device adress.
     * @return the device adress
     */
    public String getDeviceAddress() {
        return deviceAddress;
    }

}
