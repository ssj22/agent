package net.cloudamize.agent.service;

import lombok.extern.slf4j.Slf4j;
import net.cloudamize.agent.model.AgentStatusVO;
import net.cloudamize.agent.model.Tables;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static net.cloudamize.agent.model.tables.AgentStatus.AGENT_STATUS;

@Repository
@Slf4j
public class AgentStatusService {

    private AtomicInteger custId = new AtomicInteger(2);

    public Integer getCurrentCustId() {
        return custId.get();
    }

    @Autowired
    private DSLContext masterContext;

    public void setHighestCustId() {
        Record record = masterContext
                .select(AGENT_STATUS.CUSTOMER_ID)
                .from(AGENT_STATUS)
                .orderBy(AGENT_STATUS.CUSTOMER_ID.desc())
                .limit(0,1)
                .fetchOne();
        custId.set(record.getValue(AGENT_STATUS.CUSTOMER_ID));
    }

    @Transactional
    public List<AgentStatusVO> getAllAgentStatuses() {
        Result<Record> records = masterContext
                .select()
                .from(AGENT_STATUS)
                .orderBy(AGENT_STATUS.CUSTOMER_ID.desc())
                .fetch();
        return records.stream()
                .map(r -> new AgentStatusVO(r.into(Tables.AGENT_STATUS)))
                .collect(Collectors.toList());

    }

    public AgentStatusVO getAgentStatus(Integer customerId, String instanceId) {
        Record record = masterContext
                .select()
                .from(AGENT_STATUS)
                .where(AGENT_STATUS.CUSTOMER_ID.eq(customerId))
                .and(AGENT_STATUS.INSTANCE_ID.eq(instanceId))
                .fetchOne();
        return record == null?null:new AgentStatusVO(record.into(Tables.AGENT_STATUS));
    }

    /*public void saveAgentStatus(AgentStatusVO agentStatus) {
        Integer customerId = custId.incrementAndGet();
        String instanceId = "v_232893";

        AgentStatusVO dbRecord = getAgentStatus(customerId, instanceId);

        if (dbRecord == null) {
            Record record = masterContext.insertInto(AGENT_STATUS, AGENT_STATUS.CUSTOMER_ID, AGENT_STATUS.INSTANCE_ID)
                    .values(customerId, instanceId)
                    .returning(AGENT_STATUS.CUSTOMER_ID, AGENT_STATUS.INSTANCE_ID)
                    .fetchOne();
            log.info("CustID Return value from Insert Statement = " + record.getValue(AGENT_STATUS.CUSTOMER_ID));
        }
        else {
            log.warn("Record already exists");
        }
    }*/
}
