package com.openWeather.demo.listener;

import com.openWeather.demo.dao.postgres.BatchMetadataRepository;
import com.openWeather.demo.model.BatchMetadata;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.openWeather.demo.constants.Constants.LAST_PROCESSED_TIMESTAMP_KEY;

@Component
public class WeatherDataStepListener implements StepExecutionListener {


    private final BatchMetadataRepository batchMetadataRepository;
    public WeatherDataStepListener(BatchMetadataRepository batchMetadataRepository) {
        this.batchMetadataRepository = batchMetadataRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Recupera l'ultimo timestamp processato dalla tabella BatchMetadata
        BatchMetadata batchMetadata = batchMetadataRepository.findById(1L).orElse(new BatchMetadata(1L, LocalDateTime.of(1970, 1, 1, 0, 0)));
        LocalDateTime lastProcessedTimestamp = batchMetadata.getLastProcessedTimestamp();
        stepExecution.getExecutionContext().put(LAST_PROCESSED_TIMESTAMP_KEY, lastProcessedTimestamp);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Salva l'ultimo timestamp processato dopo l'esecuzione dello step
        LocalDateTime lastProcessedTimestamp = LocalDateTime.now();
        batchMetadataRepository.save(new BatchMetadata(1L, lastProcessedTimestamp));
        return ExitStatus.COMPLETED;
    }
}


