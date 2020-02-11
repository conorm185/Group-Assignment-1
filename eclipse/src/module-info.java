module GroupAssignment1 {
	requires org.junit.jupiter.api;
	requires java.desktop;
	requires com.google.gson;

	exports ics372.assignment1.model;
	opens ics372.assignment1.model;
	
	exports ics372.assignment1.test;
	opens ics372.assignment1.test;
	
	exports ics372.assignment1.controller;
	opens ics372.assignment1.controller;
	
}