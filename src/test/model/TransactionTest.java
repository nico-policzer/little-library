package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    @Test
    public void testTransaction() {
        Member m1 = new Member("John Smith");
        Book b1 = new Book("Heart of Darkness", "Classic", "Joseph Conrad");
        Transaction t1 = new Transaction(b1.getTitle(), m1.getName());
        assertEquals(t1.getBook(), b1.getTitle());
        assertEquals(t1.getMember(), m1.getName());
    }
}
