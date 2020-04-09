package Model;

import java.time.LocalDateTime;

public class Appointment {

    int appointmentID;
    String customerName;
    String consultantName; // same as created by when getting data from db
    String apptType;
    LocalDateTime start;
    LocalDateTime end;
    String createdOn;
    String lastUpdatedBy;
    String lastUpdatedOn;

    public Appointment(){
        setAppointmentID(appointmentID);
        setCustomerName(customerName);
        setConsultantName(consultantName);
        setApptType(apptType);
        setStart(start);
        setEnd(end);
        setCreatedOn(createdOn);
        setLastUpdatedBy(lastUpdatedBy);
        setLastUpdatedOn(lastUpdatedOn);
    }

    public int getAppointmentID() {return appointmentID; }
    public String getCustomerName() {
        return customerName;
    }
    public String getConsultantName() {
        return consultantName;
    }
    public String getApptType() { return apptType; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() {return end;}
    public String getCreatedOn() {return createdOn; }
    public String getLastUpdatedBy() { return lastUpdatedBy; }
    public String getLastUpdatedOn() {return lastUpdatedOn; }

    public void setAppointmentID(int appointmentID) {this.appointmentID = appointmentID; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setConsultantName(String consultantName) { this.consultantName = consultantName; }
    public void setApptType(String apptType) { this.apptType = apptType; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public void setEnd(LocalDateTime end) { this.end = end; }
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
    public void setLastUpdatedOn(String lastUpdatedOn) { this.lastUpdatedOn = lastUpdatedOn; }
}
