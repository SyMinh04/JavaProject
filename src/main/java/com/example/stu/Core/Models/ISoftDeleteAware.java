package com.example.stu.Core.Models;

/**
 * Interface for entities that support soft delete functionality
 * This allows checking if an entity supports soft delete without requiring inheritance
 */
public interface ISoftDeleteAware {
    
    /**
     * Check if the entity is deleted
     * @return true if the entity is deleted, false otherwise
     */
    boolean isDeleted();
    
    /**
     * Mark the entity as deleted
     */
    void markAsDeleted();
    
    /**
     * Restore the entity (mark as not deleted)
     */
    void restore();
}