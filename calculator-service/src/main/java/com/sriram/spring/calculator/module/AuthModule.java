package com.sriram.spring.calculator.module;

import com.sriram.spring.calculator.service.DummyUserDetailService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author guduri.sriram
 */
@Slf4j
//@Configuration
//@EnableWebSecurity
public class AuthModule
        //extends WebSecurityConfigurerAdapter
                                                {

//    @Value("${security.keytab.file}")
//    private String keytabFilePath;
//
//    @Value("${security.service.principal}")
//    private String servicePrincipal;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//          .exceptionHandling()
//          .authenticationEntryPoint(spnegoEntryPoint())
//          .and()
//          .authorizeRequests()
//          .anyRequest().authenticated()
//          .and()
//          .formLogin()
//          .and()
//          .logout()
//          .permitAll()
//          .and()
//          .addFilterBefore(spnegoAuthenticationProcessingFilter(), BasicAuthenticationFilter.class);
//    }
//
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//           .authenticationProvider(kerberosAuthenticationProvider())
//           .authenticationProvider(kerberosServiceAuthenticationProvider());
//    }
//
//    @Bean
//    public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
//        KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
//        SunJaasKerberosClient client = new SunJaasKerberosClient();
//        client.setDebug(true);
//        provider.setKerberosClient(client);
//        provider.setUserDetailsService(dummyUserDetailsService());
//        return provider;
//    }
//
//    @Bean
//    public SpnegoEntryPoint spnegoEntryPoint() {
//        return new SpnegoEntryPoint("/");
//    }
//
//    @Bean
//    public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter() {
//        SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
//        try {
//            filter.setAuthenticationManager(authenticationManagerBean());
//        } catch (Exception e) {
//            log.error("Failed to set AuthenticationManager on SpnegoAuthenticationProcessingFilter.", e);
//        }
//        return filter;
//    }
//
//    @Bean
//    public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {
//        KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
//        provider.setTicketValidator(sunJaasKerberosTicketValidator());
//        provider.setUserDetailsService(dummyUserDetailsService());
//        return provider;
//    }
//
//    @Bean
//    public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {
//        SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
//        ticketValidator.setServicePrincipal(servicePrincipal); //At this point, it must be according to what we were given in the
//        // commands from the first step.
//        FileSystemResource fs = new FileSystemResource(keytabFilePath); //Path to file tomcat.keytab
//        log.info("Initializing Kerberos KEYTAB file path:" + fs.getFilename() + " for principal: " + servicePrincipal + "file exist: " + fs.exists());
//        Assert.notNull(fs.exists(), "*.keytab key must exist. Without that security is useless.");
//        ticketValidator.setKeyTabLocation(fs);
//        ticketValidator.setDebug(true); //Turn off when it will works properly,
//        return ticketValidator;
//    }
//
//    @Bean
//    public DummyUserDetailService dummyUserDetailsService() {
//        return new DummyUserDetailService();
//    }
}