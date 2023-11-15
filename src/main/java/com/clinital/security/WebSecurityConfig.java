package com.clinital.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.clinital.security.jwt.AuthEntryPointJwt;
import com.clinital.security.jwt.AuthTokenFilter;
import com.clinital.security.oauth2.CustomOAuth2UserService;
import com.clinital.security.oauth2.HttpCookieOAuth2AuthrizationRequestRepository;
import com.clinital.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.clinital.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.clinital.security.services.UserDetailsServiceImpl;
import com.clinital.util.PDFGenerator;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private CustomOAuth2UserService cUserOauth2Service;

	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Autowired
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	
	@Autowired
	private HttpCookieOAuth2AuthrizationRequestRepository htCookiesauthRepository;

	


	private static final String[] AUTH_WHITELIST = {
			// -- Swagger UI v2
			"/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui.html", "/webjars/**",
			// -- Swagger UI v3 (OpenAPI)
			"/v3/api-docs/**", "/swagger-ui/**"
			,"/api/demandes/create",
			"/api/med/getAllSpec",
			"/api/med/medById/**",
			"/api/med/medByName",
			"/api/med/medByNameOrSpecAndVille",
			"/api/med/medByNameAndSpec",
			"/api/med/medByNameOrSpec",
			"/api/med/medByVille",
			"/api/auth/signup",
			"/api/med/agenda/**",
			"/api/med/medecins",
			"/api/med/getTypeConsultationById/**",
			"/api/typeConsultation/getAllByMedecinId/**",
			"/api/auth/checkToken/**",
			"/api/cabinet/**",
			"/api/auth/oauth2/**",
			"/api/conference/**",
			// other public endpoints of your API may be appended to this array
	};



	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
  	public PDFGenerator pdfGenerator() {
    // return a new instance of PDFGenerator
    return new PDFGenerator();
  }

	@Bean
	@Primary
	public HttpCookieOAuth2AuthrizationRequestRepository cookiesAuthoritizationRepository(){
		return new HttpCookieOAuth2AuthrizationRequestRepository();
	}

	/*~~(Migrate manually based on https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)~~>*/@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	/*~~(Migrate manually based on https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)~~>*/@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.addAllowedHeader("Content-Type");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
        http.headers(headers -> headers.frameOptions().disable());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http
                .authorizeRequests(requests -> requests
                        .antMatchers("/api/auth/**", "/oauth2/**").permitAll()
                        .antMatchers("/api/ville/**", "/api/typeConsultation/getall",
                                "/api/typeConsultation/getTypeConsultationById/**").permitAll()
                        .antMatchers(AUTH_WHITELIST).permitAll()
                        .antMatchers("/api/users/**").hasAnyRole("PATIENT", "ADMIN", "SECRETAIRE", "MEDECIN")
						.antMatchers("/api/shares/**").hasAnyRole("PATIENT", "ADMIN", "SECRETAIRE", "MEDECIN")
                        .antMatchers("/api/patient/**").hasAnyRole("PATIENT", "ADMIN")
                        .antMatchers("/api/med/**").hasAnyRole("MEDECIN", "ADMIN")
                        .antMatchers("/api/med/antecedent/**").hasAnyRole("MEDECIN", "ADMIN")
                        .antMatchers("/api/sec/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .antMatchers("/api/users/activity/myactivity").hasAnyRole("PATIENT", "MEDECIN")
                        .antMatchers("/api/users/activity/**").hasRole("ADMIN")
                        .antMatchers("/api/users/block/**").hasRole("ADMIN")
                        .antMatchers("/api/med/setvisibilityMed/**").hasRole("ADMIN")
                        .antMatchers("/api/med/setcabinetvalidation/**").hasRole("ADMIN")
                        .antMatchers("/api/medecinSchedule/**").hasRole("MEDECIN")
                        .antMatchers("/api/rdv").hasAnyRole("MEDECIN", "PATIENT")
                        .antMatchers("/api/typeConsultation/**").hasRole("MEDECIN")
                        .antMatchers("/api/users/respw").hasAnyRole("PATIENT", "ADMIN", "SECRETAIRE", "MEDECIN")
                        .antMatchers("/api/demandes/**", "/api/cabinet/validation/").hasRole("ADMIN")
						.antMatchers("/api/cabinet/allcabbymedid/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler(accessDeniedHandler()))
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(unauthorizedHandler))
                //Add login with Google and Facebook.
                .oauth2Login(login -> login
                        .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(cookiesAuthoritizationRepository())
                        .and()
                        .redirectionEndpoint()
                        .baseUri("/oauth2/callback/**")
                        .and()
                        .userInfoEndpoint()
                        .userService(cUserOauth2Service)
                        .and()
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler));
        http.sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

			
				;

//			antMatchers("/api/pers/**").permitAll().antMatchers("/api/doc/**").permitAll()
//			.antMatchers("/api/rdv/**").permitAll();

		
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustumaccessdeniedHandler();
	}

	/* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
