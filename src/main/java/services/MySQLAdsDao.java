package services;

import models.Ad;
import com.mysql.cj.jdbc.Driver;
import configs.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLAdsDao implements Ads {

    private final Connection connection;

    public MySQLAdsDao(Config config) {

        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager
                    .getConnection(
                            config.getUrl(),
                            config.getUserName(),
                            config.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ad> all() {
        String query = "SELECT * FROM ads";
        ResultSet rs;
        List<Ad> ads = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            while (rs.next()) {
                ads.add(new Ad(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("title"),
                        rs.getString("description")
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ads;
    }

    @Override
    public Long insert(Ad ad) {
        String query = "INSERT INTO ads (user_id, title, description) VALUES (?, ?, ?)";
        ResultSet rs;
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, ad.getUserId());
            statement.setString(2, ad.getTitle());
            statement.setString(3, ad.getDescription());
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
