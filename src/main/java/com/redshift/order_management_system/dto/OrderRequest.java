package com.redshift.order_management_system.dto;


import lombok.Data;

@Data
public class OrderRequest {
    private Long userId;
    private String productId;
    private int quantity;
    private double price;
}
