package com.openWeather.demo.listener;

import com.openWeather.demo.model.WeatherForecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Component
public class WeatherForecastWriterListener implements ItemWriteListener<WeatherForecast> {

    private int count = 0;
    private Logger logger = LoggerFactory.getLogger(WeatherForecastWriterListener.class);

    @Override
    public void beforeWrite(Chunk<? extends WeatherForecast> items) {
        logger.debug("About to write: {}", items);
    }

    @Override
    public void afterWrite(Chunk<? extends WeatherForecast> items) {
        logger.debug("Successfully wrote: {} ", items);
        count += items.size();
        if (count >= 10) {
            // Logica per aggiornare il database MySQL ogni 10 inserimenti
            logger.debug("Aggiornamento della tabella MySQL dopo 10 inserimenti nella tabella Postgres");
            count = 0;
        }
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends WeatherForecast> items) {
        logger.error("Error writing items: {} ", items);
        exception.printStackTrace();
    }
}
