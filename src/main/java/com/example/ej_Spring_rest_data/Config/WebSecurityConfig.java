package com.example.ej_Spring_rest_data.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/hello").permitAll() //acceder al endpoint /hello sin autenticarnos
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    //Configuramos el firewall estricto
    @Bean
    public HttpFirewall looseHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(false); //cambiar a true si queremos aceptar';'
        firewall.setAllowBackSlash(false); //cambiar a true si queremos aceptar'\'
        //Para permitir métodos POST PUT Y DELETE además del GET
//        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        return firewall;
    }


    //Configuración de la autenticación usuarios, contraseñas y roles
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())//En esta línea indicamos que codifique el password
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER")//Hay que incluir manualmente lo que hay dentro de .password
                .and()
                .withUser("admin").password(passwordEncoder().encode("password")).roles("USER", "ADMIN");//Hay que incluir manualmente lo que hay dentro de .password

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}
