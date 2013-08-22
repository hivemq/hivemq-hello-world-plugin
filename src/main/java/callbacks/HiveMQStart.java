package callbacks;

import com.dcsquare.hivemq.spi.callback.CallbackPriority;
import com.dcsquare.hivemq.spi.callback.events.broker.OnBrokerStart;
import com.dcsquare.hivemq.spi.callback.exception.BrokerUnableToStartException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the {@link OnBrokerStart} callback, which is invoked when HiveMQ is
 * starting. It can be used to execute custom plugin or system initialization stuff.
 *
 * @author Christian Goetz
 */
public class HiveMQStart implements OnBrokerStart {

    Logger log = LoggerFactory.getLogger(HiveMQStart.class);

    /**
     * This method is called from HiveMQ, and the custom behaviour has to be implemented in here.
     * If some preconditions are not met to successfully operate, a {@link BrokerUnableToStartException}
     * should be thrown.
     *
     * @throws BrokerUnableToStartException If the exception is thrown, HiveMQ will be stopped.
     */
    @Override
    public void onBrokerStart() throws BrokerUnableToStartException {
        log.info("HiveMQ is starting");
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
