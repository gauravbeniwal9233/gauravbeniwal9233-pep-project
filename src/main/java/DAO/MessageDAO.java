package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public boolean userExists (int userId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int messageId = generatedKeys.getInt(1);
                    return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertinf: " + e.getMessage());
            return null;
        }
        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return messages;
    }
}
