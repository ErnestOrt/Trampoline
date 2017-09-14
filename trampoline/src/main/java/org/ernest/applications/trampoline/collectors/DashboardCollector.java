package org.ernest.applications.trampoline.collectors;


import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.ernest.applications.trampoline.entities.MemoryUsageDto;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

import lombok.Getter;


@Component
@Getter
public class DashboardCollector {

    private final Queue<MemoryUsageDto> memoryUsageQueue = new CircularFifoQueue<>(25);

    @Scheduled(fixedDelay=10000)
    public void collectMetrics() throws JSONException, InterruptedException, CreatingSettingsFolderException, ReadingEcosystemException {

        MemoryUsageDto memoryUsageDto = new MemoryUsageDto();

        memoryUsageDto.setDate(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        memoryUsageDto.setFreeMemoryMB(new DecimalFormat("0.00").format(Runtime.getRuntime().freeMemory()/1000000));
        memoryUsageDto.setTotalMemoryMB(new DecimalFormat("0.00").format(Runtime.getRuntime().totalMemory()/1000000));

        memoryUsageQueue.add(memoryUsageDto);
    }
}
