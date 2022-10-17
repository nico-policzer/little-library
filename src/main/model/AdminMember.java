package model;

public class AdminMember extends Member {
    // ADMIN MEMBER:
    // an admin member who is identified as admin, with name "admin". has access to admin menu in library app

    private boolean isAdmin;

    public AdminMember() {
        super("admin");
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
