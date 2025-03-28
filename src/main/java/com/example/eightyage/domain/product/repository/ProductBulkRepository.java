package com.example.eightyage.domain.product.repository;

import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.SaleState;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
@RequiredArgsConstructor
public class ProductBulkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 50;

    public void bulkInsertProduct(List<Product> products) {
        String sql = "INSERT INTO product (category, name, sale_state) values (?, ?, ?)";

        Random random = new Random();

        jdbcTemplate.batchUpdate(sql, products, BATCH_SIZE, (ps, argument) -> {
            Category randomCategory = Category.values()[random.nextInt(Category.values().length)];
            ps.setString(1, randomCategory.name());
            ps.setString(2, argument.getName());
            ps.setString(3, random.nextBoolean() ? SaleState.FOR_SALE.name() : SaleState.SOLD_OUT.name());
        });
    }
}
