package booking.reserving;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class SwitcherTest {

	@Test
	void testCapacity() {
		BookingSystem sw = new BookingSystem();
		sw.fillTestingData();

		PlaceOccupancyDays data = new PlaceOccupancyDays("Blue", States.FREE, 100);
		assertEquals(100, data.getCapacity());
		sw.addRoomData(data);

		PlaceOccupancyDays data2 = new PlaceOccupancyDays("Red", States.FREE, 200);
		assertEquals(200, data2.getCapacity());
		sw.addRoomData(data2);

		assertEquals(100, sw.getCapacity("Blue"));
		assertEquals(200, sw.getCapacity("Red"));
		assertEquals(-1, sw.getCapacity("Bloe"));

	}

	@Test
	void testReset() {
		BookingSystem sw = new BookingSystem();
		sw.reset();
		assertEquals(true, sw.rooms.isEmpty());

		sw.fillTestingData();
		assertNotEquals(true, sw.rooms.isEmpty());
		assertEquals(3, sw.rooms.size());
	}

	@Test
	void testAddBookData() {
		BookingSystem sw = new BookingSystem();
		sw.reset();
		PlaceOccupancyDays data = new PlaceOccupancyDays("Blue", States.FREE, 120);
		assertNotNull(data);
		assertTrue(sw.addRoomData(data));
		assertEquals(States.FREE, data.getState());
		assertEquals(120, data.getCapacity());

		Reservable data2 = new PlaceOccupancyDays("Red", States.FREE, 120);
		sw.addRoomData((PlaceOccupancyDays) data2);
		assertEquals(2, sw.rooms.size());

		PlaceOccupancyDays data3 = new PlaceOccupancyDays("Green", States.FREE, 400);
		assertTrue(sw.addRoomData(data3));
		assertEquals(3, sw.rooms.size());
	}

	@Test
	void testBooking() {
		BookingSystem sw = new BookingSystem();
		// sw.fillTestingData();

		// assertEquals(0, sw.rooms.get(States.THREE.ordinal()).booked);

		PlaceOccupancyDays data = new PlaceOccupancyDays("Blue", States.FREE, 100);
		assertNotNull(data);

		sw.addRoomData(data);
		assertEquals(1, sw.rooms.size());
		assertTrue(0 == data.getName().compareTo("Blue"));

		assertTrue(sw.booking("Blue", 4));
		assertFalse(sw.booking("Blue", 0));
		sw.booking("Blue", 1);
		sw.booking("Blue", 2);

		assertEquals(4, data.getOccupancy());

		PlaceOccupancyDays data2 = new PlaceOccupancyDays("Red", States.FREE, 4);
		sw.addRoomData(data2);
		assertFalse(sw.booking("Red", -7));
		assertTrue(data2.getState() == States.FREE);
		assertFalse(sw.booking("Red", 100));
		assertTrue(data2.getState() == States.FREE);
		assertTrue(sw.booking("Red", 2));
	}

	@Test
	void testBookingDays() {
		BookingSystem sw = new BookingSystem();

		PlaceOccupancyDays data = new PlaceOccupancyDays("Blue", States.FREE, 2);
		assertTrue(sw.addRoomData(data));
		PlaceOccupancyDays data1 = new PlaceOccupancyDays("Red", States.FREE, 1);
		assertTrue(sw.addRoomData(data1));
		PlaceOccupancyDays data2 = new PlaceOccupancyDays("Green", States.FREE, 3);
		assertTrue(sw.addRoomData(data2));

		assertTrue(sw.booking("Green", LocalDate.of(2022, 06, 17), LocalDate.of(2022, 06, 25), 2));
		assertTrue(sw.getPlace("Green").checkin(2));
		assertTrue(sw.getPlace("Green").getState() == States.BOOKED);
		assertEquals(2, sw.getPlace("Green").getOccupancy());
		assertTrue(sw.getPlace("Green").checkout());
		assertEquals(0, sw.getPlace("Green").getOccupancy());
	}


	@Test
	public void testOccupancyDays() {
		OccupancyDays oc = new OccupancyDays();

		assertTrue(oc.setReserved(LocalDate.of(2022,06,14), LocalDate.of(2022,06,15)));

		assertEquals(1, oc.days.size());
		assertEquals(LocalDate.of(2022,06,14), oc.days.get(0));

	}

	@Test
	public void testOccupancyDaysUnReserving() {
		OccupancyDays oc = new OccupancyDays();
		assertTrue(oc.setReserved(LocalDate.of(2022,06,14), LocalDate.of(2022,06,17)));
		assertEquals(3, oc.days.size());

		assertTrue(oc.unReserving(LocalDate.of(2022,06,14), LocalDate.of(2022,06,17)));
		assertTrue(oc.days.isEmpty());

	}

	@Test
	public void testOccupancyDaysIsReserved() {
		OccupancyDays oc = new OccupancyDays();

		assertTrue(oc.setReserved(LocalDate.of(2022,06,14), LocalDate.of(2022,06,17)));

		assertTrue(oc.isReserved(LocalDate.of(2022,06,14)));
		assertFalse(oc.isReserved(LocalDate.of(2022,06,17)));
	}

	@Test
	public void testPlaceOccupancyDaysBooking() {
		PlaceOccupancyDays data = new PlaceOccupancyDays("Blue", 2);
		assertTrue(data.reserving(LocalDate.of(2022,06,14), LocalDate.of(2022,06,17), 2));
		assertFalse(data.reserving(LocalDate.of(2022,06,15), LocalDate.of(2022,06,17), 2));
		assertTrue(data.reserving(LocalDate.of(2022,06,19), LocalDate.of(2022,06,23), 2));
		assertFalse(data.reserving(LocalDate.of(2022,06,10), LocalDate.of(2022,06,12), 3));
	}

	@Test
	public void testOccupancyDaysListUnbooked() {
		OccupancyDays data = new OccupancyDays();
		assertTrue(data.setReserved(LocalDate.of(2022,06,25), LocalDate.of(2022,06,28)));
		List<LocalDate> list = data.listUnbookedDays(LocalDate.of(2022, 7, 31));
		assertFalse(list.containsAll(data.days));
		assertTrue(list.get(0).isEqual(LocalDate.now()));

		assertTrue(data.setReserved(LocalDate.of(2022,06,18), LocalDate.of(2022,06,20)));
		List<LocalDate> list1 = data.listUnbookedDays(LocalDate.of(2022, 7, 31));
		assertFalse(list1.containsAll(data.days));assertTrue(list1.get(0).isEqual(LocalDate.of(2022,06,20)));
	}

}
