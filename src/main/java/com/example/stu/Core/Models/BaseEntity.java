package com.example.stu.Core.Models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * Base entity class that combines TimeMixin and SoftDeleteMixin functionality
 * This class can be extended by all entities that need both timestamp tracking and soft delete
 */
@Data
public abstract class BaseEntity implements ISoftDeleteAware {
    
    // TimeMixin fields
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
    
    // SoftDeleteMixin fields
    @Column("is_deleted")
    private Boolean deleted = false;
    
    /**
     * Check if the entity is deleted
     * @return true if the entity is deleted, false otherwise
     */
    @Override
    public boolean isDeleted() {
        return deleted != null && deleted;
    }
    
    /**
     * Mark the entity as deleted
     */
    @Override
    public void markAsDeleted() {
        this.deleted = true;
    }
    
    /**
     * Restore the entity (mark as not deleted)
     */
    @Override
    public void restore() {
        this.deleted = false;
    }
}