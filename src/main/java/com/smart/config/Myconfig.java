package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class Myconfig{   
	
	@Bean
	public UserDetailsService getUserdetailsService() {
		
		
		return  new UserDetailsServiceImpl();
		
} 
	@Bean BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
    @Bean
    public DaoAuthenticationProvider getDaoAuthProvider() {
    	DaoAuthenticationProvider daoAuthenticationprovider=new DaoAuthenticationProvider();
    	daoAuthenticationprovider.setUserDetailsService(getUserdetailsService());
    	daoAuthenticationprovider.setPasswordEncoder(getPasswordEncoder());
      return daoAuthenticationprovider;
    }
	   @Bean
	   public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
    	httpSecurity.csrf().disable()
    	.authorizeHttpRequests()
    	.requestMatchers("/user/admin")
    	.hasRole("ADMIN")
    	.requestMatchers("/user/**").hasRole("USER")
    	.requestMatchers("/**")
    	.permitAll()
    	.anyRequest()
    	.authenticated()
    	.and()
    	.formLogin().loginPage("/signin")
    	.loginProcessingUrl("/dologin")
    	.defaultSuccessUrl("/user/index");
    	httpSecurity.authenticationProvider(getDaoAuthProvider());
		return httpSecurity.build();
     }
     
     public Myconfig() {
	
	}

}
