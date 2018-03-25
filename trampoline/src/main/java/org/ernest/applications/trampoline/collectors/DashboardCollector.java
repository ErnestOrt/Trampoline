package org.ernest.applications.trampoline.collectors;


import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.ernest.applications.trampoline.entities.MemoryUsageDto;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.services.FileManager;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

@Component
public class DashboardCollector {

    private final Logger log = LoggerFactory.getLogger(DashboardCollector.class);

    private Queue<MemoryUsageDto> memoryUsageQueue = new CircularFifoQueue(25);

    @Scheduled(fixedDelay=10000)
    public void collectMetrics() throws JSONException, InterruptedException, CreatingSettingsFolderException, ReadingEcosystemException {
        log.info("Collecting Memory Dashboard Information");

        MemoryUsageDto memoryUsageDto = new MemoryUsageDto();
        memoryUsageDto.setDate(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        memoryUsageDto.setFreeMemoryMB(new DecimalFormat("0.00").format(Runtime.getRuntime().freeMemory()/1000000));
        memoryUsageDto.setTotalMemoryMB(new DecimalFormat("0.00").format(Runtime.getRuntime().totalMemory()/1000000));
        memoryUsageQueue.add(memoryUsageDto);
    }

    public Queue<MemoryUsageDto> getMemoryUsageQueue() {
        return memoryUsageQueue;
    }
}
