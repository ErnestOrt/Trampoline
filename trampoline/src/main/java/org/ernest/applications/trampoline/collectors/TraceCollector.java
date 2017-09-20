package org.ernest.applications.trampoline.collectors;

import org.ernest.applications.trampoline.entities.Instance;
import org.ernest.applications.trampoline.entities.TraceActuator;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class TraceCollector {

    @Autowired
    EcosystemManager ecosystemManager;

    public List<TraceActuator> getTraces(String idInstance) throws CreatingSettingsFolderException, ReadingEcosystemException {
        List<TraceActuator> traces = new ArrayList<>();

        Instance instance = ecosystemManager.getEcosystem().getInstances().stream().filter(i -> i.getId().equals(idInstance)).findAny().get();
        JSONArray traceArrayJson = new JSONArray(new RestTemplate().getForObject("http://127.0.0.1:" + instance.getPort() + instance.getActuatorPrefix() + "/trace", String.class));

        for (int i = 0; i < traceArrayJson.length(); i++) {
            JSONObject traceJson = traceArrayJson.getJSONObject(i);

            TraceActuator traceActuator = new TraceActuator();
            traceActuator.setDate(new SimpleDateFormat("HH:mm:ss").format(traceJson.getLong("timestamp")));
            traceActuator.setMethod(traceJson.getJSONObject("info").getString("method"));
            traceActuator.setPath(traceJson.getJSONObject("info").getString("path"));
            traceActuator.setStatus(traceJson.getJSONObject("info").getJSONObject("headers").getJSONObject("response").getString("status"));
            traces.add(traceActuator);
        }

        return traces;
    }
}
