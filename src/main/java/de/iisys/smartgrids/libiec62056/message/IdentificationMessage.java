package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.libinterface.message.annotation.Callback;
import de.iisys.smartgrids.libiec62056.message.content.BaudRate;
import de.iisys.smartgrids.libiec62056.message.content.ProtocolMode;

/**
 * Answer of a tariff device.
 */
@MessageTemplate("/{3:manufacturerIdentification}{1:baudRateCharacter}[\\\\{1:identificationCharacter}]{identification}<CR><LF>")
public class IdentificationMessage implements Message {

    @Callback("calculateMinimumReactionTime")
    private String manufacturerIdentification;
    private int minimumReactionTime;
    @Callback("calculateBaudRateAndProtocolMode")
    private char baudRateCharacter;
    private int baudRate;
    private char protocolMode;
    private char identificationCharacter;
    private String identification;

    /**
     * Default constructor.
     */
    public IdentificationMessage() {
    }

    /**
     * If a tariff device transmits the third letter in lower case, the minimum reaction time for the
     * device is 20 ms instead of 200 ms.
     */
    protected void calculateMinimumReactionTime() {
        char lastCharacter = manufacturerIdentification.charAt(manufacturerIdentification.length() - 1);
        if (Character.isLowerCase(lastCharacter)) {
            minimumReactionTime = 20;
        } else {
            minimumReactionTime = 200;
        }
    }

    /**
     * Protocol modes A to D are identified by the baud rate identification character (see
     * item 13 in 6.3.3) while protocol mode E is identified by an escape sequence (see items 23 and
     * 24 in 6.3.2).
     */
    protected void calculateBaudRateAndProtocolMode() {
        baudRate = BaudRate.getBaudRateForBaudRateCharacter(baudRateCharacter);
        protocolMode = ProtocolMode.calculateProtocolModeByBaudRateCharacter(baudRateCharacter);
    }

    public String getManufacturerIdentification() {
        return manufacturerIdentification;
    }

    public int getMinimumReactionTime() {
        return minimumReactionTime;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public char getProtocolMode() {
        return protocolMode;
    }

    public char getIdentificationCharacter() {
        return identificationCharacter;
    }

    public String getIdentification() {
        return identification;
    }

}
