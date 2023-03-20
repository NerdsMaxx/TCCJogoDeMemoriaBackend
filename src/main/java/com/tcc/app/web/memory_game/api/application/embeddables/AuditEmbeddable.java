package com.tcc.app.web.memory_game.api.application.embeddables;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Embeddable
public class AuditEmbeddable {
    @Column(nullable = false)
    private LocalDateTime createdOn;
    
    @Column(nullable = false)
    private Long createdBy;
    
    @Column(nullable = false)
    private LocalDateTime updatedOn;
    
    @Column(nullable = false)
    private Long updateBy;
    
    public void setCreatedBy(UserEntity user) {
        this.createdBy = user.getId();
    }
    
    public void setUpdateBy(UserEntity user) {
        this.updateBy = user.getId();
    }
    
    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
    }
}