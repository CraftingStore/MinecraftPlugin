package net.craftingstore.velocity.logging;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.craftingstore.core.logging.CraftingStoreLogger;
import org.slf4j.Logger;

@Singleton
public class Slf4jLogger extends CraftingStoreLogger {

    @Inject
    private Logger logger;

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void error(String message) {
        this.logger.error(message);
    }
}
