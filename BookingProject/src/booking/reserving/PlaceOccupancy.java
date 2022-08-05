package booking.reserving;

import java.time.LocalDate;

abstract class Place implements Reservable {
	private String name;
	private States state;

	@SuppressWarnings("unused")
	private Place() {

	}

	protected Place(String name, States state) {
		super();
		this.name = name;
		this.state = state;
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected States getState() {
		return state;
	}

	protected void setState(States state) {
		this.state = state;
	}

	@Override
	public boolean isFreePlaceNow() {
		return state == States.FREE;
	}

	@Override
	public boolean checkin() {
		if (state == States.FREE) {
			state = States.BOOKED;
			return true;
		} else
			return false;
	}

	@Override
	public boolean checkout() {
		state = States.FREE;
		return true;
	}

}

class PlaceOccupancy extends Place implements Accommodation {

	private int capacity;
	private int occupancy;


	protected PlaceOccupancy(String name) {
		super(name,States.FREE);
		this.capacity = 1;
		this.occupancy = 0;

	}

	protected PlaceOccupancy(String name, int capacity) {
		super(name,States.FREE);
		this.occupancy = 0;
		this.capacity = capacity;
	}

	protected PlaceOccupancy(String name, States state, int capacity) {
		super(name,state);
		this.capacity = capacity;
		this.occupancy = 0;
	}

	@Override
	public boolean isFreePlaceNow() {
		return getState() == States.FREE && capacity > occupancy;
	}

	@Override
	public boolean checkin(int reservation) {
		if (reservation <= 0)
			return false;
		if (enoughPlace(reservation) && checkin()) {
			occupancy += reservation;
			return true;
		}
		return false;
	}



	@Override
	public boolean checkout() {
		occupancy = 0;
		return super.checkout();
	}

	public boolean enoughPlace(int reservation) {
		return capacity - occupancy >= reservation;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public int getOccupancy() {
		return occupancy;
	}

	protected void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	protected void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}



}

class PlaceOccupancyDays extends PlaceOccupancy {
	OccupancyDays days;

	private PlaceOccupancyDays() {
		super("default",0);
		reset();
	}

	protected PlaceOccupancyDays(String name, int capacity) {
		super(name, capacity);
		reset();
	}

	protected PlaceOccupancyDays(String name, States state, int capacity) {
		super(name, state, capacity);
		reset();
	}

	private PlaceOccupancyDays(String name) {
		super(name);
		reset();
	}

	private void reset() {
		days = new OccupancyDays();
	}

	public boolean reserving(LocalDate start, LocalDate end, int reservation) {
		if ( isReserved(start, end) && enoughPlace(reservation) ) {
			return days.setReserved(start, end);
		}
		return false;
	}

	public boolean isReserved(LocalDate start, LocalDate end) {
		return ! days.isReserved(start, end);
	}

	public boolean unReserving(LocalDate start, LocalDate end) {
		return days.unReserving(start, end);
	}

}