package org.ernest;

import org.ernest.applications.trampoline.entities.Instance;
import org.ernest.applications.trampoline.entities.Microservice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SanitizeActuatorPrefixTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "/actuator", "actuator" },
                { "actuator", "actuator" },
                { "/actuator/otherpath", "actuator/otherpath" },
                { "actuator/otherpath", "actuator/otherpath" },
                { "actuator/otherpath", "actuator/otherpath" },
                { "", "" }
        });
    }

    @Parameterized.Parameter
    public String prefixIntroduced;

    @Parameterized.Parameter(1)
    public String prefixExpected;

    @Test
    public void givenActuatorPrefixWhenSanitizingThenShouldNotHaveFirstDash(){
        Instance instance = new Instance();
        instance.setActuatorPrefix(prefixIntroduced);
        Assert.assertEquals(prefixExpected, instance.getActuatorPrefix());

        Microservice microservice = new Microservice();
        microservice.setActuatorPrefix(prefixIntroduced);
        Assert.assertEquals(prefixExpected, microservice.getActuatorPrefix());
    }
}
