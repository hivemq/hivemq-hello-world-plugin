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

package com.acme.callbacks;

import com.acme.configuration.Configuration;
import com.google.inject.Inject;
import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.events.broker.OnBrokerStart;
import com.hivemq.spi.callback.exception.BrokerUnableToStartException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the {@link OnBrokerStart} callback, which is invoked when HiveMQ is
 * starting. It can be used to execute custom plugin or system initialization stuff.
 *
 * @author Christian Götz
 */
public class HiveMQStart implements OnBrokerStart {

    private final Configuration pluginConfiguration;
    Logger log = LoggerFactory.getLogger(HiveMQStart.class);

    @Inject
    public HiveMQStart(Configuration pluginConfiguration) {
        this.pluginConfiguration = pluginConfiguration;
    }

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
        log.info("Property from property file is: " + pluginConfiguration.getMyProperty());
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
