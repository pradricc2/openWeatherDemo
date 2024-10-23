package com.openWeather.demo.listener;

import com.openWeather.demo.model.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.time.LocalDateTime;

import static com.openWeather.demo.constants.Constants.LAST_PROCESSED_TIMESTAMP_KEY;

@Component
public class WeatherDataReaderListener implements ItemReadListener<WeatherData>, StepExecutionListener {

    private Logger logger = LoggerFactory.getLogger(WeatherDataReaderListener.class);
    private StepExecution stepExecution;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Salva il riferimento al contesto di esecuzione
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Nessuna azione necessaria in questo caso
        return ExitStatus.COMPLETED;
    }

    @Override
    public void beforeRead() {
        logger.debug("Inizio lettura di un nuovo elemento.");
        // Accedi al contesto di esecuzione per ottenere informazioni aggiuntive
        if (stepExecution != null) {
            ExecutionContext stepContext = stepExecution.getExecutionContext();
            LocalDateTime lastProcessedTimestamp = stepContext.get(LAST_PROCESSED_TIMESTAMP_KEY, LocalDateTime.class);
            logger.debug("Last Processed Timestamp: {}", lastProcessedTimestamp);
        }
    }

    @Override
    public void afterRead(WeatherData item) {
        if (item != null) {
            logger.info("Elemento letto: ID = {}, timestamp = {}", item.getId(), item.getTimestamp());
        } else {
            logger.warn("Elemento letto risulta nullo.");
        }
    }

    @Override
    public void onReadError(Exception ex) {
        logger.error("Errore durante la lettura dell'elemento: {}", ex.getMessage(), ex);
    }
}
