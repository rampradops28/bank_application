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
        /**
         * salaryProducts = {
           "SAL1" → SalaryProduct(type="SALARY", code="SAL1", minAge=21, minTransactionAmount=100)
           "SAL2" → SalaryProduct(type="SALARY", code="SAL2", minAge=25, minTransactionAmount=200)}
         */ 

        for(StudentProduct p: loader.loadStudentProducts()) {
            studentProducts.put(p.getCode(), p);
        }
        /** 
         * studentProducts = {
           "STU1" → StudentProduct(type="STUDENT", code="STU1", maxAge=17, maxTransactionAmount=50)
           "STU2" → StudentProduct(type="STUDENT", code="STU2", maxAge=18, maxTransactionAmount=80)}
         */

        System.out.println("ProductRegistry initialized");
        System.out.println("Salary products loaded: "+salaryProducts.keySet()); // [SAL1, SAL2]
        System.out.println("Student products loaded: "+studentProducts.keySet()); // [STU1, STU2]
    }

    public Optional<Product> find(String productType, String productCode) {

        if("SALARY".equalsIgnoreCase(productType)) {
            return Optional.ofNullable(salaryProducts.get(productCode));
        }
        /**
         * Optional<Product> p = registry.find("SALARY", "SAL1");
         * Output : SalaryProduct(SAL1, minAge=21, minTransactionAmount=100) 
         */

        if("STUDENT".equalsIgnoreCase(productType)) {
            return Optional.ofNullable(studentProducts.get(productCode));
        }
        /**
         * Optional<Product> p = registry.find("STUDENT", "STU2");
         * StudentProduct(STU2, maxAge=18, maxTransactionAmount=80)
         */

        return Optional.empty();
    }
}
