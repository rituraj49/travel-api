package com.jamuara.crs.shceduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile("ec2")
@EnableScheduling
public class CronSchedulingConfig {
}
