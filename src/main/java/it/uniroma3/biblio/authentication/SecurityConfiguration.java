package it.uniroma3.biblio.authentication;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	private final DataSource dataSource;
	private final CustomOAuth2UserService customOAuth2UserService;

	public SecurityConfiguration(DataSource dataSource, CustomOAuth2UserService customOAuth2UserService) {
	    this.dataSource = dataSource;
	    this.customOAuth2UserService = customOAuth2UserService;
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
		
		manager.setUsersByUsernameQuery(
				"SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
		manager.setAuthoritiesByUsernameQuery(
				"SELECT username, ruolo FROM credentials WHERE username=?");
		
		return manager;
	}
	
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.authorizeHttpRequests(authorize -> {
			//rotte pubbliche
			authorize.requestMatchers(HttpMethod.GET, "/css/**", "/images/**", "/error/**", "/favicon.ico").permitAll();
			authorize.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/libri", "/libro/{id}",
					"/autori", "/autore/{id}", "/generi", "/genere/{id}", "/ricerca").permitAll();
			authorize.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll();
			//rotte admin
			authorize.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority("ADMIN");
			authorize.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority("ADMIN");
			
			//API rest
			authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
			
			//tutte le altre rotte
			authorize.anyRequest().authenticated();
		});
		
		httpSecurity.cors(Customizer.withDefaults());
		httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
		
		httpSecurity.formLogin(form -> {
			form.loginPage("/login").permitAll();
			form.defaultSuccessUrl("/", true);
			form.failureUrl("/login?error=true");
		});
		
		httpSecurity.oauth2Login(oauth2 -> {
		    oauth2.loginPage("/login");
		    oauth2.userInfoEndpoint(userInfo -> userInfo.oidcUserService(this.customOAuth2UserService));
		    oauth2.defaultSuccessUrl("/", true);
		});
		
		httpSecurity.logout(logout -> {
			logout.logoutUrl("/logout").permitAll();
			logout.logoutSuccessUrl("/");
			logout.invalidateHttpSession(true);
			logout.deleteCookies("JSESSIONID");
			logout.clearAuthentication(true);
			logout.permitAll();
		});
		
		return httpSecurity.build();
	}
}
