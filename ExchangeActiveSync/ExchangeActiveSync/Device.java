//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;


// This class represents a "device" and is used to
// generate a DeviceInformation Xml element, as specified
// in [MS-ASCMD] section 2.2.3.45.
public class Device   
{
    static final String strSettingsXmlns = "settings";
    static final String strSettingsNamespace = "Settings";
    private String deviceID = null;
    private String deviceType = null;
    private String model = null;
    private String IMEINumber = null;
    private String friendlyName = null;
    private String operatingSystem = null;
    private String operatingSystemLanguage = null;
    private String phoneNumber = null;
    private String mobileOperator = null;
    private String userAgent = null;
    public String getDeviceID() throws Exception {
        return deviceID;
    }

    public void setDeviceID(String value) throws Exception {
        deviceID = value;
    }

    public String getDeviceType() throws Exception {
        return deviceType;
    }

    public void setDeviceType(String value) throws Exception {
        deviceType = value;
    }

    public String getModel() throws Exception {
        return model;
    }

    public void setModel(String value) throws Exception {
        model = value;
    }

    public String getIMEI() throws Exception {
        return IMEINumber;
    }

    public void setIMEI(String value) throws Exception {
        IMEINumber = value;
    }

    public String getFriendlyName() throws Exception {
        return friendlyName;
    }

    public void setFriendlyName(String value) throws Exception {
        friendlyName = value;
    }

    public String getOperatingSystem() throws Exception {
        return operatingSystem;
    }

    public void setOperatingSystem(String value) throws Exception {
        operatingSystem = value;
    }

    public String getOperatingSystemLanguage() throws Exception {
        return operatingSystemLanguage;
    }

    public void setOperatingSystemLanguage(String value) throws Exception {
        operatingSystemLanguage = value;
    }

    public String getPhoneNumber() throws Exception {
        return phoneNumber;
    }

    public void setPhoneNumber(String value) throws Exception {
        phoneNumber = value;
    }

    public String getMobileOperator() throws Exception {
        return mobileOperator;
    }

    public void setMobileOperator(String value) throws Exception {
        mobileOperator = value;
    }

    public String getUserAgent() throws Exception {
        return userAgent;
    }

    public void setUserAgent(String value) throws Exception {
        userAgent = value;
    }

    // This function generates and returns an XmlNode for the
    // DeviceInformation element.
    public XmlNode getDeviceInformationNode() throws Exception {
        XmlDocument xmlDoc = new XmlDocument();
        XmlElement deviceInfoElement = xmlDoc.CreateElement(strSettingsXmlns, "DeviceInformation", strSettingsNamespace);
        xmlDoc.AppendChild(deviceInfoElement);
        XmlElement setElement = xmlDoc.CreateElement(strSettingsXmlns, "Set", strSettingsNamespace);
        deviceInfoElement.AppendChild(setElement);
        if (getModel() != null)
        {
            XmlElement modelElement = xmlDoc.CreateElement(strSettingsXmlns, "Model", strSettingsNamespace);
            modelElement.InnerText = getModel();
            setElement.AppendChild(modelElement);
        }
         
        if (getIMEI() != null)
        {
            XmlElement IMEIElement = xmlDoc.CreateElement(strSettingsXmlns, "IMEI", strSettingsNamespace);
            IMEIElement.InnerText = getIMEI();
            setElement.AppendChild(IMEIElement);
        }
         
        if (getFriendlyName() != null)
        {
            XmlElement friendlyNameElement = xmlDoc.CreateElement(strSettingsXmlns, "FriendlyName", strSettingsNamespace);
            friendlyNameElement.InnerText = getFriendlyName();
            setElement.AppendChild(friendlyNameElement);
        }
         
        if (getOperatingSystem() != null)
        {
            XmlElement operatingSystemElement = xmlDoc.CreateElement(strSettingsXmlns, "OS", strSettingsNamespace);
            operatingSystemElement.InnerText = getOperatingSystem();
            setElement.AppendChild(operatingSystemElement);
        }
         
        if (getOperatingSystemLanguage() != null)
        {
            XmlElement operatingSystemLanguageElement = xmlDoc.CreateElement(strSettingsXmlns, "OSLanguage", strSettingsNamespace);
            operatingSystemLanguageElement.InnerText = getOperatingSystemLanguage();
            setElement.AppendChild(operatingSystemLanguageElement);
        }
         
        if (getPhoneNumber() != null)
        {
            XmlElement phoneNumberElement = xmlDoc.CreateElement(strSettingsXmlns, "PhoneNumber", strSettingsNamespace);
            phoneNumberElement.InnerText = getPhoneNumber();
            setElement.AppendChild(phoneNumberElement);
        }
         
        if (getMobileOperator() != null)
        {
            XmlElement mobileOperatorElement = xmlDoc.CreateElement(strSettingsXmlns, "MobileOperator", strSettingsNamespace);
            mobileOperatorElement.InnerText = getMobileOperator();
            setElement.AppendChild(mobileOperatorElement);
        }
         
        if (getUserAgent() != null)
        {
            XmlElement userAgentElement = xmlDoc.CreateElement(strSettingsXmlns, "UserAgent", strSettingsNamespace);
            userAgentElement.InnerText = getUserAgent();
            setElement.AppendChild(userAgentElement);
        }
         
        return xmlDoc.DocumentElement;
    }

}


