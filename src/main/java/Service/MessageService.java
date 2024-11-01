package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message createMessage (Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() >= 255) {
            return null;
        }
        if (!messageDAO.userExists(message.getPosted_by())) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

}
