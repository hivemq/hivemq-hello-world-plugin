package com.acme.callbacks;

import com.dcsquare.hivemq.spi.callback.CallbackPriority;
import com.dcsquare.hivemq.spi.callback.events.OnConnectCallback;
import com.dcsquare.hivemq.spi.callback.exception.RefusedConnectionException;
import com.dcsquare.hivemq.spi.message.CONNECT;
import com.dcsquare.hivemq.spi.security.ClientData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an acme of a callback, which is invoked every time a new client is
 * successfully authenticated. The callback can be used to execute some custom behavior,
 * which is necessary when a new client connects or to implement a custom logic based
 * on the {@link CONNECT} to refuse the connection throwing a
 * {@link RefusedConnectionException}.
 *
 * @author Christian Goetz
 */
public class ClientConnect implements OnConnectCallback {

    Logger log = LoggerFactory.getLogger(ClientConnect.class);


    /**
     * This is the callback method, which is called by the HiveMQ core, if a client has sent,
     * a {@link CONNECT} Message and was successfully authenticated. In this acme there is only
     * a logging statement, normally the behavior would be implemented in here.
     *
     * @param connect    The {@link CONNECT} message from the client.
     * @param clientData Useful information about the clients authentication state and credentials.
     * @throws RefusedConnectionException This exception should be thrown, if the client is
     *                                    not allowed to connect.
     */
    @Override
    public void onConnect(CONNECT connect, ClientData clientData) throws RefusedConnectionException {
        log.info("Client {} is connecting", clientData.getClientId());
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
