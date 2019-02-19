package de.iisys.smartgrids.libiec62056.message.content;

/**
 * Class that sets the commands for every command message.
 * 
 */
public class CommandType {

    /**
     * Password command types.
     */
    public static class PasswordCommand {

        
        public static final char SECURE_ALGORITHM_OPERAND = '0';
        public static final char COMPARE_INTERNALLY_PASSWORD_OPERAND = '1';
        public static final char SECURE_ALGORITHM_RESULT = '2';
        public static final char FUTURE_RESERVED_3 = '3';
        public static final char FUTURE_RESERVED_4 = '4';
        public static final char FUTURE_RESERVED_5 = '5';
        public static final char FUTURE_RESERVED_6 = '6';
        public static final char FUTURE_RESERVED_7 = '7';
        public static final char FUTURE_RESERVED_8 = '8';
        public static final char FUTURE_RESERVED_9 = '9';

    }

    /**
     * Write command types.
     */
    public static class WriteCommand {

        public static final char FUTURE_RESERVED_0 = '0';
        public static final char WRITE_ASCII = '1';
        public static final char FORMATTED_COMMUNICATION_CODING_WRITE = '2';
        public static final char WRITE_ASCII_PARTIAL_BLOCK = '3';
        public static final char FORMATTED_COMMUNICATION_CODING_WRITE_PARTIAL_BLOCK = '4';
        public static final char NATIONAL_RESERVED_5 = '5';
        public static final char FUTURE_RESERVED_6 = '6';
        public static final char FUTURE_RESERVED_7 = '7';
        public static final char FUTURE_RESERVED_8 = '8';
        public static final char FUTURE_RESERVED_9 = '9';

    }

    /**
     * Read command types.
     */
    public static class ReadCommand {

        public static final char FUTURE_RESERVED_0 = '0';
        public static final char READ_ASCII = '1';
        public static final char FORMATTED_COMMUNICATION_CODING_READ = '2';
        public static final char READ_ASCII_PARTIAL_BLOCK = '3';
        public static final char FORMATTED_COMMUNICATION_CODING_READ_PARTIAL_BLOCK = '4';
        public static final char NATIONAL_RESERVED_5 = '5';
        public static final char NATIONAL_RESERVED_6 = '6';
        public static final char FUTURE_RESERVED_7 = '7';
        public static final char FUTURE_RESERVED_8 = '8';
        public static final char FUTURE_RESERVED_9 = '9';

    }

    /**
     * Execute command types.
     */
    public static class ExecuteCommand {

        public static final char FUTURE_RESERVED_0 = '0';
        public static final char FUTURE_RESERVED_1 = '1';
        public static final char FORMATTED_COMMUNICATION_CODING_EXECUTE = '2';
        public static final char FUTURE_RESERVED_3 = '3';
        public static final char FUTURE_RESERVED_4 = '4';
        public static final char FUTURE_RESERVED_5 = '5';
        public static final char FUTURE_RESERVED_6 = '6';
        public static final char FUTURE_RESERVED_7 = '7';
        public static final char FUTURE_RESERVED_8 = '8';
        public static final char FUTURE_RESERVED_9 = '9';

    }

    /**
     * Exit command types.
     */
    public static class ExitCommand {

        public static final char COMPLETE_SIGN_OFF = '0';
        public static final char COMPLETE_SIGN_OFF_FAST_WAKEUP = '1';
        public static final char FUTURE_RESERVED_2 = '2';
        public static final char FUTURE_RESERVED_3 = '3';
        public static final char FUTURE_RESERVED_4 = '4';
        public static final char FUTURE_RESERVED_5 = '5';
        public static final char FUTURE_RESERVED_6 = '6';
        public static final char FUTURE_RESERVED_7 = '7';
        public static final char FUTURE_RESERVED_8 = '8';
        public static final char FUTURE_RESERVED_9 = '9';

    }

}
