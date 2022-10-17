package model;

public class AdminMember extends Member {
    // ADMIN MEMBER:
    // an admin member who is identified as admin, with name "admin". has access to admin menu in library app

    private boolean isAdmin;

    // EFFECTS: creates an admin member, a member of name "admin"
    public AdminMember() {
        super("admin");
    }

    // EFFECTS: returns true to reflect this is an admin member
    @Override
    public boolean isAdmin() {
        return true;
    }
}
