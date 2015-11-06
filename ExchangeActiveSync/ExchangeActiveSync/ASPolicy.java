//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import CS2JNet.System.StringSupport;

// This class represents an Exchange
// ActiveSync policy.
public class ASPolicy   
{
    public enum EncryptionAlgorithm
    {
        TripleDES,
        DES,
        RC2_128bit,
        RC2_64bit,
        RC2_40bit
    }
    public enum SigningAlgorithm
    {
        SHA1,
        MD5
    }
    public enum CalendarAgeFilter
    {
        ALL,
        __dummyEnum__0,
        __dummyEnum__1,
        __dummyEnum__2,
        TWO_WEEKS,
        ONE_MONTH,
        THREE_MONTHS,
        SIX_MONTHS
    }
    public enum MailAgeFilter
    {
        ALL,
        ONE_DAY,
        THREE_DAYS,
        ONE_WEEK,
        TWO_WEEKS,
        ONE_MONTH
    }
    public enum PolicyStatus
    {
        __dummyEnum__0,
        Success,
        NoPolicyDefined,
        PolicyTypeUnknown,
        PolicyDataCorrupt,
        PolicyKeyMismatch
    }
    private Int32 status = 0;
    private UInt32 policyKey = 0;
    private byte allowBlueTooth = 0;
    private boolean allowBrowser = false;
    private boolean allowCamera = false;
    private boolean allowConsumerEmail = false;
    private boolean allowDesktopSync = false;
    private boolean allowHTMLEmail = false;
    private boolean allowInternetSharing = false;
    private boolean allowIrDA = false;
    private boolean allowPOPIMAPEmail = false;
    private boolean allowRemoteDesktop = false;
    private boolean allowSimpleDevicePassword = false;
    private Int32 allowSMIMEEncryptionAlgorithmNegotiation = 0;
    private boolean allowSMIMESoftCerts = false;
    private boolean allowStorageCard = false;
    private boolean allowTextMessaging = false;
    private boolean allowUnsignedApplications = false;
    private boolean allowUnsignedInstallationPackages = false;
    private boolean allowWifi = false;
    private boolean alphanumericDevicePasswordRequired = false;
    private boolean attachmentsEnabled = false;
    private boolean devicePasswordEnabled = false;
    private UInt32 devicePasswordExpiration = 0;
    private UInt32 devicePasswordHistory = 0;
    private UInt32 maxAttachmentSize = 0;
    private UInt32 maxCalendarAgeFilter = 0;
    private UInt32 maxDevicePasswordFailedAttempts = 0;
    private UInt32 maxEmailAgeFilter = 0;
    private Int32 maxEmailBodyTruncationSize = -1;
    private Int32 maxEmailHTMLBodyTruncationSize = -1;
    private UInt32 maxInactivityTimeDeviceLock = 0;
    private byte minDevicePasswordComplexCharacters = 1;
    private byte minDevicePasswordLength = 1;
    private boolean passwordRecoveryEnabled = false;
    private boolean requireDeviceEncryption = false;
    private boolean requireEncryptedSMIMEMessages = false;
    private Int32 requireEncryptionSMIMEAlgorithm = 0;
    private boolean requireManualSyncWhenRoaming = false;
    private Int32 requireSignedSMIMEAlgorithm = 0;
    private boolean requireSignedSMIMEMessages = false;
    private boolean requireStorageCardEncryption = false;
    private String[] approvedApplicationList = null;
    private String[] unapprovedInROMApplicationList = null;
    private boolean remoteWipeRequested = false;
    private boolean hasPolicyInfo = false;
    public Int32 getStatus() throws Exception {
        return status;
    }

    public UInt32 getPolicyKey() throws Exception {
        return policyKey;
    }

    public byte getAllowBlueTooth() throws Exception {
        return allowBlueTooth;
    }

    public boolean getAllowBrowser() throws Exception {
        return allowBrowser;
    }

    public boolean getAllowCamera() throws Exception {
        return allowCamera;
    }

    public boolean getAllowConsumerEmail() throws Exception {
        return allowConsumerEmail;
    }

    public boolean getAllowDesktopSync() throws Exception {
        return allowDesktopSync;
    }

    public boolean getAllowHTMLEmail() throws Exception {
        return allowHTMLEmail;
    }

    public boolean getAllowInternetSharing() throws Exception {
        return allowInternetSharing;
    }

    public boolean getAllowIrDA() throws Exception {
        return allowIrDA;
    }

    public boolean getAllowPOPIMAPEmail() throws Exception {
        return allowPOPIMAPEmail;
    }

    public boolean getAllowRemoteDesktop() throws Exception {
        return allowRemoteDesktop;
    }

    public boolean getAllowSimpleDevicePassword() throws Exception {
        return allowSimpleDevicePassword;
    }

    public Int32 getAllowSMIMEEncryptionAlgorithmNegotiation() throws Exception {
        return allowSMIMEEncryptionAlgorithmNegotiation;
    }

    public boolean getAllowSMIMESoftCerts() throws Exception {
        return allowSMIMESoftCerts;
    }

