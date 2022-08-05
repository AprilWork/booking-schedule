package booking.reserving;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

enum States {
	FREE, BOOKED, ANOTHER
}

class BookingSystem {

	List<PlaceOccupancyDays> rooms = new ArrayList<>();

	public BookingSystem() {
		reset();
	}

	public PlaceOccupancyDays getPlace(String name) throws NoSuchElementException {

		return
			rooms.stream()
				.filter(b -> b.getName() == name)
				.findFirst()
				.get();
		}

	public boolean booking(String name, int reservation) {
		try {
			return getPlace(name).checkin(reservation);
		}
		catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean booking(String name, LocalDate start, LocalDate end, int reservation) {
		try {
			return getPlace(name).reserving(start,end,reservation);
		}
		catch (NoSuchElementException e) {
			return false;
		}

	}

	public int getCapacity(String name) {
		try {
			return getPlace(name).getCapacity();
		} catch (NoSuchElementException e) {
			return -1;
		}
	}

	public void reset() {
		rooms.clear();
	}

	public boolean addRoomData(PlaceOccupancyDays data) {
		return rooms.add(data);
	}

	public void fillTestingData() {
		reset();
		for (int i = 0; i < States.values().length; ++i) {
			PlaceOccupancyDays data = new PlaceOccupancyDays(States.values()[i].name() + i, (i + 1) * 100);
			rooms.add(data);
		}
	}

}
