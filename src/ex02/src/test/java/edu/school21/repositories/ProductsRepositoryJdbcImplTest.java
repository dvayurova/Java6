package edu.school21.repositories;

import edu.school21.models.Product;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductsRepositoryJdbcImplTest {
    DataSource dataSource;
    Connection connection;
    ProductsRepository productsRepository;

    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = Arrays.asList(
            new Product(0L, "milk", 80),
            new Product(1L, "cheese", 200),
            new Product(2L, "bread", 50),
            new Product(3L, "eggs", 70),
            new Product(4L, "tea", 90)
    ) ;
    final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(1L, "cheese", 200);
    final Product EXPECTED_UPDATED_PRODUCT = new Product(4L, "juice", 100);

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = (DataSource) new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
         connection = dataSource.getConnection();
        productsRepository = new ProductsRepositoryJdbcImpl(connection);
    }

    @AfterEach
    void tearDown(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFindAll() throws SQLException {
        Assertions.assertEquals(EXPECTED_FIND_ALL_PRODUCTS, productsRepository.findAll());
    }

    @Test
    void testFindById() throws SQLException {
        Assertions.assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, productsRepository.findById(1L).get());
    }

    @Test
    void testUpdate() throws SQLException, NotSavedSubEntityException {
        Product product = new Product(4L, "juice", 100);
        productsRepository.update(product);
        Assertions.assertEquals(EXPECTED_UPDATED_PRODUCT, productsRepository.findById(4L).get());
    }

    @Test
    void testSave() throws SQLException, NotSavedSubEntityException {
        Product product = new Product(5L, "orange", 40);
        productsRepository.save(product);
        Assertions.assertEquals(product, productsRepository.findById(5L).get());
    }

    @Test
    void testDelete() throws SQLException, NotSavedSubEntityException {
        productsRepository.delete(1L);
        Assertions.assertTrue(productsRepository.findById(1L).isEmpty());
    }

    @Test
    void findByIdIncorrectId() throws SQLException {
        Assertions.assertEquals(Optional.empty(), productsRepository.findById(10L));
    }

    @Test
    void updateIncorrectId() {
        assertThrows(NotSavedSubEntityException.class, () -> {
            Product product = new Product(85L, "e", 6);
            productsRepository.update(product);
        });
    }

    @Test
    void deleteIncorrectId() {
        assertThrows(NotSavedSubEntityException.class, () -> {
            productsRepository.delete(15L);
        });
    }

}
