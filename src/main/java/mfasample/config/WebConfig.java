package mfasample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mfasample.controller.interceptor.AuthCheckHandlerInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private AuthCheckHandlerInterceptor authCheckHandlerInterceptor;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 画面側をVueで開発するためのCORS設定
		registry.addMapping("/auth/**").allowCredentials(true).allowedMethods("GET", "POST", "OPTIONS")
				.allowedOrigins("http://localhost:8081");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 認証済みかどうかを確認するためのInterceptorの設定
		registry.addInterceptor(authCheckHandlerInterceptor).addPathPatterns("/auth/mfa/**");
	}
}
