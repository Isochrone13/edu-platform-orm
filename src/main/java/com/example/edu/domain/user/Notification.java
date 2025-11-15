package com.example.edu.domain.user;

import com.example.edu.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String message;

    private boolean read = false;

    @Transient
    @JsonProperty(value = "createdAtNote", access = JsonProperty.Access.READ_ONLY)
    public java.time.Instant getCreatedAtNote() {
        return this.createdAt;
    }
}
