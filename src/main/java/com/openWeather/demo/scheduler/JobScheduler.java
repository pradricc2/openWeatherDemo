package com.openWeather.demo.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class JobScheduler {

    private Logger logger = LoggerFactory.getLogger(JobScheduler.class);
    private final JobLauncher jobLauncher;
    private final Job weatherDataJob;
    public JobScheduler(JobLauncher jobLauncher, Job weatherDataJob) {
        this.jobLauncher = jobLauncher;
        this.weatherDataJob = weatherDataJob;
    }

    @Scheduled(fixedRate = 60000) // Esegui ogni 60 secondi
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // Nuovo job parameter per ogni esecuzione
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(weatherDataJob, jobParameters);
            logger.debug("Job Execution Status: {}", execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

