package booking.price;

class Item {

	private int id;
	private String name;
	private String description;
	private Category category;
	private double price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Item(int id, String name, String description, Category category, double price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
	}

	public Item() {
	}

	@Override
	public String toString() {
		return (this.name + " (" + this.price + ")");
	}

}
