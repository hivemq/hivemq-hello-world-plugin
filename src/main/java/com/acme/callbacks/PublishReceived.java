package com.acme.callbacks;

import com.dcsquare.hivemq.spi.callback.CallbackPriority;
import com.dcsquare.hivemq.spi.callback.events.OnPublishReceivedCallback;
import com.dcsquare.hivemq.spi.callback.exception.OnPublishReceivedException;
import com.dcsquare.hivemq.spi.message.PUBLISH;
import com.dcsquare.hivemq.spi.security.ClientData;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the {@link OnPublishReceivedCallback}, which is triggered everytime
 * a new message is published to the broker. This callback enables a custom handling of a
 * MQTT message, for acme saving to a database.
 *
 * @author Christian Goetz
 */
public class PublishReceived implements OnPublishReceivedCallback {

    Logger logger = LoggerFactory.getLogger(PublishReceived.class);

    /**
     * This method is called from the HiveMQ, when a new MQTT {@link PUBLISH} message arrives
     * at the broker. In this acme the method is just logging each message to the console.
     *
     * @param publish    The publish message send by the client.
     * @param clientData Useful information about the clients authentication state and credentials.
     * @throws OnPublishReceivedException When the exception is thrown, the publish is not
     *                                    accepted and will NOT be delivered to the subscribing clients.
     */
    @Override
    public void onPublishReceived(PUBLISH publish, ClientData clientData) throws OnPublishReceivedException {
        String clientID = clientData.getClientId();
        String topic = publish.getTopic();
        String message = new String(publish.getPayload(), Charsets.UTF_8);

        logger.info("Client " + clientID + " sent a message to topic " + topic + ": " + message);
    }

    /**
     * The priority is used when more than one OnConnectCallback is implemented to determine the order.
     * If there is only one callback, which implements a certain interface, the priority has no effect.
     *
     * @return callback priority
     */
    @Override
    public int priority() {
        return CallbackPriority.MEDIUM;
    }
}
