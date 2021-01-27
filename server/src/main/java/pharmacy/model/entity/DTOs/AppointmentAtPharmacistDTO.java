package pharmacy.model.entity.DTOs;

import pharmacy.model.entity.DateAndTimeConverter;
import pharmacy.model.entity.appointments.AppointmentAtPharmacist;

public class AppointmentAtPharmacistDTO {

    private Long id;
    private String pharmacist;
    private String startTime;
 
	private String endTime;
    private String date;
    private String status;
    
    public AppointmentAtPharmacistDTO(AppointmentAtPharmacist app) {
    	this.id = app.getId();
    	this.pharmacist = app.getPharmacist().getFirstName() + " " +app.getPharmacist().getLastName();
    	this.startTime = DateAndTimeConverter.convertTimeFromDBFormat(app.getEndTime());
    	this.endTime = DateAndTimeConverter.convertTimeFromDBFormat(app.getStartTime());
    	this.date = app.getDate();
    	this.status = app.getStatus();
    	
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPharmacist() {
		return pharmacist;
	}
	public void setPharmacist(String pharmacist) {
		this.pharmacist = pharmacist;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
   
}
