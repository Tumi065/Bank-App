package BankingSystemOOAD.dao;

//Holds every screen while the program runs.//
public final class Session {

    private static final BankStore STORE = new BankStore();

    private Session() {
    }

    public static BankStore getStore() {
        return STORE;
    }
}
