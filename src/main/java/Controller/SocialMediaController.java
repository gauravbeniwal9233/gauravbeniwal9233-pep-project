package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();
        this.accountService = new AccountService(accountDAO);
        this.messageService = new MessageService(messageDAO, accountDAO);
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);

        if (registeredAccount != null) {
            //ctx.json(mapper.writeValueAsString(addedBook));
            ctx.json(mapper.writeValueAsString(registeredAccount)).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginRequest = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (registeredAccount != null) {
            ctx.json(mapper.writeValueAsString(registeredAccount)).status(200);
        } else {
            ctx.status(401);
        } 
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            ctx.json(mapper.writeValueAsString(createdMessage)).status(200);
        } else {
            ctx.status(400);
        } 
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(messages)).status(200);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        ObjectMapper mapper = new ObjectMapper();
        if (message != null) {
            ctx.json(mapper.writeValueAsString(message)).status(200);
        } else {
            ctx.result("").status(200);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);
        ObjectMapper mapper = new ObjectMapper();
        if (deletedMessage != null) {
            ctx.json(mapper.writeValueAsString(deletedMessage)).status(200);
        } else {
            ctx.result("").status(200);
        }
    }


}