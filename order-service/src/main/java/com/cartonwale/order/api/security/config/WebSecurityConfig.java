package com.cartonwale.order.api.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cartonwale.common.model.Permission;
import com.cartonwale.common.security.CommonAuthenticationEntryPoint;
import com.cartonwale.common.security.CommonAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CommonAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public CommonAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new CommonAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				.antMatchers(HttpMethod.GET, "/orders/placedOrders").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.PUT, "/orders/abc/recentOrders").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN, Permission.USER_CONSUMER)
				.antMatchers(HttpMethod.GET, "/orders/provider").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.GET, "/orders/consumer/requirements").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.GET, "/orders/provider/completed").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.GET, "/orders/consumer/completed").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.GET, "/orders/provider/dashboard").hasAnyRole(Permission.USER_PROVIDER)
				.antMatchers(HttpMethod.GET, "/orders/consumer/dashboard").hasAnyRole(Permission.USER_CONSUMER)
				.antMatchers(HttpMethod.GET, "/orders/toUpdate").hasRole(Permission.USER_ADMIN)
                .antMatchers(HttpMethod.GET, "/orders/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN, Permission.USER_PROVIDER)
                .antMatchers(HttpMethod.POST, "/orders/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
                .antMatchers(HttpMethod.PUT, "/orders/changeOrderStatus/**").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.PUT, "/orders/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)

				.antMatchers(HttpMethod.GET, "/quotes/provider").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN)
                .antMatchers(HttpMethod.GET, "/quotes/order/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.POST, "/quotes/award/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.GET, "/quotes/**").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN, Permission.USER_CONSUMER)
                .antMatchers(HttpMethod.POST, "/quotes/**").hasAnyRole(Permission.USER_PROVIDER)
				.antMatchers(HttpMethod.PUT, "/quotes/**").hasAnyRole(Permission.USER_PROVIDER)
				
				.antMatchers(HttpMethod.PUT, "/cart/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
				.antMatchers(HttpMethod.GET, "/cart/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
                
                //authenticated requests
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
    
}
