package ai.spring.demo.ai.playground.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 航班预定数据
 */
public class BookingData {

	/**
	 * 客户信息的列表
	 */
	private List<Customer> customers = new ArrayList<>();

	/**
	 * 机票预定详细信息的列表
	 */
	private List<Booking> bookings = new ArrayList<>();

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

}
