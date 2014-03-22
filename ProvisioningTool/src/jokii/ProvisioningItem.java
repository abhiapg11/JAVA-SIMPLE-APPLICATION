package jokii;

public class ProvisioningItem {
	public String email;
	public String otaPin;
	public String description;
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		} 
		if(!(obj instanceof ProvisioningItem)) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		ProvisioningItem testObject = (ProvisioningItem) obj;
		if(email.equals(testObject.email) 
		   && otaPin.equals(testObject.otaPin)
		   && description.equals(testObject.description)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int seed = 17;
		return seed * 31 + super.hashCode() + email.hashCode() + otaPin.hashCode() + description.hashCode();
	}
}
