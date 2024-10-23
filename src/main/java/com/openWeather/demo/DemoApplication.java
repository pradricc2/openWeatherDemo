package com.openWeather.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Component
	public class JobRunner implements CommandLineRunner {

		private final JobLauncher jobLauncher;
		private final Job weatherDataJob;
		public JobRunner(JobLauncher jobLauncher, Job weatherDataJob) {
			this.jobLauncher = jobLauncher;
			this.weatherDataJob = weatherDataJob;
		}

		@Override
		public void run(String... args) throws Exception {
			JobParameters params = new JobParametersBuilder()
					.addString("JobID", String.valueOf(System.currentTimeMillis()))
					.toJobParameters();
			jobLauncher.run(weatherDataJob, params);
		}
	}


}
