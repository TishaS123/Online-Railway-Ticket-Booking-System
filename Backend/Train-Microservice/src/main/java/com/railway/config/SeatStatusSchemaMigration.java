package com.railway.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SeatStatusSchemaMigration {

	@Bean
	ApplicationRunner migrateSeatStatusColumn(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				jdbcTemplate.execute("ALTER TABLE seat MODIFY COLUMN status VARCHAR(20) NOT NULL");
			} catch (Exception ignored) {
				// Column may already be compatible in some environments.
			}
		};
	}
}