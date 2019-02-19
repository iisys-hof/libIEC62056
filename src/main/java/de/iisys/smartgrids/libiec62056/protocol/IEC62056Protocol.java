package de.iisys.smartgrids.libiec62056.protocol;

import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.libinterface.protocol.NetworkProtocolRunner;
import de.iisys.libstate.StateGraph;
import de.iisys.smartgrids.libiec62056.client.IEC62056Client;
import de.iisys.smartgrids.libiec62056.message.AcknowledgementMessage;
import de.iisys.smartgrids.libiec62056.message.BreakMessage;
import de.iisys.smartgrids.libiec62056.message.CommandPartialMessage;
import de.iisys.smartgrids.libiec62056.message.CommandMessage;
import de.iisys.smartgrids.libiec62056.message.DataBlockMessage;
import de.iisys.smartgrids.libiec62056.message.DataBlockPartialMessage;
import de.iisys.smartgrids.libiec62056.message.DataMessage;
import de.iisys.smartgrids.libiec62056.message.ErrorMessage;
import de.iisys.smartgrids.libiec62056.message.IdentificationMessage;
import de.iisys.smartgrids.libiec62056.message.OptionSelectMessage;
import de.iisys.smartgrids.libiec62056.message.RepeatRequestMessage;
import de.iisys.smartgrids.libiec62056.message.RequestMessage;
import java.io.IOException;

/**
 * Data exchange is bi-directional in protocol modes A, B, C is always initiated by the
 * HHU with a transmission of a request message. In protocol modes A to C, the HHU acts as a
 * master and the tariff device acts as a slave. These protocol modes permit meter reading and
 * programming.
 * The protocol mode used by the tariff device is indicated to the HHU by the identification
 * message. Protocol modes A to D are identified by the baud rate identification character (see
 * item 13 in 6.3.3) while protocol mode E is identified by an escape sequence (see items 23 and
 * 24 in 6.3.2).
 * 
 */
public abstract class IEC62056Protocol extends NetworkProtocolRunner {

    //frame start character
    protected final String START = "start";
    //end character
    protected final String END = "end";

    //send action
    protected final String SEND_ACTION = "sendAction";
    //process action
    protected final String PROCESS_ACTION = "processAction";

    protected final String REQUEST_MESSAGE = "requestMessage";
    protected final String OPTION_SELECT_MESSAGE = "optionSelectMessage";
    protected final String PROGRAMMING_COMMAND_MESSAGE = "programmingCommandMessage";
    protected final String PROGRAMMING_COMMAND_PARTIAL_MESSAGE = "programmingCommandPartialMessage";
    protected final String ACKNOWLEDGE_MESSAGE = "acknowledgeMessage";
    protected final String REPEAT_REQUEST_MESSAGE = "repeatRequestMessage";
    protected final String BREAK_MESSAGE = "breakMessage";

    protected final String IDENTIFICATION_RECEIVE = "identificationReceive";
    protected final String PROGRAMMING_COMMAND_RECEIVE = "programmingCommandReceive";
    protected final String DATA_RECEIVE = "dataReceive";
    protected final String ACKNOWLEDGE_RECEIVE = "acknowledgeReceive";
    protected final String REPEAT_REQUEST_RECEIVE = "repeatRequestReceive";
    protected final String PROGRAMMING_DATA_RECEIVE = "programmingDataReceive";
    protected final String PROGRAMMING_DATA_PARTIAL_RECEIVE = "programmingDataPartialReceive";
    protected final String BREAK_RECEIVE = "breakReceive";

    protected final String ERROR_RECEIVED = "errorReceived";

    private IEC62056Client client;

    /**
     * Runs the networkprotocol and initializes {@link #client} with the given client paramater.
     * @param client the client
     */
    public IEC62056Protocol(IEC62056Client client) {
        super();
        this.client = client;
    }

    /**
     * Gets the client.
     * @return the client
     */
    protected IEC62056Client getClient() {
        return client;
    }

