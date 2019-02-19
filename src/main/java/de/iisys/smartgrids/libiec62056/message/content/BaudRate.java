package de.iisys.smartgrids.libiec62056.message.content;

/**
 * Class that defines the baud rates and baud rate characters.
 * 
 */
public class BaudRate {

    //baud rate characters
    public static final char BAUD_RATE_A = 'A';
    public static final char BAUD_RATE_B = 'B';
    public static final char BAUD_RATE_C = 'C';
    public static final char BAUD_RATE_D = 'D';
    public static final char BAUD_RATE_E = 'E';
    public static final char BAUD_RATE_F = 'F';
    public static final char BAUD_RATE_G = 'G';
    public static final char BAUD_RATE_H = 'H';
    public static final char BAUD_RATE_I = 'I';

    //baud rate characters
    public static final char BAUD_RATE_0 = '0';
    public static final char BAUD_RATE_1 = '1';
    public static final char BAUD_RATE_2 = '2';
    public static final char BAUD_RATE_3 = '3';
    public static final char BAUD_RATE_4 = '4';
    public static final char BAUD_RATE_5 = '5';
    public static final char BAUD_RATE_6 = '6';
    public static final char BAUD_RATE_7 = '7';
    public static final char BAUD_RATE_8 = '8';
    public static final char BAUD_RATE_9 = '9';

    //baud rates
    public static final int BAUD_RATE_300 = 300;
    public static final int BAUD_RATE_600 = 600;
    public static final int BAUD_RATE_1200 = 1200;
    public static final int BAUD_RATE_2400 = 2400;
    public static final int BAUD_RATE_4800 = 4800;
    public static final int BAUD_RATE_9600 = 9600;
    public static final int BAUD_RATE_19200 = 19200;

    /**
     * Gets the baud rate with the given baud rate character.
     * @param baudRateCharacter the baud rate character
     * @return baud rate
     */
    public static int getBaudRateForBaudRateCharacter(char baudRateCharacter) {
        switch (baudRateCharacter) {
            case BAUD_RATE_A:
                return BAUD_RATE_600;
            case BAUD_RATE_B:
                return BAUD_RATE_1200;
            case BAUD_RATE_C:
                return BAUD_RATE_2400;
            case BAUD_RATE_D:
                return BAUD_RATE_4800;
            case BAUD_RATE_E:
                return BAUD_RATE_9600;
            case BAUD_RATE_F:
                return BAUD_RATE_19200;
            case BAUD_RATE_G:
            case BAUD_RATE_H:
            case BAUD_RATE_I:
                throw new UnsupportedOperationException("Reserved baud rate used.");
            case BAUD_RATE_0:
                return BAUD_RATE_300;
            case BAUD_RATE_1:
                return BAUD_RATE_600;
            case BAUD_RATE_2:
                return BAUD_RATE_1200;
            case BAUD_RATE_3:
                return BAUD_RATE_2400;
            case BAUD_RATE_4:
                return BAUD_RATE_4800;
            case BAUD_RATE_5:
                return BAUD_RATE_9600;
            case BAUD_RATE_6:
                return BAUD_RATE_19200;
            case BAUD_RATE_7:
            case BAUD_RATE_8:
            case BAUD_RATE_9:
                throw new UnsupportedOperationException("Reserved baud rate used.");
            default:
                return BAUD_RATE_300;
        }
    }

    /**
     * Gets the baud rate character with the given baud rate and protocol mode.
     * @param baudRate the baud rate
     * @param protocolMode the protocol mode
     * @return the baud rate character
     */
    public static char getBaudRateCharacterForBaudRate(int baudRate, char protocolMode) {
        switch (protocolMode) {
            case ProtocolMode.PROTOCOL_MODE_B:
                switch (baudRate) {
                    case BAUD_RATE_600:
                        return BAUD_RATE_A;
                    case BAUD_RATE_1200:
                        return BAUD_RATE_B;
                    case BAUD_RATE_2400:
                        return BAUD_RATE_C;
                    case BAUD_RATE_4800:
                        return BAUD_RATE_D;
                    case BAUD_RATE_9600:
                        return BAUD_RATE_E;
                    case BAUD_RATE_19200:
                        return BAUD_RATE_F;
                }
            case ProtocolMode.PROTOCOL_MODE_C:
            case ProtocolMode.PROTOCOL_MODE_E:
                switch (baudRate) {
                    case BAUD_RATE_300:
                        return BAUD_RATE_0;
                    case BAUD_RATE_600:
                        return BAUD_RATE_1;
                    case BAUD_RATE_1200:
                        return BAUD_RATE_2;
                    case BAUD_RATE_2400:
                        return BAUD_RATE_3;
                    case BAUD_RATE_4800:
                        return BAUD_RATE_4;
                    case BAUD_RATE_9600:
                        return BAUD_RATE_5;
                    case BAUD_RATE_19200:
                        return BAUD_RATE_6;
                }
            case ProtocolMode.PROTOCOL_MODE_D:
                return BAUD_RATE_3;
            case ProtocolMode.PROTOCOL_MODE_A:
            default:
                return '?';
        }
    }

}
