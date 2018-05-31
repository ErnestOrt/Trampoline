package org.ernest.applications.trampoline.collectors;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.ernest.applications.trampoline.entities.Instance;
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

import java.net.ConnectException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MetricsCollector {

    private static final Logger log = LoggerFactory.getLogger(MetricsCollector.class);

    @Autowired
    EcosystemManager ecosystemManager;

    private Map<String, Queue<Metrics>> metricsMap = new HashMap<>();

    @Scheduled(fixedDelay=30000)
    public void collectMetrics() throws JSONException, InterruptedException, CreatingSettingsFolderException, ReadingEcosystemException {

        ecosystemManager.getEcosystem().getInstances().forEach(instance ->{
            try {
                Metrics metrics;
                try {
                    metrics = buildMetricsFromJsonResponseV1x(instance);
                }catch (Exception e){
                    metrics = buildMetricsFromJsonResponseV2x(instance);
                }

                if (metricsMap.containsKey(instance.getId())) {
                    metricsMap.get(instance.getId()).add(metrics);
                } else {
                    Queue<Metrics> queue = new CircularFifoQueue(20);
                    queue.add(metrics);
                    metricsMap.put(instance.getId(), queue);
                }
            }catch (Exception  e){
                log.error("Not possible to retrieve metrics for instance: ["+instance.getId()+"] hosted on port: ["+instance.getPort()+"]");
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
                return !ecosystemManager.getEcosystem().getInstances().stream().anyMatch(i -> i.getId().equals(id));
            } catch (CreatingSettingsFolderException e) {
                e.printStackTrace();
            } catch (ReadingEcosystemException e) {
                e.printStackTrace();
            }
            return true;
        }).collect(Collectors.toList());

        idsToBeDeleted.stream().forEach(id-> metricsMap.remove(id));
    }

    private Metrics buildMetricsFromJsonResponseV1x(Instance instance) throws JSONException {
        Metrics metrics = new Metrics();

        String url = "http://"+instance.getIp()+":" + instance.getPort() + instance.getActuatorPrefix() + "/metrics";
        log.info("Reading metrics Spring Boot 1.x for instance id: [{}] using url: [{}]", instance.getId(), url);

        JSONObject metricsJson = new JSONObject(new RestTemplate().getForObject(url, String.class));
        metrics.setTotalMemoryKB(Long.valueOf(metricsJson.get("mem").toString()));
        metrics.setFreeMemoryKB(Long.valueOf(metricsJson.get("mem.free").toString()));
        metrics.setHeapKB(Long.valueOf(metricsJson.get("heap").toString()));
        metrics.setInitHeapKB(Long.valueOf(metricsJson.get("heap.init").toString()));
        metrics.setUsedHeapKB(Long.valueOf(metricsJson.get("heap.used").toString()));
        metrics.setDate(new SimpleDateFormat("HH:mm:ss").format(new Date()));

        return metrics;
    }

    private Metrics buildMetricsFromJsonResponseV2x(Instance instance) throws JSONException {
        log.info("Reading metrics Spring Boot 2.x for instance id: [{}]", instance.getId());

        Metrics metrics = new Metrics();
        metrics.setTotalMemoryKB(getValueMetric(instance, "jvm.memory.max"));
        metrics.setHeapKB(0L);
        metrics.setInitHeapKB(0L);
        metrics.setUsedHeapKB(getValueMetric(instance, "jvm.memory.used"));
        metrics.setFreeMemoryKB(metrics.getTotalMemoryKB() - metrics.getUsedHeapKB());
        metrics.setDate(new SimpleDateFormat("HH:mm:ss").format(new Date()));

        return metrics;
    }

    private Long getValueMetric(Instance instance, String key) throws JSONException {
        JSONObject metricsJson = new JSONObject(new RestTemplate().getForObject("http://"+instance.getIp()+":" + instance.getPort() + instance.getActuatorPrefix() + "/metrics/"+key, String.class));
        return Long.valueOf(metricsJson.getJSONArray("measurements").getJSONObject(0).getInt("value"));
    }
}


