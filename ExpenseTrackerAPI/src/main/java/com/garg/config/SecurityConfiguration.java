package com.garg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.garg.filter.JwtRequestFilter;
import com.garg.service.CustomUserDetailsService;

/* As by default, after addding Spring Security dependency, all rest endpoints become protected that needs authentication but some 
 * endpoints like login user/creating new user needs no authentication that should be accessible publicly/by anyone. So, customizing 
 * default configuration. 
   
   Spring Security Control Flow - back & forth, client request ->
   1. Authentication Filter(Security Context) -
   		It uses SecurityContext to check whether the user is logged in(authenticated) or not for each request.
   		If user is logged in, don't authenticate existing user again for new requests but if not logged in, then authenticate for the 1st request.
   2. Authentication Manager - uses Authentication Type i.e., inMemory/JDBC/LDAP only for first time
   3. Authentication Provider(UserDetailsService & PasswordEncoder) - (Authenticate User)Verifies username & password to login

	as username & password are hardcoded for inMemory Authentication but customize UserDetailsService to validate username & password
	from db.
	Use Spring Security in Service layer
		A CustomUserDetailsService class that extends UserDetailsService, override UserDetails loadUserByUsername(); 
		with findByEmail() in User repository & autowired in SecurityConfiguration class.
*/

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// Step 4. Register the JWT filter with Spring Security Config file i.e., modify void configure(HttpSecurity http)
	//		To tell Spring Security to validate jwt token for all request 
	@Bean
	public JwtRequestFilter authenticationJwtTokenFilter() {
		return new JwtRequestFilter();
	}
	
	// for customizing public(no auth) & protected(auth) Http Requests rest endpoints
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		
		//	super.configure(http);
		
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/login", "/register").permitAll()
			.anyRequest().authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);	// tell not to use session
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
			
		http.httpBasic();	// for login using Postman/Microservices
		
		//	http.formLogin();	for form based login in webapp
	}

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	// to customize user default login inMemory authentication info(username - user & password generated by spring boot)/multiple user login info 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
	
		//	super.configure(auth);
	
		/* User login details are hardcoded but validating user login details from db using CustomUserDetailsService Bean
		UserDetails user1=User.withUsername("Shivam Garg").password("11111").authorities("ADMIN").build();
		UserDetails user2=User.withUsername("Harsh Garg").password("22222").authorities("USER").build();
		
		InMemoryUserDetailsManager manager=new InMemoryUserDetailsManager(user1, user2);
		
		// AuthenticationManager uses diff authentication types i.e., inMemory/Jdbc/LDAP/custom, etc., without user role & password encoding, spring gives exception
		auth.userDetailsService(manager)
			.passwordEncoder(NoOpPasswordEncoder.getInstance());
			//	.passwordEncoder(createPasswordEncoderBean());
		*/
		
		auth.userDetailsService(userDetailsService);
	}
	
	@Bean
	public PasswordEncoder createPasswordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	
}