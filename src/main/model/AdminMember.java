package model;

public class AdminMember extends Member {
    // an admin member who can only register books and view the transaction history of the library

    private boolean isAdmin;

    public AdminMember() {
        super("admin");
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