    /**
     * Registers the states of the given protocol.
     * @param protocol the protocol.
     */
    @Override
    protected void registerStates(StateGraph protocol) {
        protocol.registerState(START);
        protocol.registerState(END);

        protocol.registerState(SEND_ACTION, (state) -> send(state));
        protocol.registerState(PROCESS_ACTION, (state) -> process(state));

        protocol.registerState(REQUEST_MESSAGE, (state) -> prepareAndSend(state, RequestMessage.class));
        protocol.registerState(OPTION_SELECT_MESSAGE, (state) -> prepareAndSend(state, OptionSelectMessage.class));
        protocol.registerState(PROGRAMMING_COMMAND_MESSAGE, (state) -> prepareAndSend(state, CommandMessage.class));
        protocol.registerState(PROGRAMMING_COMMAND_PARTIAL_MESSAGE, (state) -> prepareAndSend(state, CommandPartialMessage.class));
        protocol.registerState(ACKNOWLEDGE_MESSAGE, (state) -> prepareAndSend(state, AcknowledgementMessage.class));
        protocol.registerState(REPEAT_REQUEST_MESSAGE, (state) -> prepareAndSend(state, RepeatRequestMessage.class));
        protocol.registerState(BREAK_MESSAGE, (state) -> prepareAndSend(state, BreakMessage.class));

        protocol.registerState(IDENTIFICATION_RECEIVE, (state) -> receiveAndProcess(state, IdentificationMessage.class, ErrorMessage.class));
        protocol.registerState(PROGRAMMING_COMMAND_RECEIVE, (state) -> receiveAndProcess(state, CommandMessage.class, ErrorMessage.class));
        protocol.registerState(DATA_RECEIVE, (state) -> receiveAndProcess(state, DataMessage.class, ErrorMessage.class));
        protocol.registerState(ACKNOWLEDGE_RECEIVE, (state) -> receiveAndProcess(state, AcknowledgementMessage.class, ErrorMessage.class));
        protocol.registerState(REPEAT_REQUEST_RECEIVE, (state) -> receiveAndProcess(state, RepeatRequestMessage.class, ErrorMessage.class));
        protocol.registerState(PROGRAMMING_DATA_RECEIVE, (state) -> receiveAndProcess(state, DataBlockMessage.class, ErrorMessage.class));
        protocol.registerState(PROGRAMMING_DATA_PARTIAL_RECEIVE, (state) -> receiveAndProcess(state, DataBlockPartialMessage.class, ErrorMessage.class));
        protocol.registerState(BREAK_RECEIVE, (state) -> receiveAndProcess(state, BreakMessage.class, ErrorMessage.class));

        protocol.registerState(ERROR_RECEIVED);
    }

    /**
     * Registers the transitions of the given protocol.
     * @param protocol the protocol
     */
    @Override
    protected void registerTransitions(StateGraph protocol) {
        protocol.registerTransition(START);

        protocol.registerTransition(BREAK_MESSAGE, END, (transition) -> getPreparedMessage(transition.getSource()) instanceof BreakMessage);

        protocol.registerTransition(IDENTIFICATION_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);
        protocol.registerTransition(PROGRAMMING_COMMAND_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);
        protocol.registerTransition(DATA_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);
        protocol.registerTransition(ACKNOWLEDGE_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);
        protocol.registerTransition(REPEAT_REQUEST_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);
        protocol.registerTransition(PROGRAMMING_DATA_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);
        protocol.registerTransition(PROGRAMMING_DATA_PARTIAL_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);
        protocol.registerTransition(BREAK_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getDestination()) instanceof ErrorMessage);

        protocol.registerTransition(ERROR_RECEIVED, END);
        protocol.registerTransition(BREAK_RECEIVE, END);
    }

    /**
     * Registers the suppliers for {@link RequestMessage}, {@link AcknowledgementMessage},
     * {@link RepeatRequestMessage} and {@link BreakMessage}.
     */
    @Override
    protected void registerSuppliers() {
        supplyFor(RequestMessage.class, () -> new RequestMessage());
        supplyFor(AcknowledgementMessage.class, () -> new AcknowledgementMessage());
        supplyFor(RepeatRequestMessage.class, () -> new RepeatRequestMessage());
        supplyFor(BreakMessage.class, () -> new BreakMessage());
    }
     
    /**
     * Gets the client and writes the message.
     * @param message the message
     * @return the message
     * @throws IOException 
     */
    @Override
    protected Message write(Message message) throws IOException {
        getClient().write(message);
        return message;
    }

    /**
     * Gets the client and reads the message of the class.
     * @param classes the class
     * @return the message to read
     * @throws IOException 
     */
    @Override
    protected Message read(Class<? extends Message>... classes) throws IOException {
        return getClient().read(classes);
    }

}