    public boolean getAllowStorageCard() throws Exception {
        return allowStorageCard;
    }

    public boolean getAllowTextMessaging() throws Exception {
        return allowTextMessaging;
    }

    public boolean getAllowUnsignedApplications() throws Exception {
        return allowUnsignedApplications;
    }

    public boolean getAllowUnsignedInstallationPackages() throws Exception {
        return allowUnsignedInstallationPackages;
    }

    public boolean getAllowWifi() throws Exception {
        return allowWifi;
    }

    public boolean getAlphanumericDevicePasswordRequired() throws Exception {
        return alphanumericDevicePasswordRequired;
    }

    public boolean getAttachmentsEnabled() throws Exception {
        return attachmentsEnabled;
    }

    public boolean getDevicePasswordEnabled() throws Exception {
        return devicePasswordEnabled;
    }

    public UInt32 getDevicePasswordExpiration() throws Exception {
        return devicePasswordExpiration;
    }

    public UInt32 getDevicePasswordHistory() throws Exception {
        return devicePasswordHistory;
    }

    public UInt32 getMaxAttachmentSize() throws Exception {
        return maxAttachmentSize;
    }

    public UInt32 getMaxCalendarAgeFilter() throws Exception {
        return maxCalendarAgeFilter;
    }

    public UInt32 getMaxDevicePasswordFailedAttempts() throws Exception {
        return maxDevicePasswordFailedAttempts;
    }

    public UInt32 getMaxEmailAgeFilter() throws Exception {
        return maxEmailAgeFilter;
    }

    public Int32 getMaxEmailBodyTruncationSize() throws Exception {
        return maxEmailBodyTruncationSize;
    }

    public Int32 getMaxEmailHTMLBodyTruncationSize() throws Exception {
        return maxEmailHTMLBodyTruncationSize;
    }

    public UInt32 getMaxInactivityTimeDeviceLock() throws Exception {
        return maxInactivityTimeDeviceLock;
    }

    public byte getMinDevicePasswordComplexCharacters() throws Exception {
        return minDevicePasswordComplexCharacters;
    }

    public byte getMinDevicePasswordLength() throws Exception {
        return minDevicePasswordLength;
    }

    public boolean getPasswordRecoveryEnabled() throws Exception {
        return passwordRecoveryEnabled;
    }

    public boolean getRequireDeviceEncryption() throws Exception {
        return requireDeviceEncryption;
    }

    public boolean getRequireEncryptedSMIMEMessages() throws Exception {
        return requireEncryptedSMIMEMessages;
    }

    public Int32 getRequireEncryptionSMIMEAlgorithm() throws Exception {
        return requireEncryptionSMIMEAlgorithm;
    }

    public boolean getRequireManualSyncWhenRoaming() throws Exception {
        return requireManualSyncWhenRoaming;
    }

    public Int32 getRequireSignedSMIMEAlgorithm() throws Exception {
        return requireSignedSMIMEAlgorithm;
    }

    public boolean getRequireSignedSMIMEMessages() throws Exception {
        return requireSignedSMIMEMessages;
    }

    public boolean getRequireStorageCardEncryption() throws Exception {
        return requireStorageCardEncryption;
    }

    public String[] getApprovedApplicationList() throws Exception {
        return approvedApplicationList;
    }

    public String[] getUnapprovedInROMApplicationList() throws Exception {
        return unapprovedInROMApplicationList;
    }

    public boolean getRemoteWipeRequested() throws Exception {
        return remoteWipeRequested;
    }

    public boolean getHasPolicyInfo() throws Exception {
        return hasPolicyInfo;
    }

