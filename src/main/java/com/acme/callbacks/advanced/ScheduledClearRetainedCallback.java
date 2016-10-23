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

import com.hivemq.spi.message.RetainedMessage;
import com.hivemq.spi.services.BlockingRetainedMessageStore;
import com.hivemq.spi.services.RetainedMessageStore;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * This is a scheduled callback, which will be called in the specified interval. The callback is independent
 * of MQTT messages and other stuff. The purpose is to do maintenance stuff or publish stuff regularly.
 * <p>
 * In this example the callback is used to clear all retained messages every 30 seconds.
 * <p>
 * The callback is blocking as we want to guarantee the 30 second interval.
 * <p>
 *
 * @author Christian Götz
 */
public class ScheduledClearRetainedCallback implements com.hivemq.spi.callback.schedule.ScheduledCallback {

    private static final Logger log = LoggerFactory.getLogger(ScheduledClearRetainedCallback.class);
    private final BlockingRetainedMessageStore retainedMessageStore;

    @Inject
    public ScheduledClearRetainedCallback(final BlockingRetainedMessageStore retainedMessageStore) {
        this.retainedMessageStore = retainedMessageStore;
    }

    @Override
    public void execute() {
        /*
         * Careful: BlockingRetainedMessageStore.getRetainedMessages() returns all retained messages from all hivemq
         * cluster nodes (may block for a very long time and the result may use a lot of memory)
         */
        final Set<RetainedMessage> retainedMessages = retainedMessageStore.getRetainedMessages();
        for (final RetainedMessage retainedMessage : retainedMessages) {
            if (retainedMessage != null) {
                retainedMessageStore.remove(retainedMessage.getTopic());
            }
        }
        log.info("All retained messages have been cleared!");
    }

    @Override
    public String cronExpression() {
        // Every 40 seconds
        return "0/40 * * * * ?";
    }
}
