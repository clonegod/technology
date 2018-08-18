package part2_oop.grovvybean

class MrBean {
	// groovy style properties
	String firstname, lastname
	
	// getter for derived property
	String getName() {
		return "$firstname $lastname"
	}
}
