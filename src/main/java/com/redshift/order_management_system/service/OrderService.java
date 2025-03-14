package com.redshift.order_management_system.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redshift.order_management_system.dto.NotificationDto;
import com.redshift.order_management_system.dto.OrderRequest;
import com.redshift.order_management_system.model.Order;
import com.redshift.order_management_system.repository.OrderRepository;
import lombok.Builder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String ORDER_QUEUE = "notification.queue";

    @Transactional
    public Order placeOrder(OrderRequest request) {
        // Create and save order
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setStatus("PENDING");

        Order savedOrder = orderRepository.save(order);
        NotificationDto notificationDto=new NotificationDto(){{
            setMessage("ORDER_PLACED");
            setRecipient(request.getUserId().toString());
        }};
    //    rabbitTemplate.convertAndSend("notification.queue", notificationDto);
        try {
            String json = objectMapper.writeValueAsString(notificationDto);
            rabbitTemplate.convertAndSend("notification.queue", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return savedOrder;
    }
}
