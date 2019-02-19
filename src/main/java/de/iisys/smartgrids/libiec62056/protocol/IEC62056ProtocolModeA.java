package de.iisys.smartgrids.libiec62056.protocol;

import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.libstate.StateGraph;
import de.iisys.libstate.interfaces.State;
import de.iisys.libstate.interfaces.Transition;
import de.iisys.smartgrids.libiec62056.client.IEC62056Client;
import de.iisys.smartgrids.libiec62056.message.AcknowledgementMessage;
import de.iisys.smartgrids.libiec62056.message.BreakMessage;
import de.iisys.smartgrids.libiec62056.message.CommandMessage;
import de.iisys.smartgrids.libiec62056.message.DataBlockMessage;
import de.iisys.smartgrids.libiec62056.message.DataBlockPartialMessage;
import de.iisys.smartgrids.libiec62056.message.ErrorMessage;
import de.iisys.smartgrids.libiec62056.message.IdentificationMessage;
import de.iisys.smartgrids.libiec62056.message.RepeatRequestMessage;
import de.iisys.smartgrids.libiec62056.message.content.CommandMessageIdentifier;
import java.io.IOException;

/**
 * Protocol mode A supports bidirectional data exchange at 300 baud without baud rate switching.
 * This protocol mode permits data readout and programming with optional password protection.
 * 
 */
public class IEC62056ProtocolModeA extends IEC62056Protocol {

    protected final String PASSWORD_ANSWER_RECEIVE = "passwordReceive";

    /**
     * Initializes {@link IEC62056Protocol#IEC62056Protocol(de.iisys.smartgrids.libiec62056.client.IEC62056Client) }
     * with the given client.
     * @param client the client
     */
    public IEC62056ProtocolModeA(IEC62056Client client) {
        super(client);
    }
    /**
     * Registers the states with the given protocol.
     * @param protocol the protocol
     */
    @Override
    protected void registerStates(StateGraph protocol) {
        super.registerStates(protocol);

        protocol.overrideState(PROGRAMMING_DATA_RECEIVE, (state) -> receiveAndProcess(state, DataBlockMessage.class, AcknowledgementMessage.class, RepeatRequestMessage.class, BreakMessage.class, ErrorMessage.class));
        protocol.overrideState(PROGRAMMING_DATA_PARTIAL_RECEIVE, (state) -> receiveAndProcess(state, DataBlockMessage.class, DataBlockPartialMessage.class, AcknowledgementMessage.class, RepeatRequestMessage.class, BreakMessage.class, ErrorMessage.class));

        protocol.registerState(PASSWORD_ANSWER_RECEIVE, (state) -> receiveAndProcess(state, AcknowledgementMessage.class, RepeatRequestMessage.class, BreakMessage.class, ErrorMessage.class));
    }

