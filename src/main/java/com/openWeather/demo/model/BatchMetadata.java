package com.openWeather.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_metadata")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "batch_seq")
    @SequenceGenerator(name = "batch_seq", sequenceName = "batch_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "last_processed_timestamp")
    private LocalDateTime lastProcessedTimestamp;


}
