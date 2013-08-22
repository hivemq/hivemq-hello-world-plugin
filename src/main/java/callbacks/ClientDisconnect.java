package callbacks;

import com.dcsquare.hivemq.spi.callback.events.OnDisconnectCallback;
import com.dcsquare.hivemq.spi.security.ClientData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the OnDisconnectCallback, which is invoke everytime a client disconnects.
 * The callback allows to implement custom logic, which should be executed after a disconnect.
 *
 * @author Christian Goetz
 */
public class ClientDisconnect implements OnDisconnectCallback {

    Logger log = LoggerFactory.getLogger(ClientDisconnect.class);

    /**
     * This method is called from the HiveMQ on a client disconnect.
     *
     * @param clientData       Useful information about the clients authentication state and credentials.
     * @param abruptDisconnect When true the connection of the client broke down without a
     *                         {@link com.dcsquare.hivemq.spi.message.DISCONNECT} message and if false then the client
     *                         disconnected properly with a {@link com.dcsquare.hivemq.spi.message.DISCONNECT} message.
     */
    @Override
    public void onDisconnect(ClientData clientData, boolean abruptDisconnect) {
        log.info("Client {} is disconnected", clientData.getClientId());
    }
}