    /**
     * Registers the transitions with the given protocol.
     * @param protocol the protocol
     */
    @Override
    protected void registerTransitions(StateGraph protocol) {
        super.registerTransitions(protocol);

        protocol.registerTransition(START, REQUEST_MESSAGE);
        protocol.registerTransition(REQUEST_MESSAGE, IDENTIFICATION_RECEIVE);
        protocol.registerTransition(IDENTIFICATION_RECEIVE, DATA_RECEIVE, this::adaptIdentification);
        protocol.registerTransition(DATA_RECEIVE, PROGRAMMING_COMMAND_MESSAGE);

        protocol.registerTransition(PROGRAMMING_COMMAND_MESSAGE, PROGRAMMING_DATA_RECEIVE);
        protocol.registerTransition(PROGRAMMING_COMMAND_MESSAGE, END, (transition) -> getPreparedMessage(transition.getSource()) instanceof BreakMessage);

        protocol.registerTransition(PROGRAMMING_DATA_RECEIVE, END, (transition) -> getReceivedMessage(transition.getSource()) instanceof BreakMessage);
        protocol.registerTransition(PROGRAMMING_DATA_RECEIVE, SEND_ACTION, (transition) -> getReceivedMessage(transition.getSource()) instanceof RepeatRequestMessage);
        protocol.registerTransition(SEND_ACTION, PROGRAMMING_DATA_RECEIVE, (transition) -> {
            Message message = getPreparedMessage(transition.getSource());
            return message instanceof CommandMessage && (((CommandMessage) message).getCommandMessageIdentifier() == CommandMessageIdentifier.READ_COMMAND
                    || ((CommandMessage) message).getCommandMessageIdentifier() == CommandMessageIdentifier.WRITE_COMMAND);
        });
        protocol.registerTransition(PROGRAMMING_DATA_RECEIVE, PROGRAMMING_COMMAND_MESSAGE);

        protocol.registerTransition(PROGRAMMING_COMMAND_PARTIAL_MESSAGE, PROGRAMMING_DATA_PARTIAL_RECEIVE);
        protocol.registerTransition(PROGRAMMING_COMMAND_PARTIAL_MESSAGE, END, (transition) -> getPreparedMessage(transition.getSource()) instanceof BreakMessage);

        protocol.registerTransition(PROGRAMMING_DATA_PARTIAL_RECEIVE, END, (transition) -> getReceivedMessage(transition.getSource()) instanceof BreakMessage);
        protocol.registerTransition(PROGRAMMING_DATA_PARTIAL_RECEIVE, PROGRAMMING_COMMAND_MESSAGE, (transition) -> getReceivedMessage(transition.getSource()) instanceof DataBlockMessage);

        protocol.registerTransition(PROGRAMMING_DATA_PARTIAL_RECEIVE, ACKNOWLEDGE_MESSAGE);
        protocol.registerTransition(ACKNOWLEDGE_RECEIVE, PROGRAMMING_COMMAND_PARTIAL_MESSAGE, (transition) -> getReceivedMessage(transition.getSource()) instanceof DataBlockPartialMessage);

        protocol.registerTransition(PROGRAMMING_COMMAND_MESSAGE, PASSWORD_ANSWER_RECEIVE, (transition) -> {
            CommandMessage commandMessage = getPreparedMessage(transition.getSource());
            return commandMessage != null && commandMessage.getCommandMessageIdentifier() == CommandMessageIdentifier.PASSWORD_COMMAND;
        });

        protocol.registerTransition(PASSWORD_ANSWER_RECEIVE, PROGRAMMING_COMMAND_MESSAGE, (transition) -> getReceivedMessage(transition.getSource()) instanceof AcknowledgementMessage);
        protocol.registerTransition(PASSWORD_ANSWER_RECEIVE, SEND_ACTION, (transition) -> getReceivedMessage(transition.getSource()) instanceof RepeatRequestMessage);
        protocol.registerTransition(SEND_ACTION, PASSWORD_ANSWER_RECEIVE, (transition) -> {
            Message message = getPreparedMessage(transition.getSource());
            return message instanceof CommandMessage && ((CommandMessage) message).getCommandMessageIdentifier() == CommandMessageIdentifier.PASSWORD_COMMAND;
        });
        protocol.registerTransition(PASSWORD_ANSWER_RECEIVE, ERROR_RECEIVED, (transition) -> getReceivedMessage(transition.getSource()) instanceof ErrorMessage);
        protocol.registerTransition(PASSWORD_ANSWER_RECEIVE, END, (transition) -> getReceivedMessage(transition.getSource()) instanceof BreakMessage);

        protocol.registerTransition(REPEAT_REQUEST_RECEIVE, SEND_ACTION);
    }
    
    /**
     * Adapts the identification by setting the miniumum response time by getting the
     * miniumum reaction time and getting the transition source.
     * @param transition the transition
     * @throws IOException 
     */
    protected void adaptIdentification(Transition<State, State> transition) throws IOException {
        IdentificationMessage identificationMessage = getReceivedMessage(transition.getSource());
        getClient().setMinimumResponseTime(identificationMessage.getMinimumReactionTime());
    }

}
