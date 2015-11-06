using System.Xml;

namespace ExchangeActiveSync
{
    // This class represents a "device" and is used to
    // generate a DeviceInformation Xml element, as specified
    // in [MS-ASCMD] section 2.2.3.45.
    class Device
    {
        const string strSettingsXmlns = "settings";
        const string strSettingsNamespace = "Settings";

        private string deviceID = null;
        private string deviceType = null;
        private string model = null;
        private string IMEINumber = null;
        private string friendlyName = null;
        private string operatingSystem = null;
        private string operatingSystemLanguage = null;
        private string phoneNumber = null;
        private string mobileOperator = null;
        private string userAgent = null;

        #region Property Accessors
        public string DeviceID
        {
            get
            {
                return deviceID;
            }
            set
            {
                deviceID = value;
            }
        }

        public string DeviceType
        {
            get
            {
                return deviceType;
            }
            set
            {
                deviceType = value;
            }
        }

        public string Model
        {
            get
            {
                return model;
            }
            set
            {
                model = value;
            }
        }

        public string IMEI
        {
            get
            {
                return IMEINumber;
            }
            set
            {
                IMEINumber = value;
            }
        }

        public string FriendlyName
        {
            get
            {
                return friendlyName;
            }
            set
            {
                friendlyName = value;
            }
        }

        public string OperatingSystem
        {
            get
            {
                return operatingSystem;
            }
            set
            {
                operatingSystem = value;
            }
        }

        public string OperatingSystemLanguage
        {
            get
            {
                return operatingSystemLanguage;
            }
            set
            {
                operatingSystemLanguage = value;
            }
        }

        public string PhoneNumber
        {
            get
            {
                return phoneNumber;
            }
            set
            {
                phoneNumber = value;
            }
        }

        public string MobileOperator
        {
            get
            {
                return mobileOperator;
            }
            set
            {
                mobileOperator = value;
            }
        }

        public string UserAgent
        {
            get
            {
                return userAgent;
            }
            set
            {
                userAgent = value;
            }
        }
        #endregion

        // This function generates and returns an XmlNode for the
        // DeviceInformation element.
        public XmlNode GetDeviceInformationNode()
        {
            XmlDocument xmlDoc = new XmlDocument();

            XmlElement deviceInfoElement = xmlDoc.CreateElement(strSettingsXmlns, "DeviceInformation", strSettingsNamespace);
            xmlDoc.AppendChild(deviceInfoElement);

            XmlElement setElement = xmlDoc.CreateElement(strSettingsXmlns, "Set", strSettingsNamespace);
            deviceInfoElement.AppendChild(setElement);

            if (Model != null)
            {
                XmlElement modelElement = xmlDoc.CreateElement(strSettingsXmlns, "Model", strSettingsNamespace);
                modelElement.InnerText = Model;
                setElement.AppendChild(modelElement);
            }

            if (IMEI != null)
            {
                XmlElement IMEIElement = xmlDoc.CreateElement(strSettingsXmlns, "IMEI", strSettingsNamespace);
                IMEIElement.InnerText = IMEI;
                setElement.AppendChild(IMEIElement);
            }

            if (FriendlyName != null)
            {
                XmlElement friendlyNameElement = xmlDoc.CreateElement(strSettingsXmlns, "FriendlyName", strSettingsNamespace);
                friendlyNameElement.InnerText = FriendlyName;
                setElement.AppendChild(friendlyNameElement);
            }

            if (OperatingSystem != null)
            {
                XmlElement operatingSystemElement = xmlDoc.CreateElement(strSettingsXmlns, "OS", strSettingsNamespace);
                operatingSystemElement.InnerText = OperatingSystem;
                setElement.AppendChild(operatingSystemElement);
            }

            if (OperatingSystemLanguage != null)
            {
                XmlElement operatingSystemLanguageElement = xmlDoc.CreateElement(strSettingsXmlns, "OSLanguage", strSettingsNamespace);
                operatingSystemLanguageElement.InnerText = OperatingSystemLanguage;
                setElement.AppendChild(operatingSystemLanguageElement);
            }

            if (PhoneNumber != null)
            {
                XmlElement phoneNumberElement = xmlDoc.CreateElement(strSettingsXmlns, "PhoneNumber", strSettingsNamespace);
                phoneNumberElement.InnerText = PhoneNumber;
                setElement.AppendChild(phoneNumberElement);
            }

            if (MobileOperator != null)
            {
                XmlElement mobileOperatorElement = xmlDoc.CreateElement(strSettingsXmlns, "MobileOperator", strSettingsNamespace);
                mobileOperatorElement.InnerText = MobileOperator;
                setElement.AppendChild(mobileOperatorElement);
            }

            if (UserAgent != null)
            {
                XmlElement userAgentElement = xmlDoc.CreateElement(strSettingsXmlns, "UserAgent", strSettingsNamespace);
                userAgentElement.InnerText = UserAgent;
                setElement.AppendChild(userAgentElement);
            }

            return xmlDoc.DocumentElement;
        }
    }
}
