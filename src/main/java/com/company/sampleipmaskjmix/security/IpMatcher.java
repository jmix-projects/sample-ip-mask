package com.company.sampleipmaskjmix.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IpMatcher {
    private static final Set<String> whiteListedIPs = Set.of("127.0.0.1", "0:0:0:0:0:0:0:1");
    private List<String[]> masks = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(IpMatcher.class);

    public IpMatcher(String source) {
        if (!StringUtils.isBlank(source)) {
            String[] parts = source.split("[,;]");
            for (String part : parts) {
                String m = part.trim();
                String[] mask = ipv4(m);
                if (mask == null) {
                    mask = ipv6(m);
                    if (mask == null) {
                        log.warn("Invalid IP mask: '{}'", m);
                        continue;
                    }
                }
                masks.add(mask);
            }
        }
    }

    public boolean match(String ip) {
        if (StringUtils.isBlank(ip) || whiteListedIPs.contains(ip)) {
            return true;
        }
        if (masks.isEmpty()) {
            return true;
        }

        String[] ipv = ipv4(ip);
        if (ipv == null) {
            ipv = ipv6(ip);
            if (ipv == null) {
                log.warn("IP format not supported: '{}'", ip);
                return true;
            }
        }

        for (String[] mask : masks) {
            if (match(mask, ipv))
                return true;
        }
        return false;
    }

    private boolean match(String[] mask, String[] ip) {
        if (mask.length != ip.length) {
            return false;
        }
        for (int j = 0; j < mask.length; j++) {
            String mp = mask[j];
            if (!mp.equals("*") && !ip[j].equals(mp)) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static String[] ipv4(String ip) {
        String[] ipp = ip.split("\\.");
        if (ipp.length != 4) {
            return null;
        }
        return ipp;
    }

    @Nullable
    private static String[] ipv6(String ip) {
        String[] ipp = ip.split(":");
        if (ipp.length != 8) {
            return null;
        }
        return ipp;
    }
}
