package com.example.thucbashop.controllers;

import com.example.thucbashop.components.LocalizationUtils;
import com.example.thucbashop.components.MessageKeys;
import com.example.thucbashop.dtos.OrderDTO;
import com.example.thucbashop.models.Order;
import com.example.thucbashop.services.IOrderServiece;
import com.example.thucbashop.services.OrderServiece;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiece orderServiece;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<?> createOder(@Valid @RequestBody OrderDTO oderDTO, BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessage =  result.getFieldErrors().
                        stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order orderResponse = orderServiece.createOrder(oderDTO);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOders(@Valid @PathVariable("user_id") Long userid){
        try{
            List<Order> orders = orderServiece.findByUserId(userid);
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOder(@Valid @PathVariable("id") Long orderId){
        try{
            Order existingOrder = orderServiece.getOrderById(orderId);
            return ResponseEntity.ok(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //admmin
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id,
                                        @Valid @RequestBody OrderDTO orderDTO){

        try{
            Order order = orderServiece.updateOrder(id,orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id){
        //xóa mềm => cập nhật active = false
        orderServiece.deleteOrder(id);
        return ResponseEntity.ok(localizationUtils.getLocalizationUtils(MessageKeys.ORDER_DELETE_SUCCESSFULLY));

    }























}
