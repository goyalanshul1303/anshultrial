package com.cartonwale.product.api.security.config;

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
                
                .antMatchers(HttpMethod.GET, "/product/{consumerId}/{id}/**").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
                .antMatchers(HttpMethod.GET, "/product/{id}/**").hasAnyRole(Permission.USER_PROVIDER)
                .antMatchers(HttpMethod.GET, "/product").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
                .antMatchers(HttpMethod.GET, "/product/getByIds").hasAnyRole(Permission.USER_CONSUMER, Permission.USER_ADMIN)
                .antMatchers(HttpMethod.GET, "/product/acceptingOffers").hasAnyRole(Permission.USER_PROVIDER, Permission.USER_ADMIN)
                .antMatchers(HttpMethod.GET, "/product/consumer/{consumerId}/**").hasAnyRole(Permission.USER_ADMIN, Permission.USER_PROVIDER, Permission.USER_CONSUMER)
                .antMatchers(HttpMethod.GET, "/product/provider/{providerId}/**").hasAnyRole(Permission.USER_ADMIN, Permission.USER_PROVIDER, Permission.USER_CONSUMER)
                .antMatchers(HttpMethod.POST, "/product/**").hasAnyRole(Permission.USER_ADMIN)
				.antMatchers(HttpMethod.PUT, "/product/**").hasAnyRole(Permission.USER_ADMIN, Permission.USER_CONSUMER)
				
				.antMatchers(HttpMethod.PUT, "/price/startAcceptingOffers").hasAnyRole(Permission.USER_ADMIN)
                .antMatchers(HttpMethod.PUT, "/price/stopAcceptingOffers").hasAnyRole(Permission.USER_ADMIN)
                .antMatchers(HttpMethod.PUT, "/price/addOffer/**").hasAnyRole(Permission.USER_PROVIDER)
                .antMatchers(HttpMethod.PUT, "/price/{productId}/{price}").hasAnyRole(Permission.USER_ADMIN)
                .antMatchers(HttpMethod.GET, "/price/consumer/**").hasAnyRole(Permission.USER_CONSUMER)
                .antMatchers(HttpMethod.GET, "/price/**").hasAnyRole(Permission.USER_ADMIN)
                
                .antMatchers(HttpMethod.GET, "/batch/**").permitAll()
                
                //authenticated requests
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
    
}
