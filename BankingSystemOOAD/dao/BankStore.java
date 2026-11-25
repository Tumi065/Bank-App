package BankingSystemOOAD.dao;

import BankingSystemOOAD.model.Customer;

import java.util.ArrayList;
import java.util.List;

//Keeps the customers in memory while the program runs.//
public class BankStore {

    private final List<Customer> customers = new ArrayList<>();
    private int customerCount = 0;
    private int accountCount = 0;

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Customer findByUsername(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equalsIgnoreCase(username)) {
                return c;
            }
        }
        return null;
    }

    public String nextCustomerId() {
        customerCount++;
        return String.format("C%03d", customerCount);
    }

    //prefix is SAV, INV or CHQ//
    public String nextAccountNumber(String prefix) {
        accountCount++;
        return String.format("%s-%03d", prefix, accountCount);
    }
}
