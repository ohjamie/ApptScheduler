package Model;

public class Customer {

    int customerId;
    int addressId;
    String customerName;
    String phoneNum;
    String address;
    String city;
    String postalCode;
    String createdBy;
    String createdOn;
    String lastUpdatedBy;
    String lastUpdatedOn;

    public Customer() {
        setCustomerId(customerId);
        setAddressId(addressId);
        setCustomerName(customerName);
        setPhoneNum(phoneNum);
        setAddress(address);
        setPostalCode(postalCode);
        setCreatedBy(createdBy);
        setCreatedOn(createdOn);
        setLastUpdatedBy(lastUpdatedBy);
        setLastUpdatedOn(lastUpdatedOn);
    }

    public int getCustomerId() { return customerId; }
    public int getAddressId() { return addressId; }
    public String getCustomerName() {
        return customerName;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public String getAddress() { return address; }
    public String getCity() {return city;}
    public String getPostalCode() { return postalCode; }
    public String getCreatedBy() { return createdBy; }
    public String getCreatedOn() {return createdOn; }
    public String getLastUpdatedBy() { return lastUpdatedBy; }
    public String getLastUpdatedOn() {return lastUpdatedOn; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
    public void setLastUpdatedOn(String lastUpdatedOn) { this.lastUpdatedOn = lastUpdatedOn; }
}
