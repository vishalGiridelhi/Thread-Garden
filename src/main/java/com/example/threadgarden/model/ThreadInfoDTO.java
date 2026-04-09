package com.example.threadgarden.model;

public class ThreadInfoDTO {

    private long threadId;
    private String name;
    private String state;
    private long cpuTime;
    private Long lockOwnerId; // NEW

    public ThreadInfoDTO(long threadId, String name, String state, long cpuTime, Long lockOwnerId) {
        this.threadId = threadId;
        this.name = name;
        this.state = state;
        this.cpuTime = cpuTime;
        this.lockOwnerId = lockOwnerId;
    }

    public long getThreadId() { return threadId; }
    public String getName() { return name; }
    public String getState() { return state; }
    public long getCpuTime() { return cpuTime; }
    public Long getLockOwnerId() { return lockOwnerId; }

    // NEW (required for demo edges)
    public void setLockOwnerId(Long lockOwnerId) {
        this.lockOwnerId = lockOwnerId;
    }
}