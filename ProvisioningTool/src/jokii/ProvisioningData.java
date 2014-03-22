package jokii;

import java.util.ArrayList;
import java.util.List;

public class ProvisioningData {

    private List<ProvisioningItem> mList;

    public ProvisioningData(){
        mList = new ArrayList<ProvisioningItem>();
    }

    public void add(ProvisioningItem p){
        mList.add(p);
    }
    
    public List<ProvisioningItem> getList() {
    	return mList;
    }
}
