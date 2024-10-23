package com.openWeather.demo.config;

import com.openWeather.demo.listener.WeatherDataReaderListener;
import com.openWeather.demo.listener.WeatherDataStepListener;
import com.openWeather.demo.listener.WeatherForecastWriterListener;
import com.openWeather.demo.model.WeatherForecast;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.openWeather.demo.model.WeatherData;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final WeatherDataStepListener weatherDataStepListener;
    private final WeatherDataReaderListener weatherDataReaderListener;
    private final WeatherForecastWriterListener weatherForecastWriterListener;
    public BatchConfig(WeatherDataStepListener weatherDataStepListener, WeatherDataReaderListener weatherDataReaderListener, WeatherForecastWriterListener weatherForecastWriterListener) {
        this.weatherDataStepListener = weatherDataStepListener;
        this.weatherDataReaderListener = weatherDataReaderListener;
        this.weatherForecastWriterListener = weatherForecastWriterListener;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource postgresDataSource) {
        return new DataSourceTransactionManager(postgresDataSource);
    }

    @Bean
    public PlatformTransactionManager mysqlTransactionManagerBatchConfig(@Qualifier("mysqlDataSource") DataSource mysqlDataSource) {
        return new DataSourceTransactionManager(mysqlDataSource);
    }


    @Bean
    public JobRepository jobRepository(@Qualifier("dataSource") DataSource postgresDataSource,
                                       @Qualifier("transactionManager") PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(postgresDataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Job weatherDataJob(JobRepository jobRepository, Step processWeatherDataStep) {
        return new JobBuilder("weatherDataJob", jobRepository)
                .start(processWeatherDataStep)
                .build();
    }

    @Bean
    public Step weatherDataStep(JobRepository jobRepository,
                                @Qualifier("transactionManager") PlatformTransactionManager postgresTransactionManager,
                                @Qualifier("mysqlTransactionManager") PlatformTransactionManager mysqlTransactionManager) {
        return new StepBuilder("weatherDataStep", jobRepository)
                .<WeatherData, WeatherForecast>chunk(10, postgresTransactionManager)
                .reader(reader(null, null))
                .listener(Optional.ofNullable(weatherDataReaderListener))
                .processor(processor())
                .writer(writer(null))
                .transactionManager(mysqlTransactionManager)
                .listener(weatherForecastWriterListener)
                .listener(weatherDataStepListener)
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<WeatherData> reader(
            @Qualifier("postgresEntityManager") EntityManagerFactory entityManagerFactory,
            @Value("#{stepExecutionContext['lastProcessedTimestamp']}") LocalDateTime lastProcessedTimestamp) {

        JpaPagingItemReader<WeatherData> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT w FROM WeatherData w WHERE w.timestamp > :lastProcessedTimestamp");
        reader.setParameterValues(Map.of("lastProcessedTimestamp", lastProcessedTimestamp));
        reader.setPageSize(5); // Leggi 5 record per volta
        reader.setName("weatherDataReader");

        return reader;
    }


    @Bean
    @StepScope
    public ItemProcessor<WeatherData, WeatherForecast> processor() {
        return weatherData -> {
            WeatherForecast forecast = new WeatherForecast();
            forecast.setForecastDay(weatherData.getTimestamp().toLocalDate());
            forecast.setTemperature(BigDecimal.valueOf(weatherData.getTemperature()));
            forecast.setHumidity(BigDecimal.valueOf(weatherData.getHumidity()));
            forecast.setDescription("Previsioni basate sui dati storici");
            forecast.setTimestamp(LocalDateTime.now());
            return forecast;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<WeatherForecast> writer(@Qualifier("mysqlEntityManager") EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<WeatherForecast> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
