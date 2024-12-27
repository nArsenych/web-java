package com.example.spacecatsmarket.repository;

import com.example.spacecatsmarket.repository.entity.OrderEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderEntryRepository extends JpaRepository<OrderEntryEntity, UUID> {
}
