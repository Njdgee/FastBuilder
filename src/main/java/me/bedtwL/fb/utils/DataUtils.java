package me.bedtwL.fb.utils;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DataUtils {
    @Getter
    public static HikariDataSource dataSource;


    //Stores the database connection information
    public static HikariDataSource setupDatabase() {
        final HikariDataSource dataSource = new HikariDataSource();
        final String host = "localhost";
        final String database = "player_status";
        final String url = "jdbc:mysql://" + host + ":3306/" + database;
        dataSource.setJdbcUrl(url);
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.addDataSourceProperty("autoReconnect", "true");
        dataSource.addDataSourceProperty("autoReconnectForPools", "true");
        dataSource.addDataSourceProperty("interactiveClient", "true");
        dataSource.addDataSourceProperty("characterEncoding", "UTF-8");
        dataSource.addDataSourceProperty("useSSL", "false");
        return dataSource;
    }
    public static Long getPb(UUID uuid) {
        try (Connection connection = getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `fb` WHERE `uuid` = ?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next())
                return result.getLong("pb");
        } catch (SQLException ignored) {
        }
        return 0L;
    }
    public static String getStringValue(UUID uuid,String K) {
        try (Connection connection = getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `fb` WHERE `uuid` = ?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next())
                return result.getString(K);
        } catch (SQLException ignored) {
        }
        return "";
    }
    public static boolean setStringValue(UUID uuid, String K ,String V) {
        try (Connection connection = getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `fb`(`uuid`, `"+K+"`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `"+K+"` = ?"
            );
            statement.setString(1, uuid.toString());
            statement.setString(2, V);
            statement.setString(3, V);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();  // Print the exception details for debugging
            return false;
        }
    }
    public static boolean setPb(UUID uuid, Long V) {
        try (Connection connection = getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `fb`(`uuid`, `pb`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `pb` = ?"
            );
            statement.setString(1, uuid.toString());
            statement.setLong(2, V);
            statement.setLong(3, V);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();  // Print the exception details for debugging
            return false;
        }
    }
}