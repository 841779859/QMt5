package com.example.asus.qmt5.Scan.bean;

public class HouseInfo {
	private String house_ID;
	private String house_name;
	private String house_introduce;
	private String house_addr;
	private String house_position;
	private String house_price;
	private String house_phonenum;
	private String house_img;
	private String house_state;
	private String house_img2;
	private String house_img3;
	private String house_img4;

	public HouseInfo() {
		super();
	}


	public HouseInfo(String house_ID) {
		super();
		this.house_ID=house_ID;
	}

	public HouseInfo(String house_ID,String house_state) {
		super();
		this.house_ID=house_ID;
		this.house_state=house_state;
	}



	public HouseInfo( String house_name,String house_addr,
					  String house_price, String house_state,String house_introduce )
	//,String house_img, String house_img2, String house_img3,String house_img4)
	{
		super();

		this.house_name = house_name;

		this.house_introduce = house_introduce;
		this.house_addr = house_addr;
		this.house_price = house_price;

		//this.house_img = house_img;
		this.house_state = house_state;
		//this.house_img2 = house_img2;
		//this.house_img3 = house_img3;
		//this.house_img4 = house_img4;
	}
	public HouseInfo( String house_ID,String house_name,String house_addr,String house_position,
					  String house_price, String house_state,String house_introduce )
	//,String house_img, String house_img2, String house_img3,String house_img4)
	{
		super();

		this.house_name = house_name;
		this.house_ID=house_ID;
		this.house_introduce = house_introduce;
		this.house_addr = house_addr;
		this.house_position=house_position;
		this.house_price = house_price;

		//this.house_img = house_img;
		this.house_state = house_state;
		//this.house_img2 = house_img2;
		//this.house_img3 = house_img3;
		//this.house_img4 = house_img4;
	}




	public HouseInfo(String house_ID, String house_name,
					 String house_introduce, String house_addr, String house_position,
					 String house_price, String house_phonenum, String house_img,
					 String house_state, String house_img2, String house_img3,
					 String house_img4) {
		super();
		this.house_ID = house_ID;
		this.house_name = house_name;

		this.house_introduce = house_introduce;
		this.house_addr = house_addr;
		this.house_position = house_position;
		this.house_price = house_price;
		this.house_phonenum = house_phonenum;
		this.house_img = house_img;
		this.house_state = house_state;
		this.house_img2 = house_img2;
		this.house_img3 = house_img3;
		this.house_img4 = house_img4;
	}





	public String getHouse_ID() {
		return house_ID;
	}


	public void setHouse_ID(String house_ID) {
		this.house_ID = house_ID;
	}

	public String getHouse_name() {
		return house_name;
	}

	public void setHouse_name(String house_name) {
		this.house_name = house_name;
	}

	public String getHouse_introduce() {
		return house_introduce;
	}

	public void setHouse_introduce(String house_introduce) {
		this.house_introduce = house_introduce;
	}

	public String getHouse_addr() {
		return house_addr;
	}

	public void setHouse_addr(String house_addr) {
		this.house_addr = house_addr;
	}

	public String getHouse_position() {
		return house_position;
	}

	public void setHouse_position(String house_position) {
		this.house_position = house_position;
	}

	public String getHouse_price() {
		return house_price;
	}

	public void setHouse_price(String house_price) {
		this.house_price = house_price;
	}

	public String getHouse_phonenum() {
		return house_phonenum;
	}

	public void setHouse_phonenum(String house_phonenum) {
		this.house_phonenum = house_phonenum;
	}

	public String getHouse_img() {
		return house_img;
	}

	public void setHouse_img(String house_img) {
		this.house_img = house_img;
	}
	public String getHouse_state() {
		return house_state;
	}


	public void setHouse_state(String house_state) {
		this.house_state = house_state;
	}

	public String getHouse_img2() {
		return house_img2;
	}

	public void setHouse_img2(String house_img2) {
		this.house_img2 = house_img2;
	}

	public String getHouse_img3() {
		return house_img3;
	}

	public void setHouse_img3(String house_img3) {
		this.house_img3 = house_img3;
	}

	public String getHouse_img4() {
		return house_img4;
	}

	public void setHouse_img4(String house_img4) {
		this.house_img4 = house_img4;
	}
}

