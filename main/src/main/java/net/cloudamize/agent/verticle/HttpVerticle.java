package net.cloudamize.agent.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import net.cloudamize.agent.SpringBootApp;
import net.cloudamize.agent.routes.MainRouter;
import net.cloudamize.agent.service.AgentStatusService;
import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by sushiljoshi on 25/04/16.
 */

@Component
@SpringVerticle(springConfig = SpringBootApp.class)
@Slf4j
public class HttpVerticle extends AbstractVerticle {

    //private static final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);

    @Inject
    private MainRouter mainRouter;

    @Inject
    private AgentStatusService agentStatusService;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);
        /*router.get("/*").handler(req -> {
            log.info("Endpoint Called on thread " + Thread.currentThread().getName());
            req.response().end("Server is up and running!");
        });*/

        router.get("/api").handler(mainRouter::handleJooq);


        mainRouter.setRouter(router);

        vertx.executeBlocking(future -> {
            agentStatusService.setHighestCustId();
            log.info("Setting Cust ID to " + agentStatusService.getCurrentCustId());

            HttpServerOptions httpOpts = new HttpServerOptions();
            httpOpts.setKeyStoreOptions(new JksOptions()
                    .setPassword(config().getString("certificate-password"))
                    .setPath(config().getString("certificate-path")));
            httpOpts.setSsl(true);
            future.complete(httpOpts);
        }, (AsyncResult<HttpServerOptions> result) -> {
            if (!result.failed()) {
                vertx.createHttpServer(result.result()).requestHandler(
                        mainRouter.getRouter()::accept
                ).listen(config().getInteger("https.port"));
                startFuture.complete();
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }

}
