package net.cloudamize.agent.verticle;

import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;
import net.cloudamize.agent.SpringBootApp;
import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.stereotype.Component;

@Component
@SpringVerticle(springConfig = SpringBootApp.class)
@Slf4j
public class SetupVerticle extends AbstractVerticle {



    @Override
    public void start() throws Exception {

    }
}
