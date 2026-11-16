package com.dymun.pricealert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling // @Scheduled 메서드를 찾아서 실행해주는 스케줄링 엔진을 켬 (이게 없으면 @Scheduled는 무시됨)
public class PricealertApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricealertApplication.class, args);
	}

}
