package com.lolacosmetics.ecomerce_back.service;


import com.lolacosmetics.ecomerce_back.dto.ProductoDTO;
import com.lolacosmetics.ecomerce_back.model.Producto;
import com.lolacosmetics.ecomerce_back.repository.CategoriaRepository;
import com.lolacosmetics.ecomerce_back.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Autowired
    private CategoriaService categoriaService;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crearProducto(@Valid ProductoDTO producto) {
        Producto producto1=new Producto();
        producto1.setNombre( producto.getNombre());
        producto1.setDescripcion(producto1.getDescripcion());
        producto1.setStock(producto.getStock());
        producto1.setPrecio(producto.getPrecio());

        producto1.setCategoria(categoriaRepository.getReferenceById(producto.getCategoriaId()));


        return productoRepository.save(producto1);
    }

    public void actualizarStock(Long id, Integer cantidad) {
        Producto producto = obtenerProducto(id);
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    public Producto actualizar(Long id, ProductoDTO productoDTO) {
        Producto prd =productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        prd.setNombre(productoDTO.getNombre());
        prd.setDescripcion(productoDTO.getDescripcion());
        prd.setPrecio(productoDTO.getPrecio());

        return prd;

    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}