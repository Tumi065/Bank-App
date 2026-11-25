package BankingSystemOOAD.dao;

/**
 * Holds the one BankStore shared by every screen while the program runs.
 * In Task 7 the DAO classes will replace the in-memory store.
 */
public final class Session {

    private static final BankStore STORE = new BankStore();

    private Session() {
    }

    public static BankStore getStore() {
        return STORE;
    }
}
