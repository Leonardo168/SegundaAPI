package com.Leonardo168.api.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_PRODUCT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID productId;
	@Column(nullable = false, length = 17)
	private String isbn;
	@Column(nullable = false, length = 70)
	private String title;
	@Column(nullable = false, length = 70)
	private String author;
	@ManyToOne
	@JoinTable(name = "TB_PRODUCTS_CATEGORIES",
			joinColumns = @JoinColumn(name = "product_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id"))
	private CategoryModel category;
	@Column(nullable = false)
	private BigDecimal value;
	@Column(nullable = false)
	private String description;
	@Column(nullable = false)
	private boolean available;
	@Column(nullable = false)
	private LocalDateTime editDate;
	@Column(nullable = false)
	private UUID vendorId;


}
