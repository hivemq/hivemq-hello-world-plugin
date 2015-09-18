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

package com.acme.plugin;

import com.acme.callbacks.*;
import com.acme.callbacks.advanced.*;
import com.hivemq.spi.PluginEntryPoint;
import com.hivemq.spi.callback.registry.CallbackRegistry;
import com.hivemq.spi.message.QoS;
import com.hivemq.spi.message.RetainedMessage;
import com.hivemq.spi.services.RetainedMessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * This is the main class of the plugin, which is instanciated during the HiveMQ start up process.
 *
 * @author Christian Götz
 */
public class HelloWorldMainClass extends PluginEntryPoint {

    Logger log = LoggerFactory.getLogger(HelloWorldMainClass.class);

    private final RetainedMessageStore retainedMessageStore;

    private final ClientConnect clientConnect;
    private final PublishReceived publishReceived;
    private final SimpleScheduledCallback simpleScheduledCallback;
    private final ScheduledClearRetainedCallback scheduledClearRetainedCallback;
    private final AddSubscriptionOnClientConnect addSubscriptionOnClientConnect;
    private final SendListOfAllClientsOnPublish sendListOfAllClientsOnPublish;
    private final HiveMQStart hiveMQStart;


    @Inject
    public HelloWorldMainClass(final RetainedMessageStore retainedMessageStore,
                               final ClientConnect clientConnect, final PublishReceived publishReceived,
                               final SimpleScheduledCallback simpleScheduledCallback,
                               final ScheduledClearRetainedCallback scheduledClearRetainedCallback,
                               final AddSubscriptionOnClientConnect addSubscriptionOnClientConnect,
                               final SendListOfAllClientsOnPublish sendListOfAllClientsOnPublish,
                               final HiveMQStart hiveMQStart){
        this.retainedMessageStore = retainedMessageStore;
        this.clientConnect = clientConnect;
        this.publishReceived = publishReceived;
        this.simpleScheduledCallback = simpleScheduledCallback;
        this.scheduledClearRetainedCallback = scheduledClearRetainedCallback;
        this.addSubscriptionOnClientConnect = addSubscriptionOnClientConnect;
        this.sendListOfAllClientsOnPublish = sendListOfAllClientsOnPublish;
        this.hiveMQStart = hiveMQStart;
    }

    /**
     * This method is executed after the instanciation of the whole class. It is used to initialize
     * the implemented callbacks and make them known to the HiveMQ core.
     */
    @PostConstruct
    public void postConstruct() {

        CallbackRegistry callbackRegistry = getCallbackRegistry();
        callbackRegistry.addCallback(hiveMQStart);
        callbackRegistry.addCallback(clientConnect);
        callbackRegistry.addCallback(new ClientDisconnect());
        callbackRegistry.addCallback(publishReceived);
        callbackRegistry.addCallback(simpleScheduledCallback);
        callbackRegistry.addCallback(scheduledClearRetainedCallback);
        callbackRegistry.addCallback(addSubscriptionOnClientConnect);
        callbackRegistry.addCallback(sendListOfAllClientsOnPublish);

        addRetainedMessage("/default", "Hello World.");
    }

    /**
     * Programmatically add a new Retained Message.
     */
    public void addRetainedMessage(String topic, String message) {

        if (!retainedMessageStore.contains(new RetainedMessage(topic, new byte[]{}, QoS.valueOf(0))))
            retainedMessageStore.addOrReplace(new RetainedMessage(topic, message.getBytes(), QoS.valueOf(1)));
    }
}
