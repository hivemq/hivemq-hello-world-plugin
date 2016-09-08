/*
 * Copyright 2015 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acme.callbacks.advanced;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.events.OnConnectCallback;
import com.hivemq.spi.callback.exception.RefusedConnectionException;
import com.hivemq.spi.message.CONNECT;
import com.hivemq.spi.message.QoS;
import com.hivemq.spi.message.Topic;
import com.hivemq.spi.security.ClientData;
import com.hivemq.spi.services.AsyncSubscriptionStore;
import com.hivemq.spi.services.SubscriptionStore;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an acme of a callback, which is invoked every time a new client is
 * successfully authenticated. The callback can be used to execute some custom behavior,
 * which is necessary when a new client connects or to implement a custom logic based
 * on the {@link com.hivemq.spi.message.CONNECT} to refuse the connection throwing a
 * {@link com.hivemq.spi.callback.exception.RefusedConnectionException}.
 * <p>
 * The callback adds a individual subscription to devices/{clientId}/sensor for each connecting client.
 * <p>
 * The callback is using the new, as of SPI 3.1.0, async services.
 * <p>
 *
 * @author Christian Götz
 * @author Georg Held
 */
public class AddSubscriptionOnClientConnect implements OnConnectCallback {

    private final AsyncSubscriptionStore subscriptionStore;
    Logger log = LoggerFactory.getLogger(AddSubscriptionOnClientConnect.class);

    @Inject
    public AddSubscriptionOnClientConnect(final AsyncSubscriptionStore subscriptionStore) {
        this.subscriptionStore = subscriptionStore;
    }

    /**
     * This is the callback method, which is called by the HiveMQ core, if a client has sent,
     * a {@link com.hivemq.spi.message.CONNECT} Message and was successfully authenticated. In this acme there is only
     * a logging statement, normally the behavior would be implemented in here.
     *
     * @param connect    The {@link com.hivemq.spi.message.CONNECT} message from the client.
     * @param clientData Useful information about the clients authentication state and credentials.
     * @throws com.hivemq.spi.callback.exception.RefusedConnectionException This exception should be thrown, if the client is
     *                                                                      not allowed to connect.
     */
    @Override
    public void onConnect(CONNECT connect, ClientData clientData) throws RefusedConnectionException {
        final String clientId = clientData.getClientId();

        log.info("Client {} is connecting", clientId);

        // Adding a subscription without automatically for the client
        addClientToTopic(clientId, "devices/" + clientId + "/sensor");
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

    /**
     * Add a Subscription for a certain client
     */
    private void addClientToTopic(final String clientId, final String topic) {

        //The AsyncSubscriptionStore returns a ListenableFuture object
        final ListenableFuture<Void> adSubscriptionFuture = subscriptionStore.addSubscription(clientId, new Topic(topic, QoS.valueOf(0)));

        //Register callbacks, one for success and one for failure
        Futures.addCallback(adSubscriptionFuture, new FutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                log.info("Added subscription to {} for client {}", topic, clientId);
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("Failed to add subscription to {} for client {} because of {}", topic, clientId, t.getCause());
            }
        });
    }
}
