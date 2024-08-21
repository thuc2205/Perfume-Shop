package com.example.thucbashop.controllers;

import com.example.thucbashop.components.LocalizationUtils;
import com.example.thucbashop.dtos.OrderDetailDTO;
import com.example.thucbashop.models.OrderDetail;
import com.example.thucbashop.services.IOrderDetailSerVice;
import com.example.thucbashop.services.IOrderServiece;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orderdetails")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailSerVice orderDetailSerVice;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<?> createOrderdetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
        try{

            return ResponseEntity.ok(orderDetailSerVice.createOrderDetail(orderDetailDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOdersDetail(@Valid @PathVariable("id") Long id){
        try{
           OrderDetail orderDetail=  orderDetailSerVice.getOrderDetailById(id);
            return ResponseEntity.ok(orderDetail);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    lấy order_detail từ 1 1 order
    @GetMapping("order/{orderId}")
    public ResponseEntity<?> getOdersDetails(@Valid @PathVariable("orderId") Long orderId){
       List<OrderDetail>orderDetails= orderDetailSerVice.findByOrderId(orderId);
        return ResponseEntity.ok(orderDetails);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id")Long id ,
                                            @RequestBody OrderDetailDTO neworderDetailDTO){
        return ResponseEntity.ok("update Orderdetail by id :  "+id + "new OrderDetailData: " +neworderDetailDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@Valid @PathVariable("id")Long id){
        return ResponseEntity.noContent().build();
    }

























}
