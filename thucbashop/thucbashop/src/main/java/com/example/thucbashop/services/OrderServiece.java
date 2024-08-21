package com.example.thucbashop.services;

import com.example.thucbashop.dtos.CartItemDTO;
import com.example.thucbashop.dtos.OrderDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.models.*;
import com.example.thucbashop.repositories.OrderDetailRepo;
import com.example.thucbashop.repositories.OrderRepo;
import com.example.thucbashop.repositories.ProductRepo;
import com.example.thucbashop.repositories.UserRepo;
import com.example.thucbashop.responese.OrderResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiece implements IOrderServiece {
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final ProductRepo productRepo;
    private final  OrderDetailRepo orderDetailRepo;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        //tìm xem userID có tồn tại không
        User user = userRepo.findById(orderDTO.getUserId()).
                orElseThrow(() -> new DataNotFound("khong thay user vói id" + orderDTO.getUserId()));

        // chuyển đổi orderDto => order
        //tạo ánh xạ riêng để kiểm soát việc ánh xạ
        // Check if a TypeMap already exists
        TypeMap<OrderDTO, Order> typeMap = modelMapper.getTypeMap(OrderDTO.class, Order.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(OrderDTO.class, Order.class);
            typeMap.addMappings(mapper -> mapper.skip(Order::setId));
        }

        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        //kiểm tra > ngày hnay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFound("Khong Duoc BE Hon Ngay Hom Nay");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);
        order.setTotalMoney(order.getTotalMoney());
        orderRepo.save(order);
        //tao danh sach các đối tượng Order detailtừ cart Item
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItemDTO cartItemDTO : orderDTO.getCartItem()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            // Lay Thong Tin San Pham tu CartItem DTO
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantify();
            // tim thong tin san pham tu csdl hoặc sử dụng cache
            Product product = productRepo.findById(productId)
                    .orElseThrow(()-> new DataNotFound("Khong Tim Thay Id : "+productId));

            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            // cac trường khác orderdetail nếu cần
            orderDetail.setPrice(product.getPrice());
            //thêm orderdetail vào danh sách
            orderDetails.add(orderDetail);
        }

        //luu danh sach orderDetail vao csdl
        orderDetailRepo.saveAll(orderDetails);

        return order;
    }

    @Override
    public Order getOrderById(long id) {
        return orderRepo.findById(id).orElse(null);
    }

    @Override
    public List<Order> findByUserId(long userId) {
        return orderRepo.findByUserId(userId);
    }


    @Override
    @Transactional
    public Order updateOrder(long id, OrderDTO orderDTO)
            throws DataNotFound {
        Order order = orderRepo.findById(id).orElseThrow(() -> new DataNotFound("Khong tim thay id " + id));
        User existingUser = userRepo.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFound("Khong tim thay id " + id));
        //tạo ánh xạ kiểm tra ánh xạ
        TypeMap<OrderDTO, Order> typeMap = modelMapper.createTypeMap(OrderDTO.class, Order.class);
        typeMap.addMappings(mapper -> mapper.skip(Order::setId));

        //cập nhật các luồng đơn hàng từ orderDTO
        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        return orderRepo.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(long id) {
       Order order = orderRepo.findById(id).orElse(null);
        //koxoasas cứng -- xóa mềm
        if(order != null){
           order.setActive(false);
           orderRepo.save(order);
        }

    }
}
