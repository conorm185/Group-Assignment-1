module GroupAssignment1 {
	requires org.junit.jupiter.api;
	requires java.desktop;
	requires json.simple;
	requires com.google.gson;

	exports ics372.assignment1.model;

	opens ics372.assignment1.model;
}