package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "target_information")
public class TargetInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String boardName;

    private String architecture;

    private String interfaceMethod;

    private String debuggerName;

    private String executableSize;

    private String bit;

    private String ramUsage;

    private String ramFreeSize;

    private String flashFreeSize;

    @Builder
    public TargetInformation(long idx,
                             String tableName,
                             String boardName,
                             String architecture,
                             String interfaceMethod,
                             String debuggerName,
                             String executableSize,
                             String bit,
                             String ramUsage,
                             String ramFreeSize,
                             String flashFreeSize) {
        setIdx(idx);
        this.tableName = tableName;
        this.boardName = boardName;
        this.architecture = architecture;
        this.interfaceMethod = interfaceMethod;
        this.debuggerName = debuggerName;
        this.executableSize = executableSize;
        this.bit = bit;
        this.ramUsage = ramUsage;
        this.ramFreeSize = ramFreeSize;
        this.flashFreeSize = flashFreeSize;
    }

    public void update(TargetInformation targetInformation) {
        this.tableName = targetInformation.getTableName();
        this.boardName = targetInformation.getBoardName();
        this.architecture = targetInformation.getArchitecture();
        this.interfaceMethod = targetInformation.getInterfaceMethod();
        this.debuggerName = targetInformation.getDebuggerName();
        this.executableSize = targetInformation.getExecutableSize();
        this.bit = targetInformation.getBit();
        this.ramUsage = targetInformation.getRamUsage();
        this.ramFreeSize = targetInformation.getRamFreeSize();
        this.flashFreeSize = targetInformation.getFlashFreeSize();
    }
}