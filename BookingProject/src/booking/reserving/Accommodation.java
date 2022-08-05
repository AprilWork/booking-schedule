package booking.reserving;

public interface Accommodation extends Reservable {

	public int getCapacity();
	public int getOccupancy();
	public boolean checkin(int reservation);

}
