package cn.hub.jackeroo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.hub.jackeroo.*.dao")
public class JackerooBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JackerooBootApplication.class, args);
	}

	/*@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}*/
}
