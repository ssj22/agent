package net.cloudamize.agent.model;

import lombok.Data;
import net.cloudamize.agent.model.tables.records.AgentStatusRecord;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class AgentStatusVO implements Serializable {
    private Integer id;
    private Integer customerId;
    private String instanceId;
    private String currentAgentStatus;
    private String preferredAgentStatus;
    private String currentVersion;
    private String preferredVersion;
    private String upgradeAction;
    private Double cpuPercent;
    private Double memoryPercent;
    private String diskDirOutKb;
    private String diskDirBinKb;
    private String diskDirLogsKb;
    private Timestamp updatedDate;
    private java.sql.Timestamp createdDate;
    private String diskDirClOutKb;
    private String diskDirOflOutKb;
    private String currentClStatus;
    private String preferredClStatus;
    private String currentOflStatus;
    private String preferredOflStatus;
    private Double clCpuPercent;
    private Double clMemoryPercent;
    private Double oflCpuPercent;
    private Double oflMemoryPercent;
    private Boolean onprem;
    private Long accountId;
    private String operation;
    private Integer beingProcessedCnt;
    private Integer numberOfAttempts;
    private Object workerIpAddress;
    private Integer workerPid;
    private Timestamp processingStartTime;
    private Timestamp lastHeartbeatTime;
    private Timestamp dataProcessedUpTo;
    private Boolean isVisible;
    private Boolean isReadyForProcessing;
    private Boolean hasDataError;
    private String message;
    private Timestamp lastUploadDate;

    public AgentStatusVO(AgentStatusRecord record) {
        this.id = record.getId();
        this.customerId = record.getCustomerId();
        this.instanceId = record.getInstanceId();
        this.currentAgentStatus = record.getCurrentAgentStatus();
        this.preferredAgentStatus = record.getPreferredAgentStatus();
        this.currentVersion = record.getCurrentVersion();
        this.preferredVersion = record.getPreferredVersion();
        this.upgradeAction = record.getUpgradeAction();
        this.cpuPercent = record.getCpuPercent();
        this.memoryPercent = record.getMemoryPercent();
        this.diskDirOutKb = record.getDiskDirOutKb();
        this.diskDirBinKb = record.getDiskDirBinKb();
        this.diskDirLogsKb = record.getDiskDirLogsKb();
        this.updatedDate = record.getUpdatedDate();
        this.createdDate = record.getCreatedDate();
        this.diskDirClOutKb = record.getDiskDirClOutKb();
        this.diskDirOflOutKb = record.getDiskDirOflOutKb();
        this.currentClStatus = record.getCurrentClStatus();
        this.preferredClStatus = record.getPreferredClStatus();
        this.currentOflStatus = record.getCurrentOflStatus();
        this.preferredOflStatus = record.getPreferredOflStatus();
        this.clCpuPercent = record.getClCpuPercent();
        this.clMemoryPercent = record.getClMemoryPercent();
        this.oflCpuPercent = record.getOflCpuPercent();
        this.oflMemoryPercent = record.getOflMemoryPercent();
        this.onprem = record.getOnprem();
        this.accountId = record.getAccountId();
        this.operation = record.getOperation();
        this.beingProcessedCnt = record.getBeingProcessedCnt();
        this.numberOfAttempts = record.getNumberOfAttempts();
        this.workerIpAddress = record.getWorkerIpAddress();
        this.workerPid = record.getWorkerPid();
        this.processingStartTime = record.getProcessingStartTime();
        this.lastHeartbeatTime = record.getLastHeartbeatTime();
        this.dataProcessedUpTo = record.getDataProcessedUpTo();
        this.isVisible = record.getIsVisible();
        this.isReadyForProcessing = record.getIsReadyForProcessing();
        this.hasDataError = record.getHasDataError();
        this.message = record.getMessage();
        this.lastUploadDate = record.getLastUploadDate();
    }

}
