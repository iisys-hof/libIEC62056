package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.Callback;
import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.smartgrids.libiec62056.message.content.BaudRate;
import de.iisys.smartgrids.libiec62056.message.content.ProtocolControlCharacter;
import de.iisys.smartgrids.libiec62056.message.content.ProtocolMode;

/**
 * Class to calculate baud rate or the baud rate character.
 * 
 */
@MessageTemplate("<ACK>{1:protocolControlCharacter}{1:baudRateCharacter}{1:modeControlCharacter}<CR><LF>")
public class OptionSelectMessage implements Message {

    private char protocolControlCharacter;
    @Callback("calculateBaudRate")
    private char baudRateCharacter;
    private int baudRate;
    private char modeControlCharacter;

    /**
     * Default constructor.
     */
    public OptionSelectMessage() {
    }
    
    /**
     * Gets the baudrate character with the given baud rate and protocol mode c
     * and sets the {@link #modeControlCharacter} with the given one.
     * 
     * @param baudRate the baud rate
     * @param modeControlCharacter the mode control character
     */
    public OptionSelectMessage(int baudRate, char modeControlCharacter) {
        this(BaudRate.getBaudRateCharacterForBaudRate(baudRate, ProtocolMode.PROTOCOL_MODE_C), modeControlCharacter);
    }

    /**
     * Initializes the {@link #protocolControlCharacter} to '0'. Also initializes
     * {@link #baudRate} and {@link #modeControlCharacter} with the baud rate character
     * and mode control character.
     * @param baudRateCharacter the baud rate character
     * @param modeControlCharacter the mode control character
     */
    public OptionSelectMessage(char baudRateCharacter, char modeControlCharacter) {
        this(ProtocolControlCharacter.NORMAL_PROTOCOL, baudRateCharacter, modeControlCharacter);
    }

    /**
     * Initializes {@link #protocolControlCharacter}, {@link #baudRateCharacter} and 
     * {@link #modeControlCharacter} with the given protocol control character, baud rate character
     * and mode control character.
     * @param protocolControlCharacter the protocol character
     * @param baudRateCharacter the baud rate character
     * @param modeControlCharacter the mode control character
     */
    public OptionSelectMessage(char protocolControlCharacter, char baudRateCharacter, char modeControlCharacter) {
        this.protocolControlCharacter = protocolControlCharacter;
        this.baudRateCharacter = baudRateCharacter;
        this.modeControlCharacter = modeControlCharacter;
    }

    /**
     * Calculates the baud rate by the given baud rate character.
     */
    protected void calculateBaudRate() {
        baudRate = BaudRate.getBaudRateForBaudRateCharacter(baudRateCharacter);
    }

    public char getProtocolControlCharacter() {
        return protocolControlCharacter;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public char getModeControlCharacter() {
        return modeControlCharacter;
    }

}
