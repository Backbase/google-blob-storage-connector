package com.backbase.blob.storage.connector.config;

import com.backbase.buildingblocks.context.ContextScoped;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
@ContextScoped
class SecurityConfiguration {

    /**
     * Since we are URL encoding paths as C3ObjectIDs, potentially conflicting sequences can block functionality if
     * default StrictHttpFirewall rules should apply: Example: /my/cool/object.txt -> %2Fmy%2Fcool%2Fobject.txt ->
     * %252Fmy%252Fcool%252Fobject.txt '%25' -> encoded percent is not allowed in default rules
     *
     * @return configured http firewall
     */
    @Bean
    public HttpFirewall relaxedHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedPeriod(true);
        return firewall;
    }
}
