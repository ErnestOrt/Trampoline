package org.ernest.applications.trampoline.collectors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.ernest.applications.trampoline.entities.Instance;
import org.ernest.applications.trampoline.entities.TraceActuator;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.val;


@Component
@AllArgsConstructor
public class TraceCollector {

    private final EcosystemManager ecosystemManager;

    public List<TraceActuator> getTraces(String idInstance) throws CreatingSettingsFolderException, ReadingEcosystemException {
        final List<TraceActuator> traces = new ArrayList<>();

        val instanceOptional = ecosystemManager.getEcosystem().getInstances().stream().filter(i -> i.getId().equals(idInstance)).findAny();
        if (instanceOptional.isPresent()){
            val instance = instanceOptional.get();
            try {
                val traceArrayJson = new JSONArray(new RestTemplate().getForObject("http://127.0.0.1:" + instance.getPort() + instance.getActuatorPrefix() + "/trace", String.class));

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
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
