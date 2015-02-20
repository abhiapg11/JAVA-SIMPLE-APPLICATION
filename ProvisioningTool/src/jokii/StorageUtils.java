package jokii;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

public class StorageUtils {

    public static boolean updateStorageData(ProvisioningData provisioningStorageData) {
        return updateXmlFile(provisioningStorageData, ProjectConst.DATA_FILE_NAME);
    }

    public static ProvisioningData readStorageData() {
        ProvisioningData provisioningData = new ProvisioningData();

        try {
            provisioningData = (ProvisioningData)new XStream().fromXML(new FileInputStream(ProjectConst.DATA_FILE_NAME));
        } catch (FileNotFoundException e) {
            System.out.println("No data to load - " + ProjectConst.DATA_FILE_NAME + " doesn't exist in current path.");
        } catch (Exception e) {
            System.out.println("Error on start application: " + e.toString());
        }

        return provisioningData;
    }

    public static boolean updateConfigData(ConfigItem configItem) {
        return updateXmlFile(configItem, ProjectConst.CONFIG_FILE_NAME);
    }

    public static ConfigItem readConfigData() {
        ConfigItem configItem = new ConfigItem();

        try {
            configItem = (ConfigItem)new XStream().fromXML(new FileInputStream(ProjectConst.CONFIG_FILE_NAME));
        } catch (FileNotFoundException e) {
            System.out.println("No data to load - " + ProjectConst.CONFIG_FILE_NAME + " doesn't exist in current path.");
        } catch (Exception e) {
            System.out.println("Error on start application: " + e.toString());
        }

        return configItem;
    }

    private static boolean updateXmlFile(Object objToXml, String fileName) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            new XStream().toXML(objToXml, fos);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return true;
    }
}
