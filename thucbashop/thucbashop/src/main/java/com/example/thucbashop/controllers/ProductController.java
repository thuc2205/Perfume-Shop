package com.example.thucbashop.controllers;

import com.example.thucbashop.components.LocalizationUtils;
import com.example.thucbashop.components.MessageKeys;
import com.example.thucbashop.dtos.CategoryDTO;
import com.example.thucbashop.dtos.ProductDTO;
import com.example.thucbashop.dtos.ProductImageDTO;
import com.example.thucbashop.models.Product;
import com.example.thucbashop.models.ProductImage;
import com.example.thucbashop.repositories.ProductRepo;
import com.example.thucbashop.responese.ProductListRespones;
import com.example.thucbashop.responese.ProductResponse;
import com.example.thucbashop.services.IProductService;
import com.example.thucbashop.services.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final ProductRepo productRepo;
    private final LocalizationUtils localizationUtils;

    @GetMapping("")
    public ResponseEntity<ProductListRespones> getProducts(
            @RequestParam(defaultValue = "")String keyword,
            @RequestParam(defaultValue = "0",name = "category_id")Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int limit
    ){
        PageRequest pageRequest;
        if (categoryId != 0) {
            // Sử dụng PageRequest mặc định nếu có categoryId được cung cấp
            pageRequest = PageRequest.of(page, limit);
        } else {
            // Sử dụng PageRequest với sắp xếp theo id nếu không có categoryId
            pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        }
        Page<ProductResponse> productPage = productService.getAllProduct(keyword,categoryId,pageRequest);
        //lay trong so trang
        int totalPage = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListRespones.builder()
                        .products(products)
                        .totalPage(totalPage)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") long productId ){

        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){

        try {
           productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Đã Xóa : id : %d " +id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid  @RequestBody  ProductDTO productDTO,
                                           BindingResult result
    ){
        try {
            if(result.hasErrors()){
                List<String> errorMessage =  result.getFieldErrors().
                        stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product newProduct = productService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute("files") List<MultipartFile> files,
                                          @PathVariable("id") Long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);

            if (files.size() > ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizationUtils(MessageKeys.PRODUCT_UPLOAD_IMAGE_MAX_5));
            }

            List<ProductImage> productImages = new ArrayList<>();
            String thumbnailFileName = null;

            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);

                if (file != null && file.getSize() > 0) {
                    // Check file size
                    if (file.getSize() > 10 * 1024 * 1024) { // 10 MB
                        throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, localizationUtils.getLocalizationUtils(MessageKeys.PRODUCT_IMAGE_FILE_LARGE));
                    }

                    // Check if the file is an image
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(localizationUtils.getLocalizationUtils(MessageKeys.MUST_BE_IMAGE));
                    }

                    // Store the file and get the file name
                    String fileName = storefile(file);

                    // Create and save ProductImage
                    ProductImage productImage = productService.createProductImage(existingProduct.getId(),
                            ProductImageDTO.builder().imageUrl(fileName).build());
                    productImages.add(productImage);

                    // Set the first image as the thumbnail
                    if (i == 0) {
                        thumbnailFileName = fileName;
                    }
                }
            }

            // Update the product's thumbnail if an image was uploaded
            if (thumbnailFileName != null) {
                existingProduct.setThumbnail(thumbnailFileName);
                productRepo.save(existingProduct); // Ensure this method updates the product
            }

            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storefile(MultipartFile file) throws IOException{
        if(!isImageFile(file) || file.getOriginalFilename()==null){
            throw new IOException("KHONG PHAI PHAI ANH");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //thêm UUID vào trước tên file để đảm bảo file đó là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" +fileName;
        //đươờng dẫn thư mục muốn lưu file
        java.nio.file.Path uploadDir= Paths.get("uploads");
        //kiểm tra tồn tại
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //đường dẫn đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(),uniqueFileName);
        //sao chép file vào thư mục đích
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);// nếu có nó sẽ thay thế
        return uniqueFileName;
    }
    //kiểm tra xem phải file anhr không
    public boolean isImageFile(MultipartFile file){
        String contenType = file.getContentType();
        return contenType != null && contenType.startsWith("image/");
    }
    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i =0;i< 100;i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90_00_000)) // Chuyển đổi từ int sang float
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(1, 3)) // Chuyển đổi từ int sang long
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }
        return ResponseEntity.ok("FAKE PRODUCT CREATED SUCCESSFULLY");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id")long id ,@RequestBody ProductDTO productDTO){
        try {
            Product updateProduct = productService.updateProduct(id,productDTO);
            return ResponseEntity.ok("Update Thanh Cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {
                java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if(resource.exists() ){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }else{
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }

        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductByIds(@RequestParam("ids")String ids){
        try {
            List<Long> productId = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productId);
            return ResponseEntity.ok(products);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }





//    {
//        "name": "xx",
//            "price": -3213,
//            "thumbnail":"",
//            "description": " test",
//            "category_id": 1
//    }






















}
