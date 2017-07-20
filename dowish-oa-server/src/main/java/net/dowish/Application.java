package net.dowish;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@SpringBootApplication
@MapperScan("net.dowish.modules.*.dao")
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Bean
	public WebMvcConfigurer CorsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedHeaders("*")
						.allowedMethods("*")
						.allowedOrigins("*");
			}

//			@Override
//			public void addInterceptors(InterceptorRegistry registry) {
//				registry.addInterceptor(new HandlerInterceptorAdapter() {
//
//					@Override
//					public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//											 Object handler) throws Exception {
//
//						if ("OPTIONS".equals(request.getMethod())) {
//							response.setStatus(HttpServletResponse.SC_OK);
//							return true;
//						}
//						final String authHeader = request.getHeader("Authorization");
////                        log.info("authHeader is : {}", authHeader);
//						if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//							log.error("Missing or invalid Authorization header.");
//							return false;
//						}
//
//						final String token = authHeader.substring(7); // The part after "Bearer "
//
//						try {
//							final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY)
//													  .parseClaimsJws(token).getBody();
//							Map<String, ?> userObj = (Map) claims.get("user");
//							User user = new User();
//							user.setId(Long.parseLong(userObj.get("id").toString()));
//							user.setRole(Integer.valueOf(userObj.get("role") == null ? "4" : userObj.get("role").toString()));
//							user.setUsername(userObj.get("username") == null ? "" : userObj.get("username").toString());
//							user.setPassword(userObj.get("password") == null ? "" : userObj.get("password").toString());
//							request.setAttribute("user", user);
//						} catch (final Exception e) {
//							e.printStackTrace();
//							log.error("Invalid Authorization header.");
//							return false;
//						}
//						return true;
//					}
//				}).addPathPatterns("/api/**");
//			}
		};
	}
}
