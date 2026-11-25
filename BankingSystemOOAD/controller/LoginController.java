package BankingSystemOOAD.controller;

import BankingSystemOOAD.dao.Session;
import BankingSystemOOAD.model.Customer;

public class LoginController {

    /** Returns the customer when the details are right, otherwise throws with a message to show on screen. */
    public Customer login(String username, String password) {
        String user = username == null ? "" : username.trim();
        String pass = password == null ? "" : password;

        if (user.isEmpty() || pass.isEmpty()) {
            throw new IllegalArgumentException("Please enter your username and password.");
        }

        Customer customer = Session.getStore().findByUsername(user);
        if (customer == null || !customer.verifyPassword(pass)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return customer;
    }
}
