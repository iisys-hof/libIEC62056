package de.iisys.smartgrids.libiec62056.parser;

import de.iisys.libinterface.parser.service.MessageCharacters;
import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.libinterface.parser.service.MessageParserService;
import de.iisys.smartgrids.libiec62056.message.AcknowledgementMessage;
import de.iisys.smartgrids.libiec62056.message.OptionSelectMessage;
import de.iisys.smartgrids.libiec62056.message.IdentificationMessage;
import de.iisys.smartgrids.libiec62056.message.RepeatRequestMessage;
import de.iisys.smartgrids.libiec62056.message.RequestMessage;
import de.iisys.smartgrids.libiec62056.message.content.BaudRate;
import de.iisys.smartgrids.libiec62056.message.content.IdentificationCharacter;
import de.iisys.smartgrids.libiec62056.message.content.ModeControlCharacter;
import de.iisys.smartgrids.libiec62056.message.content.ProtocolControlCharacter;
import de.iisys.smartgrids.libiec62056.message.content.ProtocolMode;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Class to test the message parser service.
 * 
 * @author <a href="mailto:adrian.woeltche@iisys.de">Adrian W&ouml;ltche
 * &lt;adrian.woeltche@iisys.de&gt;</a>
 */
public class IEC62056ParserTest {

    private static MessageParserService messageParserService;

    /**
     * Sets the message parser service before class.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        messageParserService = new MessageParserService();
    }

    /**
     * Tests the request message.
     */
    @Test
    public void testRequestMessage() {
        RequestMessage message = new RequestMessage();
        String serialized = messageParserService.serialize(message);
        assertEquals("/?!" + MessageCharacters.CR + MessageCharacters.LF, serialized);

        message = new RequestMessage("12345");
        serialized = messageParserService.serialize(message);
        assertEquals("/?12345!" + MessageCharacters.CR + MessageCharacters.LF, serialized);
    }

    /**
     * Tests the identification message.
     */
    @Test
    public void testIdentificationMessage() {
        String message = "/ABC4IDENTIFICATION" + MessageCharacters.CR + MessageCharacters.LF;
        IdentificationMessage deserialized = messageParserService.deserialize(message, IdentificationMessage.class);
        assertEquals("ABC", deserialized.getManufacturerIdentification());
        assertEquals(200, deserialized.getMinimumReactionTime());
        assertEquals(BaudRate.getBaudRateForBaudRateCharacter('4'), deserialized.getBaudRate());
        assertEquals(ProtocolMode.PROTOCOL_MODE_C, deserialized.getProtocolMode());
        assertEquals(0, deserialized.getIdentificationCharacter());
        assertEquals("IDENTIFICATION", deserialized.getIdentification());

        message = "/XYZXIDENT" + MessageCharacters.CR + MessageCharacters.LF;
        deserialized = messageParserService.deserialize(message, IdentificationMessage.class);
        assertEquals("XYZ", deserialized.getManufacturerIdentification());
        assertEquals(200, deserialized.getMinimumReactionTime());
        assertEquals(BaudRate.getBaudRateForBaudRateCharacter('X'), deserialized.getBaudRate());
        assertEquals(ProtocolMode.PROTOCOL_MODE_A, deserialized.getProtocolMode());
        assertEquals(0, deserialized.getIdentificationCharacter());
        assertEquals("IDENT", deserialized.getIdentification());

        message = "/DEfB\\2TEST" + MessageCharacters.CR + MessageCharacters.LF;
        deserialized = messageParserService.deserialize(message, IdentificationMessage.class);
        assertEquals("DEf", deserialized.getManufacturerIdentification());
        assertEquals(20, deserialized.getMinimumReactionTime());
        assertEquals(BaudRate.getBaudRateForBaudRateCharacter('B'), deserialized.getBaudRate());
        assertEquals(ProtocolMode.PROTOCOL_MODE_B, deserialized.getProtocolMode());
        assertEquals(IdentificationCharacter.BINARY_MODE_HDLC, deserialized.getIdentificationCharacter());
        assertEquals("TEST", deserialized.getIdentification());

        message = "/JKlD\\@TESTER" + MessageCharacters.CR + MessageCharacters.LF;
        deserialized = messageParserService.deserialize(message, IdentificationMessage.class);
        assertEquals("JKl", deserialized.getManufacturerIdentification());
        assertEquals(20, deserialized.getMinimumReactionTime());
        assertEquals(BaudRate.getBaudRateForBaudRateCharacter('D'), deserialized.getBaudRate());
        assertEquals(ProtocolMode.PROTOCOL_MODE_B, deserialized.getProtocolMode());
        assertEquals('@', deserialized.getIdentificationCharacter());
        assertEquals("TESTER", deserialized.getIdentification());
    }

    /**
     * Tests the option select message.
     */
    @Test
    public void testOptionSelectMessage() {
        String message = MessageCharacters.ACK + "060" + MessageCharacters.CR + MessageCharacters.LF;
        OptionSelectMessage deserialized = messageParserService.deserialize(message, OptionSelectMessage.class);
        assertEquals(ProtocolControlCharacter.NORMAL_PROTOCOL, deserialized.getProtocolControlCharacter());
        assertEquals(BaudRate.getBaudRateForBaudRateCharacter('6'), deserialized.getBaudRate());
        assertEquals(BaudRate.BAUD_RATE_19200, deserialized.getBaudRate());
        assertEquals(ModeControlCharacter.DATA_READOUT, deserialized.getModeControlCharacter());
    }

    /**
     * Tests the acknowledge repeat messages.
     */
    @Test
    public void testAckRepeatMessages() {
        String message = Character.toString(MessageCharacters.ACK);
        Message deserialized = messageParserService.deserialize(message, AcknowledgementMessage.class, RepeatRequestMessage.class);
        assertTrue(deserialized instanceof AcknowledgementMessage);

        message = Character.toString(MessageCharacters.NAK);
        deserialized = messageParserService.deserialize(message, AcknowledgementMessage.class, RepeatRequestMessage.class);
        assertTrue(deserialized instanceof RepeatRequestMessage);
    }
}
