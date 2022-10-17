package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AdminMemberTest {

    @Test
    public void adminMemberTest() {
        AdminMember admin = new AdminMember();
        assertEquals(admin.getName(), "admin");
        assertTrue(admin.isAdmin());
        assertFalse(admin.isBorrowingBooks());
        assertEquals(admin.getReviews().size(), 0);
        assertEquals(admin.getTransactions().size(),0);
        assertEquals(admin.getBorrowedBooks().size(), 0);
    }
}
