package com.chichkanov.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import java.util.*
import javax.servlet.http.HttpServletRequest


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(0)
class WebSecurityConfig constructor(
        private val jwtTokenProvider: JwtTokenProvider
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeRequests()//
                .antMatchers("/users/sign-in").permitAll()
                .antMatchers("/users/sign-up").permitAll()
                .antMatchers("/h2-console/**/**").permitAll()
                .anyRequest().authenticated()

        http.cors().configurationSource(object : CorsConfigurationSource {
            override fun getCorsConfiguration(request: HttpServletRequest): CorsConfiguration? {
                val config = CorsConfiguration()
                config.allowedHeaders = Collections.singletonList("*")
                config.allowedMethods = Collections.singletonList("*")
                config.addAllowedOrigin("*")
                config.allowCredentials = true
                return config
            }
        })

        http.exceptionHandling().accessDeniedPage("/login")
        http.apply<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>>(JwtTokenFilterConfigurer(jwtTokenProvider))
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public")

                // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }

}