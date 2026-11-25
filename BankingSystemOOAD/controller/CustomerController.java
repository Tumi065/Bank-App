package BankingSystemOOAD.controller;

import BankingSystemOOAD.dao.BankStore;
import BankingSystemOOAD.dao.Session;
import BankingSystemOOAD.model.Customer;

public class CustomerController {

    //Creates,stores a new customer. Throws with a message to show on screen if something is wrong.//
    public Customer registerCustomer(
            String name,
            String address,
            String phone,
            String username,
            String password) {

        String n = clean(name);
        String a = clean(address);
        String p = clean(phone);
        String u = clean(username);
        String pw = password == null ? "" : password;

        if (n.isEmpty() || a.isEmpty() || p.isEmpty() || u.isEmpty() || pw.isEmpty()) {
            throw new IllegalArgumentException("Please fill in all the fields.");
        }
        if (pw.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }

        BankStore store = Session.getStore();
        if (store.findByUsername(u) != null) {
            throw new IllegalArgumentException("That username is already taken.");
        }

        Customer customer = new Customer(store.nextCustomerId(), n, a, p, u, pw);
        store.addCustomer(customer);
        return customer;
    }

    private String clean(String text) {
        return text == null ? "" : text.trim();
    }
}
