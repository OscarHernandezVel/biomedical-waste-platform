package com.biomedical.waste.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "ai_interactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String type; // "chat" or "analyzer"

    @Lob
    @Column(columnDefinition = "TEXT")
    private String userInput;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aiResponse;

    @Column
    private String analysisType; // for analyzer: "waste", "route", etc.

    @Column
    private String imageName; // for analyzer: original filename

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
