package org.ernest.applications.trampoline.utils;

import org.ernest.applications.trampoline.collectors.TraceCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class PortsChecker {

    private static final Logger log = LoggerFactory.getLogger(PortsChecker.class);

    public static boolean available(int port) {
        log.info("Checking port [{}]", port);
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) { ds.close(); }
            if (ss != null) { try { ss.close(); } catch (IOException e) {}}
        }
        return false;
    }
}
