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
import com.hivemq.spi.services.RetainedMessageStore;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * This is a scheduled callback, which will be called in the specified interval. The callback is independent
 * of MQTT messages and other stuff. The purpose is to do maintenance stuff or publish stuff regularly.
 *
 * In this example the callback is used to clear all retained messages every 30 seconds.
 *
 * @author Christian Götz
 */
public class ScheduledClearRetainedCallback implements com.hivemq.spi.callback.schedule.ScheduledCallback {

    private static final Logger log = LoggerFactory.getLogger(ScheduledClearRetainedCallback.class);
    private final RetainedMessageStore retainedMessageStore;

    @Inject
    public ScheduledClearRetainedCallback(final RetainedMessageStore retainedMessageStore) {
        this.retainedMessageStore = retainedMessageStore;
    }

    @Override
    public void execute() {
        final Set<RetainedMessage> retainedMessages = retainedMessageStore.getRetainedMessages();
        for (RetainedMessage retainedMessage : retainedMessages) {
            retainedMessageStore.remove(retainedMessage);
        }
        log.info("All retained messages have been cleared!");
    }

    @Override
    public String cronExpression() {
        // Every 40 seconds
        return "0/40 * * * * ?";
    }
}