    // This function parses a Provision command
    // response (as specified in MS-ASPROV section 2.2)
    // and extracts the policy information.
    public boolean loadXML(String policyXml) throws Exception {
        XmlDocument xmlDoc = new XmlDocument();
        try
        {
            xmlDoc.LoadXml(policyXml);
            XmlNamespaceManager xmlNsMgr = new XmlNamespaceManager(xmlDoc.NameTable);
            xmlNsMgr.AddNamespace("provision", "Provision");
            // If this is a remote wipe, there's no
            // further parsing to do.
            XmlNode remoteWipeNode = xmlDoc.SelectSingleNode(".//provision:RemoteWipe", xmlNsMgr);
            if (remoteWipeNode != null)
            {
                remoteWipeRequested = true;
                return true;
            }
             
            // Find the policy.
            XmlNode policyNode = xmlDoc.SelectSingleNode(".//provision:Policy", xmlNsMgr);
            if (policyNode != null)
            {
                XmlNode policyTypeNode = policyNode.SelectSingleNode("provision:PolicyType", xmlNsMgr);
                if (policyTypeNode != null && StringSupport.equals(policyTypeNode.InnerText, "MS-EAS-Provisioning-WBXML"))
                {
                    // Get the policy's status
                    XmlNode policyStatusNode = policyNode.SelectSingleNode("provision:Status", xmlNsMgr);
                    if (policyStatusNode != null)
                        status = XmlConvert.ToInt32(policyStatusNode.InnerText);
                     
                    // Get the policy key
                    XmlNode policyKeyNode = policyNode.SelectSingleNode("provision:PolicyKey", xmlNsMgr);
                    if (policyKeyNode != null)
                        policyKey = XmlConvert.ToUInt32(policyKeyNode.InnerText);
                     
                    // Get the contents of the policy
                    XmlNode provisioningDocNode = policyNode.SelectSingleNode(".//provision:EASProvisionDoc", xmlNsMgr);
                    if (provisioningDocNode != null)
                    {
                        hasPolicyInfo = true;
                        for (Object __dummyForeachVar0 : provisioningDocNode.ChildNodes)
                        {
                            XmlNode policySettingNode = (XmlNode)__dummyForeachVar0;
                            // Loop through the child nodes and
                            // set the corresponding property.
                            LocalName __dummyScrutVar0 = policySettingNode.LocalName;
                            if (__dummyScrutVar0.equals(("AllowBluetooth")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowBlueTooth = XmlConvert.ToByte(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowBrowser")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowBrowser = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowCamera")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowCamera = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowConsumerEmail")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowConsumerEmail = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowDesktopSync")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowDesktopSync = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowHTMLEmail")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowHTMLEmail = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowInternetSharing")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowInternetSharing = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowIrDA")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowIrDA = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowPOPIMAPEmail")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowPOPIMAPEmail = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowRemoteDesktop")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowRemoteDesktop = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowSimpleDevicePassword")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowSimpleDevicePassword = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowSMIMEEncryptionAlgorithmNegotiation")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowSMIMEEncryptionAlgorithmNegotiation = XmlConvert.ToInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowSMIMESoftCerts")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowSMIMESoftCerts = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowStorageCard")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowStorageCard = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowTextMessaging")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowTextMessaging = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowUnsignedApplications")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowUnsignedApplications = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowUnsignedInstallationPackages")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowUnsignedInstallationPackages = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AllowWiFi")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    allowWifi = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AlphanumericDevicePasswordRequired")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    alphanumericDevicePasswordRequired = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("ApprovedApplicationList")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    approvedApplicationList = parseAppList(policySettingNode);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("AttachmentsEnabled")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    attachmentsEnabled = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("DevicePasswordEnabled")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    devicePasswordEnabled = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("DevicePasswordExpiration")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    devicePasswordExpiration = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("DevicePasswordHistory")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    devicePasswordHistory = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MaxAttachmentSize")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    maxAttachmentSize = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MaxCalendarAgeFilter")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    maxCalendarAgeFilter = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MaxDevicePasswordFailedAttempts")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    maxDevicePasswordFailedAttempts = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MaxEmailAgeFilter")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    maxEmailAgeFilter = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MaxEmailBodyTruncationSize")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    maxEmailBodyTruncationSize = XmlConvert.ToInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MaxEmailHTMLBodyTruncationSize")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    maxEmailHTMLBodyTruncationSize = XmlConvert.ToInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MaxInactivityTimeDeviceLock")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    maxInactivityTimeDeviceLock = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MinDevicePasswordComplexCharacters")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    minDevicePasswordComplexCharacters = XmlConvert.ToByte(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("MinDevicePasswordLength")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    minDevicePasswordLength = XmlConvert.ToByte(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("PasswordRecoveryEnabled")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    passwordRecoveryEnabled = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("RequireDeviceEncryption")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    requireDeviceEncryption = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("RequireEncryptedSMIMEMessages")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    requireEncryptedSMIMEMessages = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("RequireEncryptionSMIMEAlgorithm")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    requireEncryptionSMIMEAlgorithm = XmlConvert.ToInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("RequireManualSyncWhenRoaming")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    requireManualSyncWhenRoaming = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("RequireSignedSMIMEAlgorithm")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    requireSignedSMIMEAlgorithm = XmlConvert.ToInt32(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("RequireSignedSMIMEMessages")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    requireSignedSMIMEMessages = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("RequireStorageCardEncryption")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    requireStorageCardEncryption = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                 
                            }
                            else if (__dummyScrutVar0.equals(("UnapprovedInROMApplicationList")))
                            {
                                if (!StringSupport.equals(policySettingNode.InnerText, ""))
                                    unapprovedInROMApplicationList = parseAppList(policySettingNode);
                                 
                            }
                            else
                            {
                            }                                          
                        }
                    }
                     
                }
                 
            }
             
        }
        catch (Exception __dummyCatchVar0)
        {
            return false;
        }

        return true;
    }

    // This function parses the contents of the
    // ApprovedApplicationList and the UnapprovedInROMApplicationList
    // nodes.
    private String[] parseAppList(XmlNode appListNode) throws Exception {
        List<String> appList = new List<String>();
        for (Object __dummyForeachVar1 : appListNode.ChildNodes)
        {
            XmlNode appNode = (XmlNode)__dummyForeachVar1;
            appList.Add(appNode.InnerText);
        }
        return appList.ToArray();
    }

}


