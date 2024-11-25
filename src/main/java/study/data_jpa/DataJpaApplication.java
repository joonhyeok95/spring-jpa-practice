package study.data_jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}
	
	@Bean
	public AuditorAware<String> auditorProvider(){
	    // 여기서 실제 아이디를 꺼내는 작업
	    return () -> Optional.of(UUID.randomUUID().toString()); // interface에서 메서드가 1개면 람다로 바꿀수 있다.
	}
}
