package sit.int222.cfan.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sit.int222.cfan.repositories.JwtblacklistRepository;
import sit.int222.cfan.services.TokenService;

public class TokenFilterConfiguerer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenService service;
    private final JwtblacklistRepository jwtblacklistRepository;

    public TokenFilterConfiguerer(TokenService service, JwtblacklistRepository jwtblacklistRepository) {
        this.service = service;
        this.jwtblacklistRepository = jwtblacklistRepository;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        TokenFilter filter = new TokenFilter(service,jwtblacklistRepository);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
