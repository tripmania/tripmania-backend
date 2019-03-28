package com.chichkanov.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
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
                .antMatchers("/users/refresh").permitAll()
                .anyRequest().authenticated()

        http.exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

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

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }

}