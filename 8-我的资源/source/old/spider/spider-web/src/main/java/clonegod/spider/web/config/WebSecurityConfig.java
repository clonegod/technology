package clonegod.spider.web.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests() /**用户认证*/
                .antMatchers("/", "/home", "/rest/hello").permitAll() // /和/home不需要认证
                .anyRequest().authenticated()	// 其它任何请求都需要进行认证
                .and()
            .formLogin() /**登陆表单*/
                .loginPage("/login") // 表单登陆页面路径
                .permitAll()	// 允许任何人请求该地址进行登录
                .and()
            .logout() /**注销*/
                .permitAll(); // 允许任何人请求该地址
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("alice").password("123456").roles("USER");
    }
}