package com.Leonardo168.api.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Leonardo168.api.models.TransactionModel;
import com.Leonardo168.api.repositories.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;

	public Object save(TransactionModel transactionModel) {
		return transactionRepository.save(transactionModel);
	}

	public Optional<Page<Object>> findByBuyerId(UUID userId, Pageable pageable) {
		return transactionRepository.findByBuyerId(userId, pageable);
	}

	public Optional<Page<Object>> findByBuyerIdOrVendorId(UUID buyerId, UUID vendorId, Pageable pageable) {
		return transactionRepository.findByBuyerIdOrVendorId(buyerId, vendorId, pageable);
	}

	public boolean existsByBuyerIdOrVendorId(UUID buyerId, UUID vendorId) {
		return transactionRepository.existsByBuyerIdOrVendorId(buyerId, vendorId);
	}

	public boolean existsByProductId(UUID id) {
		return transactionRepository.existsByProductId(id);
	}

	public Optional<TransactionModel> findById(UUID id) {
		return transactionRepository.findById(id);
	}

	public void delete(TransactionModel transactionModel) {
		transactionRepository.delete(transactionModel);
	}

}
