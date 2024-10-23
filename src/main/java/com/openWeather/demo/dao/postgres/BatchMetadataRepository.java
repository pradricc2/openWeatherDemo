package com.openWeather.demo.dao.postgres;

import com.openWeather.demo.model.BatchMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchMetadataRepository extends JpaRepository<BatchMetadata, Long> {
}
