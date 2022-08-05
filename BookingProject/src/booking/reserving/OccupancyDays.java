package booking.reserving;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OccupancyDays {
	String roomName;
	public List<LocalDate> days = new ArrayList<>();

	public OccupancyDays() {
	}

	public OccupancyDays(String name) {
		this.roomName = name;
	}

	public boolean setReserved(LocalDate start, LocalDate end) {

		for(int i=0; i < Period.between(start, end).getDays(); ++i) {
			days.add(start.plusDays(i));
		}
		return true;
	}

	public boolean unReserving(LocalDate start, LocalDate end) {
		for(int i=0; i < Period.between(start, end).getDays(); ++i) {
			days.remove(start.plusDays(i));
		}
		return true;
	}

	public boolean isReserved(LocalDate start) {
		return
			days.stream()
				.sorted()
				.anyMatch(d -> d.isEqual(start));
	}

	public boolean isReserved(LocalDate start, LocalDate end) {
		for(int i=0; i < Period.between(start, end).getDays(); i++) {
			if ( isReserved(start.plusDays(i)) ) return true;
		}
		return false;
	}

	public List<LocalDate> listUnbookedDays(LocalDate end) {
		return
		LocalDate.now()
			.datesUntil(end)
			.filter(d -> !isReserved(d))
			.collect(Collectors.toList())
			;

	}

}