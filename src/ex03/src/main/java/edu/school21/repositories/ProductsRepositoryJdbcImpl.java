package edu.school21.repositories;

import edu.school21.exceptions.NotSavedSubEntityException;
import edu.school21.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository{

    private Connection connection;

    public ProductsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> productList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM product");
        while (resultSet.next()) {
           Product product = new Product(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("price"));
            productList.add(product);
        }
        return productList;
    }

    @Override
    public Optional<Product> findById(Long id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE id = " + id);
        if (resultSet.next()) {
            Optional<Product> product = Optional.of(new Product(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("price")));
            return product;
        }
        return Optional.empty();
    }

    @Override
    public void update(Product product) throws SQLException, NotSavedSubEntityException {
        PreparedStatement statement = connection.prepareStatement("update product set name = ?, price = ? where id = ?");
        statement.setString(1, product.getName());
        statement.setInt(2, product.getPrice());
        statement.setLong(3, product.getId());
        if (statement.executeUpdate() == 0) {
            throw new NotSavedSubEntityException("Updating failed, incorrect identifier");
        }
    }

    @Override
    public void save(Product product) throws SQLException, NotSavedSubEntityException {
        PreparedStatement statement = connection.prepareStatement("insert into product (name, price) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, product.getName());
        statement.setInt(2, product.getPrice());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
           product.setId(resultSet.getLong(1));
        }
    }

    @Override
    public void delete(Long id) throws SQLException, NotSavedSubEntityException {
        Statement statement = connection.createStatement();
        String deleteQuery = "delete from product where id = " + id;
        if (statement.executeUpdate(deleteQuery) == 0) {
            throw new NotSavedSubEntityException("Delete failed");
        }
    }
}
