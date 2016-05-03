package net.cloudamize.agent.routes;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cloudamize.agent.model.AgentStatusVO;
import net.cloudamize.agent.service.AgentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by sushiljoshi on 27/04/16.
 */
@Component
@Data
@Slf4j
public class MainRouter {
    private Router router;

    @Autowired
    private AgentStatusService agentStatusService;

    public MainRouter() {}
    public MainRouter(Router router) {
        this.router = router;
    }

    public void handleJooq(RoutingContext context) {
        List<AgentStatusVO> agents = agentStatusService.getAllAgentStatuses();
        log.info("Agent Status = " + (agents==null?"Null": agents.size()));
        if (agents != null) {
            AgentStatusVO agent = agentStatusService.getAgentStatus(agents.get(0).getCustomerId(), agents.get(0).getInstanceId());

            //agentStatusService.saveAgentStatus(null);
            log.info("Agent.instanceId = " + agent.getInstanceId());
        }

        context.response().end("New Response");
    }

    public void mountRouter(String resourceUrl, Router router) {
        this.router.mountSubRouter(resourceUrl, router);
    }

}
