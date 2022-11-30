package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

// tests for event - from AlarmSystem
public class EventTest {
	private Event e;

	@BeforeEach
	public void runBefore() {
		e = new Event("Sensor open at door");
	}
	
	@Test
	public void testEvent() {
		assertEquals("Sensor open at door", e.getDescription());
	}

	@Test
	public void testToString() {
		assertEquals(e.getDate().toString() + "\n" + "Sensor open at door", e.toString());
	}
}
