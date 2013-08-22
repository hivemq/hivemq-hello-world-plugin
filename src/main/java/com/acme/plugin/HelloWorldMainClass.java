package com.acme.plugin;

import com.acme.callbacks.ClientConnect;
import com.acme.callbacks.ClientDisconnect;
import com.acme.callbacks.HiveMQStart;
import com.acme.callbacks.PublishReceived;
import com.dcsquare.hivemq.spi.PluginEntryPoint;
import com.dcsquare.hivemq.spi.callback.registry.CallbackRegistry;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * This is the main class of the plugin, which is instanciated during the HiveMQ start up process.
 */
public class HelloWorldMainClass extends PluginEntryPoint {

    Logger log = LoggerFactory.getLogger(HelloWorldMainClass.class);

    private final Configuration configuration;

    /**
     * @param configuration Injected configuration, which is declared in the {@link HelloWorldPluginModule}.
     */
    @Inject
    public HelloWorldMainClass(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * This method is executed after the instanciation of the whole class. It is used to initialize
     * the implemented callbacks and make them known to the HiveMQ core.
     */
    @PostConstruct
    public void postConstruct() {

        CallbackRegistry callbackRegistry = getCallbackRegistry();

        callbackRegistry.addCallback(new HiveMQStart());
        callbackRegistry.addCallback(new ClientConnect());
        callbackRegistry.addCallback(new ClientDisconnect());
        callbackRegistry.addCallback(new PublishReceived());

        log.info("Plugin configuration property: {}", configuration.getString("myProperty"));
    }
}
