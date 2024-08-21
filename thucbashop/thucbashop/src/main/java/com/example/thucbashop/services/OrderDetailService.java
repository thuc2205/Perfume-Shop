package com.example.thucbashop.services;

import com.example.thucbashop.dtos.OrderDetailDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.models.Order;
import com.example.thucbashop.models.OrderDetail;
import com.example.thucbashop.models.Product;
import com.example.thucbashop.repositories.OrderDetailRepo;
import com.example.thucbashop.repositories.OrderRepo;
import com.example.thucbashop.repositories.ProductRepo;
import com.example.thucbashop.responese.OrderResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailSerVice{
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final ProductRepo productRepo;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        //tim xem orderId cos ton tai hay khong
        Order order =orderRepo.findById(orderDetailDTO.getOrderId()).
                orElseThrow(()-> new DataNotFound("Cannot find order by id"));
        Product product =productRepo.findById(orderDetailDTO.getProductId()).
                orElseThrow(()-> new DataNotFound("Cannot find Product by id"));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProduct())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return   orderDetailRepo.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetailById(long id) throws DataNotFound {
        return orderDetailRepo.findById(id).
                orElseThrow(()->new DataNotFound("Khong Tim Thay id orderdetail"));
    }

    @Override
    public List<OrderDetail> findByOrderId(long orderId) {
        return orderDetailRepo.findByOrderId(orderId);
    }


    @Override
    public OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) {
        return null;
    }

    @Override
    @Transactional
    public void deleteOrderDetails(long id) {
        orderDetailRepo.deleteById(id);

    }
}
