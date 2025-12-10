package org.ram.product.factory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.ram.product.model.Product;
import org.ram.product.model.SalaryProduct;
import org.ram.product.model.StudentProduct;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ProductRegistry {

    private final YamlProductLoader loader;

    private final Map<String, Product> salaryProducts = new HashMap<>();
    private final Map<String, Product> studentProducts = new HashMap<>();

    public ProductRegistry(YamlProductLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    void init() {
        for(SalaryProduct p: loader.loadSalaryProducts()) {
            salaryProducts.put(p.getCode(), p);
        }

        for(StudentProduct p: loader.loadStudentProducts()) {
            studentProducts.put(p.getCode(), p);
        }

        System.out.println("ProductRegistry initialized");
        System.out.println("Salary products loaded: "+salaryProducts.keySet());
        System.out.println("Student products loaded: "+studentProducts.keySet());
    }

    public Optional<Product> find(String productType, String productCode) {

        if("SALARY".equalsIgnoreCase(productType)) {
            return Optional.ofNullable(salaryProducts.get(productCode));
        }

        if("STUDENT".equalsIgnoreCase(productType)) {
            return Optional.ofNullable(studentProducts.get(productCode));
        }

        return Optional.empty();
    }
}
