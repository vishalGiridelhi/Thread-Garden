package com.example.threadgarden.service;

import com.example.threadgarden.model.ThreadInfoDTO;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.lang.management.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThreadMonitorService {

    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    @PostConstruct
    public void init() {
        if (!threadMXBean.isThreadCpuTimeEnabled()) {
            threadMXBean.setThreadCpuTimeEnabled(true);
        }
    }

    public List<ThreadInfoDTO> getThreadData() {

        long[] threadIds = threadMXBean.getAllThreadIds();
        ThreadInfo[] infos = threadMXBean.getThreadInfo(threadIds, true, true);

        List<ThreadInfoDTO> result = new ArrayList<>();

        for (ThreadInfo info : infos) {
            if (info == null) continue;

            long cpuTime = threadMXBean.getThreadCpuTime(info.getThreadId());
            Long lockOwner = info.getLockOwnerId() == -1 ? null : info.getLockOwnerId();

            result.add(new ThreadInfoDTO(
                    info.getThreadId(),
                    info.getThreadName(),
                    info.getThreadState().name(),
                    cpuTime < 0 ? 0 : cpuTime,
                    lockOwner
            ));
        }

        //todo - will remove
        if (!result.isEmpty()) {
            long mainThreadId = result.get(0).getThreadId();

            for (ThreadInfoDTO t : result) {
                if (t.getLockOwnerId() == null && t.getThreadId() != mainThreadId) {
                    t.setLockOwnerId(mainThreadId);
                }
            }
        }

        return result;
    }

    // NEW (deadlock detection)
    public Set<Long> getDeadlockedThreads() {
        long[] ids = threadMXBean.findDeadlockedThreads();
        if (ids == null) return Collections.emptySet();

        return Arrays.stream(ids).boxed().collect(Collectors.toSet());
    }
}