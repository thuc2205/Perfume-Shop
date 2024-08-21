package com.example.thucbashop.services;

import com.example.thucbashop.dtos.CategoryDTO;
import com.example.thucbashop.dtos.OrderDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.models.Category;
import com.example.thucbashop.models.Order;
import com.example.thucbashop.responese.OrderResponse;

import java.util.List;

public interface IOrderServiece {

    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrderById(long id);
    List<Order> findByUserId(long userId);
    Order updateOrder(long id,OrderDTO orderDTO) throws DataNotFound;
    void deleteOrder(long id);
}
