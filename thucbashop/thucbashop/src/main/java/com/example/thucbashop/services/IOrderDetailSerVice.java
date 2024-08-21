package com.example.thucbashop.services;

import com.example.thucbashop.dtos.OrderDTO;
import com.example.thucbashop.dtos.OrderDetailDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.models.Order;
import com.example.thucbashop.models.OrderDetail;
import com.example.thucbashop.models.ProductImage;

import java.util.List;

public interface IOrderDetailSerVice {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetail getOrderDetailById(long id) throws DataNotFound;
    List<OrderDetail> findByOrderId(long orderId);
    OrderDetail updateOrderDetail(long id,OrderDetailDTO orderDetailDTO);
    void deleteOrderDetails(long id);
}
