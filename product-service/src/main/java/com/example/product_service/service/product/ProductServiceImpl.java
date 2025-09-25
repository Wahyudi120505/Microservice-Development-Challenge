package com.example.product_service.service.product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.dto.product.ProductRequest;
import com.example.product_service.dto.product.ProductResponse;
import com.example.product_service.model.Category;
import com.example.product_service.model.Product;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.image.ConvertImageService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ConvertImageService convertImageService;

    @Override
    public void create(ProductRequest request, MultipartFile image) {
        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new RuntimeException("Kategori tidak boleh kosong! " + request.getNama());
        }

        if (productRepository.existsByNameIgnoreCase(request.getNama())) {
            throw new RuntimeException("Produk dengan nama '" + request.getNama() + "' sudah ada!");
        }

        Category kategori = categoryRepository.findCategoryByName(request.getCategory());
        if (kategori == null) {
            throw new RuntimeException("Kategori tidak ditemukan: " + request.getCategory());
        }

        try {
            String status = request.getQuantity() > 0 ? "Tersedia" : "Tidak Tersedia";

            Product product = Product.builder()
                    .name(request.getNama())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .photo(convertImageService.convertBlob(image))
                    .description(request.getDesc())
                    .status(status)
                    .deleted(false)
                    .category(kategori)
                    .build();

            productRepository.save(product);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Gagal memproses data produk: " + e.getMessage());
        }
    }

    @Override
    public void update(Integer id, ProductRequest request, MultipartFile image) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id: " + id));

        if (request.getNama() != null && !request.getNama().isBlank()) {
            existing.setName(request.getNama());
        }

        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }

        if (request.getQuantity() != null) {
            existing.setQuantity(request.getQuantity());
            existing.setStatus(request.getQuantity() > 0 ? "Tersedia" : "Tidak Tersedia");
        }

        if (request.getDesc() != null) {
            existing.setDescription(request.getDesc());
        }

        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            Category kategori = categoryRepository.findCategoryByName(request.getCategory());
            if (kategori == null) {
                throw new RuntimeException("Kategori tidak ditemukan: " + request.getCategory());
            }
            existing.setCategory(kategori);
        }

        if (image != null) {
            try {
                existing.setPhoto(convertImageService.convertBlob(image));
            } catch (IOException | SQLException e) {
                throw new RuntimeException("Gagal mengupdate gambar: " + e.getMessage());
            }
        }

        productRepository.save(existing);
    }

    @Override
    public ProductResponse getById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id: " + id));
        return toProdukResponse(product);
    }

    @Override
    public void delete(Integer id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id: " + id));
        existing.setDeleted(true);
        productRepository.save(existing);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::toProdukResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse toProdukResponse(Product produk) {
        try {
            return ProductResponse.builder()
                    .nama(produk.getName())
                    .price(produk.getPrice())
                    .quantity(produk.getQuantity())
                    .photo(convertImageService.convertImage(produk.getPhoto()))
                    .desc(produk.getDescription())
                    .status(produk.getStatus())
                    .category(produk.getCategory().getName())
                    .deleted(produk.getDeleted())
                    .build();
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Gagal mengkonversi data produk: " + e.getMessage());
        }
    }
}
