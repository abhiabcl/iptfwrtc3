package flavor.tech.com.iptfwebrtc.util;

public class RoomData {
	
	private String customer_mobile;
	private String room_url;
	private String current_timestamp;
	
	public String getCustomer_mobile() {
		return customer_mobile;
	}
	public void setCustomer_mobile(String customer_mobile) {
		this.customer_mobile = customer_mobile;
	}

	public String getCurrent_timestamp() {
		return current_timestamp;
	}
	public void setCurrent_timestamp(String current_timestamp) {
		this.current_timestamp = current_timestamp;
	}
	
	public String getRoom_url() {
		return room_url;
	}
	public void setRoom_url(String room_url) {
		this.room_url = room_url;
	}
}
