package com.company.sampleipmaskjmix.security;

import com.company.sampleipmaskjmix.entity.User;
import io.jmix.core.Messages;
import io.jmix.core.security.event.PreAuthenticationCheckEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("sec_IpMaskAuthenticationChecks")
public class IpMaskValidationChecks {

    @Autowired(required = false)
    private HttpServletRequest httpRequest;
    @Autowired
    private Messages messages;

    @EventListener
    public void onPreAuthenticationCheck(PreAuthenticationCheckEvent event) {
        if (isProtectionEnabled()) {
            UserDetails userDetails = event.getUser();
            String mask = ((User) userDetails).getIpMask();
            if (!isValid(getIpAddress(), mask)) {
                throw new LockedException(messages.getMessage("com.company.sampleipmaskjmix.screen.login.invalidIP"));
            }
        }
    }

    private boolean isValid(String validatingIp, String ipMask) {
        return new IpMatcher(ipMask).match(validatingIp);
    }

    private boolean isProtectionEnabled() {
        return true;
    }

    private String getIpAddress() {
        if (httpRequest != null) {
            String xForwardedHeader = httpRequest.getHeader("X-Forwarded-For");
            if (xForwardedHeader == null) {
                return httpRequest.getRemoteAddr();
            } else {
                return xForwardedHeader.split(",")[0];
            }
        }
        return "";
    }
}

