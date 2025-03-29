package com.example.eightyage.domain.product.repository;

import com.example.eightyage.domain.product.category.Category;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.salestate.SaleState;
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

        jdbcTemplate.batchUpdate(sql, products, BATCH_SIZE, (ps, argument) -> {
            ps.setString(1, argument.getCategory().name());
            ps.setString(2, argument.getName());
            ps.setString(3, argument.getSaleState().name());
        });
    }
}
