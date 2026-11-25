package BankingSystemOOAD.controller;

import BankingSystemOOAD.dao.BankStore;
import BankingSystemOOAD.dao.Session;
import BankingSystemOOAD.model.Account;
import BankingSystemOOAD.model.ChequeAccount;
import BankingSystemOOAD.model.Customer;
import BankingSystemOOAD.model.InterestCalculations;
import BankingSystemOOAD.model.InvestmentAccount;
import BankingSystemOOAD.model.SavingsAccount;

public class AccountController {

    private static final double INVESTMENT_MINIMUM = 500.00;

    /** Opens a Savings, Investment or Cheque account for the customer and returns it. */
    public Account openAccount(Customer customer, String type, String branch,
                               String depositText, String employerName, String employerAddress) {

        String branchName = clean(branch);
        if (branchName.isEmpty()) {
            throw new IllegalArgumentException("Please enter the branch.");
        }

        BankStore store = Session.getStore();
        Account account;

        if ("Investment".equals(type)) {
            double opening = parseAmount(depositText, false);
            if (opening < INVESTMENT_MINIMUM) {
                throw new IllegalArgumentException("Minimum opening deposit is BWP500.00");
            }
            account = new InvestmentAccount(store.nextAccountNumber("INV"), branchName, customer, opening);

        } else if ("Cheque".equals(type)) {
            String employer = clean(employerName);
            String employerAddr = clean(employerAddress);
            if (employer.isEmpty() || employerAddr.isEmpty()) {
                throw new IllegalArgumentException("Cheque accounts need the employer name and address.");
            }
            double opening = parseAmount(depositText, true);
            account = new ChequeAccount(store.nextAccountNumber("CHQ"), branchName, customer,
                    employer, employerAddr);
            if (opening > 0) {
                account.deposit(opening, "Opening deposit");
            }

        } else if ("Savings".equals(type)) {
            double opening = parseAmount(depositText, true);
            account = new SavingsAccount(store.nextAccountNumber("SAV"), branchName, customer);
            if (opening > 0) {
                account.deposit(opening, "Opening deposit");
            }

        } else {
            throw new IllegalArgumentException("Please choose an account type.");
        }

        customer.addAccount(account);
        return account;
    }

    /** Deposits into the account and returns the amount that was deposited. */
    public double deposit(Account account, String amountText, String note) {
        if (account == null) {
            throw new IllegalArgumentException("Please choose an account.");
        }
        double amount = parseAmount(amountText, false);
        String reference = clean(note);
        account.deposit(amount, reference.isEmpty() ? "Deposit" : reference);
        return amount;
    }

    /** Withdraws from an Investment or Cheque account and returns the amount withdrawn. */
    public double withdraw(Account account, String amountText) {
        if (account == null) {
            throw new IllegalArgumentException("Please choose an account.");
        }
        double amount = parseAmount(amountText, false);
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        boolean ok;
        if (account instanceof InvestmentAccount) {
            ok = ((InvestmentAccount) account).withdraw(amount);
        } else if (account instanceof ChequeAccount) {
            ok = ((ChequeAccount) account).withdraw(amount);
        } else {
            throw new IllegalArgumentException("Withdrawals are not allowed on Savings accounts.");
        }

        if (!ok) {
            throw new IllegalArgumentException("Insufficient funds. Balance is "
                    + "BWP " + String.format(java.util.Locale.US, "%,.2f", account.getBalance()));
        }
        return amount;
    }

    /** Pays interest into the account and returns the interest paid. */
    public double payInterest(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Please choose an account.");
        }
        if (!(account instanceof InterestCalculations)) {
            throw new IllegalArgumentException("Cheque accounts do not earn interest.");
        }
        InterestCalculations interestAccount = (InterestCalculations) account;
        double interest = interestAccount.calculateInterest();
        interestAccount.payInterest();
        return interest;
    }

    private String clean(String text) {
        return text == null ? "" : text.trim();
    }

    /** Reads an amount typed by the user. An empty box counts as 0 only when allowEmpty is true. */
    private double parseAmount(String text, boolean allowEmpty) {
        String t = clean(text).replace(",", "");
        if (t.isEmpty()) {
            if (allowEmpty) {
                return 0;
            }
            throw new IllegalArgumentException("Please enter an amount.");
        }

        double value;
        try {
            value = Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a number, e.g. 500.00");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Amount must be a number, e.g. 500.00");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        return value;
    }
}
