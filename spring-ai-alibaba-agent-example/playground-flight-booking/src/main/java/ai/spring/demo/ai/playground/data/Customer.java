package ai.spring.demo.ai.playground.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户信息
 */
public class Customer {

	/**
	 * 客户姓名
	 */
	private String name;

	/**
	 * 机票预定详细信息的列表
	 */
	private List<Booking> bookings = new ArrayList<>();

	public Customer() {
	}

	public Customer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

}
