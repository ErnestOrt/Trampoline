package org.ernest;


import org.ernest.applications.trampoline.utils.VMParser;
import org.junit.Assert;
import org.junit.Test;

public class VMParserTest {

    @Test
    public void givenVMArgsWhenParsingToWindowsEnvVariableThemThenResultExpected(){
        Assert.assertEquals("&& SET server.port=true", VMParser.toWindowsEnviromentVariables("-Dserver.port=true"));
        Assert.assertEquals("&& SET server.port=true && SET argument=false", VMParser.toWindowsEnviromentVariables("-Dserver.port=true -Dargument=false"));
        System.out.println(VMParser.toWindowsEnviromentVariables("-Dserver.port=true -Dargument=false"));
    }

    @Test
    public void givenVMArgsWhenParsingToUnixEnvVariableThemThenResultExpected(){
        Assert.assertEquals("; export SERVER_PORT=true", VMParser.toUnixEnviromentVariables("-Dserver.port=true"));
        Assert.assertEquals("; export SERVER_PORT=true ; export ARGUMENT=false", VMParser.toUnixEnviromentVariables("-Dserver.port=true -Dargument=false"));
    }
}