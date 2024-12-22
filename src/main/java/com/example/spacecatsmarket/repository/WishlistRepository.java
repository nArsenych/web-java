package com.example.spacecatsmarket.repository;

import com.example.spacecatsmarket.repository.entity.WishlistEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntryEntity, Long> {
}
