package org.ram.product.factory;

import jakarta.enterprise.context.ApplicationScoped;
import org.ram.product.model.SalaryProduct;
import org.ram.product.model.StudentProduct;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class YamlProductLoader {


    public List<SalaryProduct> loadSalaryProducts() {

        // create yaml parser
        Yaml yaml = new Yaml();

        // read the yaml file from resources
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("salary-products.yml");

        // Parse yaml into Java map
        Map<String, List<Map<String, Object>>> data = yaml.load(inputStream);

        List<SalaryProduct> result = new ArrayList<>();

        // get the list
        List<Map<String, Object>> list = data.get("salary-products");

        if (list == null) {
            throw new RuntimeException("Missing 'salary-products' root key in salary-products.yml");
        }

        for(Map<String, Object> item : list) {
            SalaryProduct p = new SalaryProduct();
            p.setType("SALARY");
            p.setCode((String) item.get("code"));
            p.setMinAge((Integer) item.get("minAge"));
            p.setMinTransactionAmount((Double) item.get("minTransactionAmount"));

            result.add(p);
        }

        return result;

    }

    public List<StudentProduct> loadStudentProducts() {
        Yaml yaml = new Yaml();

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("student-products.yml");

        Map<String, List<Map<String, Object>>> data = yaml.load(inputStream);

        List<StudentProduct> result = new ArrayList<>();
        
        List<Map<String, Object>> list = data.get("student-products");

        if (list == null) {
            throw new RuntimeException("Missing 'student-products' in student-products.yml");
        }

        for(Map<String, Object> item: list) {
            StudentProduct p = new StudentProduct();

            p.setType("STUDENT");
            p.setCode((String) item.get("code"));
            p.setMaxAge((Integer) item.get("maxAge"));
            p.setMaxTransactionAmount((Double) item.get("maxTransactionAmount"));

            result.add(p);
        }

        return result;
    }
}
