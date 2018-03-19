package org.ernest.applications.trampoline.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VMParser {

    public static final String REGEX_VM_ARGUMENTS = "[-D](.*?)\\=";

    public static String toUnixEnviromentVariables(String vmArguments){
        Pattern pattern = Pattern.compile(REGEX_VM_ARGUMENTS);
        Matcher matcher = pattern.matcher(vmArguments);

        StringBuffer upperCaseStringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(upperCaseStringBuffer, matcher.group().toUpperCase());
        }
        matcher.appendTail(upperCaseStringBuffer);

        String strResult = upperCaseStringBuffer.toString();
        strResult = strResult.replaceAll("-D", "; export ");
        strResult = strResult.replaceAll("\\.", "_");
        return strResult;
    }

    public static String toWindowsEnviromentVariables(String vmArguments){
        Pattern pattern = Pattern.compile(REGEX_VM_ARGUMENTS);
        Matcher matcher = pattern.matcher(vmArguments);

        StringBuffer upperCaseStringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(upperCaseStringBuffer, matcher.group().toUpperCase());
        }
        matcher.appendTail(upperCaseStringBuffer);

        String strResult = upperCaseStringBuffer.toString();
        strResult = strResult.replaceAll("-D", "&& SET ");
        strResult = strResult.replaceAll("\\.", "_");
        return strResult;
    }
}
