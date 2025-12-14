package org.ram.product.factory;

import jakarta.enterprise.context.ApplicationScoped;
import org.ram.exception.InvalidProductException;
import org.ram.product.model.Product;
import org.ram.product.strategy.ProductValidator;
import org.ram.product.strategy.SalaryProductValidator;
import org.ram.product.strategy.StudentProductValidator;

import java.util.Optional;

@ApplicationScoped
public class ProductFactory {

    private final ProductRegistry registry;

    public ProductFactory(ProductRegistry registry) {
        this.registry = registry;
    }

    public Product getProduct(String productType, String productCode) {
        Optional<Product> opt = registry.find(productType, productCode);

        if(opt.isEmpty()) {
            throw new InvalidProductException(
                    "Unknown productCode '"+productCode+ "' for type "+productType
            );
        }
        return opt.get();
    }

    public ProductValidator getValidator(String productType) {

        if("SALARY".equalsIgnoreCase(productType)) {
            return new SalaryProductValidator();
        }

        if("STUDENT".equalsIgnoreCase(productType)) {
               return new StudentProductValidator();
        }

        throw new InvalidProductException("Invalid product type: "+productType);
    }
}
