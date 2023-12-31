package com.Leonardo168.api.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRecordDto(
		@NotBlank @Size(min = 17, max = 17) String isbn,
		@NotBlank @Size(max = 70) String title,
		@NotBlank @Size(max = 70) String author,
		@NotBlank @Size(max = 70) String category,
		@NotNull BigDecimal value,
		@NotBlank @Size(max = 255) String description) {
	
	public ProductRecordDto (String isbn,String title,String author,String category,BigDecimal value,String description) {
		this.isbn = isbn;
		this.title = title.toUpperCase();
		this.author = author.toUpperCase();
		this.category = category;
		this.value = value;
		this.description = description;
	}
}
