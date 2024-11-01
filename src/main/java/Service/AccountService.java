package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount (String username, String password) throws IllegalArgumentException {
        if (username == null || username.isBlank() || password == null || password.length() < 4) {
            throw new IllegalArgumentException("Invalid input !!");
        }

        if (accountDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("user already exists !!");
        }

        Account newAccount = new Account(username, password);
        Account createdAccount = accountDAO.insertAccount(newAccount);
        if (createdAccount == null) {
            throw new RuntimeException("Failed to register account !!");
        }

        return createdAccount;
    }
}
