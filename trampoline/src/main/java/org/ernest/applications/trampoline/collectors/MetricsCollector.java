package org.ernest.applications.trampoline.collectors;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.ernest.applications.trampoline.entities.Metrics;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MetricsCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsCollector.class);

    @Autowired
    EcosystemManager ecosystemManager;

    private Map<String, Queue<Metrics>> metricsMap = new HashMap<>();

    @Scheduled(fixedDelay=30000)
    public void collectMetrics() throws JSONException, InterruptedException, CreatingSettingsFolderException, ReadingEcosystemException {

        ecosystemManager.getEcosystem().getInstances().forEach(instance ->{
            try {
                JSONObject metricsJson = new JSONObject(new RestTemplate().getForObject("http://127.0.0.1:" + instance.getPort() + instance.getActuatorPrefix() + "/metrics", String.class));
                Metrics metrics = buildMetricsFromJsonResponse(metricsJson);

                if (metricsMap.containsKey(instance.getId())) {
                    metricsMap.get(instance.getId()).add(metrics);
                } else {
                    Queue<Metrics> queue = new CircularFifoQueue<>(20);
                    queue.add(metrics);
                    metricsMap.put(instance.getId(), queue);
                }
            }catch (Exception e){
                LOGGER.error("Not possible to retrieve metrics for instance: ["+instance.getId()+"] hosted on port: ["+instance.getPort()+"]");
            }
        });

        removeNotActiveInstances();
    }

    public Queue<Metrics> getInstanceMetrics(String id){
        return metricsMap.get(id);
    }

    private void removeNotActiveInstances() {
        List<String> idsToBeDeleted = metricsMap.keySet().stream().filter(id -> {
            try {
                return ecosystemManager.getEcosystem().getInstances().stream().noneMatch(i -> i.getId().equals(id));
            } catch (CreatingSettingsFolderException e) {
                e.printStackTrace();
            } catch (ReadingEcosystemException e) {
                e.printStackTrace();
            }
            return true;
        }).collect(Collectors.toList());

        idsToBeDeleted.forEach(id-> metricsMap.remove(id));
    }

    //TODO should be marshaled via jackson or Gson
    private Metrics buildMetricsFromJsonResponse(JSONObject metricsJson) {
        Metrics metrics = new Metrics();
        try
        {
            metrics.setTotalMemoryKB(Long.valueOf(metricsJson.get("mem").toString()));
            metrics.setFreeMemoryKB(Long.valueOf(metricsJson.get("mem.free").toString()));
            metrics.setHeapKB(Long.valueOf(metricsJson.get("heap").toString()));
            metrics.setInitHeapKB(Long.valueOf(metricsJson.get("heap.init").toString()));
            metrics.setUsedHeapKB(Long.valueOf(metricsJson.get("heap.used").toString()));
            metrics.setDate(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }
        catch (Exception e)
        {
            LOGGER.error("Error while reconstruction Metrics", e);
        }
        return metrics;
    }
}
