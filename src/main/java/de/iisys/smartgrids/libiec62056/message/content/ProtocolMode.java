package de.iisys.smartgrids.libiec62056.message.content;

/**
 * Class that calculates the protocol mode.
 * 
 */
public class ProtocolMode {

    public static final char PROTOCOL_MODE_A = 'A';
    public static final char PROTOCOL_MODE_B = 'B';
    public static final char PROTOCOL_MODE_C = 'C';
    public static final char PROTOCOL_MODE_D = 'D';
    public static final char PROTOCOL_MODE_E = 'E';

    /**
     * Calculates the protocol mode with the given baud rate character.
     * @param baudRateCharacter the baud rate character
     * @return the protocol mode
     */
    public static char calculateProtocolModeByBaudRateCharacter(char baudRateCharacter) {
        switch (baudRateCharacter) {
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
                return PROTOCOL_MODE_B;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return ProtocolMode.PROTOCOL_MODE_C;
            default:
                return ProtocolMode.PROTOCOL_MODE_A;
        }
    }

}
