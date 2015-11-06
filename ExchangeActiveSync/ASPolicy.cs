using System;
using System.Collections.Generic;
using System.Xml;

namespace ExchangeActiveSync
{
    // This class represents an Exchange
    // ActiveSync policy.
    class ASPolicy
    {
        #region Enumerations
        public enum EncryptionAlgorithm
        {
            TripleDES = 0,
            DES = 1,
            RC2_128bit = 2,
            RC2_64bit = 3,
            RC2_40bit = 4
        }

        public enum SigningAlgorithm
        {
            SHA1 = 0,
            MD5 = 1
        }

        public enum CalendarAgeFilter
        {
            ALL = 0,
            TWO_WEEKS = 4,
            ONE_MONTH = 5,
            THREE_MONTHS = 6,
            SIX_MONTHS = 7
        }

        public enum MailAgeFilter
        {
            ALL = 0,
            ONE_DAY = 1,
            THREE_DAYS = 2,
            ONE_WEEK = 3,
            TWO_WEEKS = 4,
            ONE_MONTH = 5
        }

        public enum PolicyStatus
        {
            Success = 1,
            NoPolicyDefined = 2,
            PolicyTypeUnknown = 3,
            PolicyDataCorrupt = 4,
            PolicyKeyMismatch = 5
        }
        #endregion

        private Int32 status = 0;
        private UInt32 policyKey = 0;
        private byte allowBlueTooth = 0;
        private bool allowBrowser = false;
        private bool allowCamera = false;
        private bool allowConsumerEmail = false;
        private bool allowDesktopSync = false;
        private bool allowHTMLEmail = false;
        private bool allowInternetSharing = false;
        private bool allowIrDA = false;
        private bool allowPOPIMAPEmail = false;
        private bool allowRemoteDesktop = false;
        private bool allowSimpleDevicePassword = false;
        private Int32 allowSMIMEEncryptionAlgorithmNegotiation = 0;
        private bool allowSMIMESoftCerts = false;
        private bool allowStorageCard = false;
        private bool allowTextMessaging = false;
        private bool allowUnsignedApplications = false;
        private bool allowUnsignedInstallationPackages = false;
        private bool allowWifi = false;
        private bool alphanumericDevicePasswordRequired = false;
        private bool attachmentsEnabled = false;
        private bool devicePasswordEnabled = false;
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
        private bool passwordRecoveryEnabled = false;
        private bool requireDeviceEncryption = false;
        private bool requireEncryptedSMIMEMessages = false;
        private Int32 requireEncryptionSMIMEAlgorithm = 0;
        private bool requireManualSyncWhenRoaming = false;
        private Int32 requireSignedSMIMEAlgorithm = 0;
        private bool requireSignedSMIMEMessages = false;
        private bool requireStorageCardEncryption = false;

        private string[] approvedApplicationList = null;
        private string[] unapprovedInROMApplicationList = null;

        private bool remoteWipeRequested = false;
        private bool hasPolicyInfo = false;


        #region Property Accessors
        public Int32 Status
        {
            get
            {
                return status;
            }
        }

        public UInt32 PolicyKey
        {
            get
            {
                return policyKey;
            }
        }

        public byte AllowBlueTooth 
        {
            get
            {
                return allowBlueTooth;
            }
        }

        public bool AllowBrowser
        {
            get 
            {
                return allowBrowser;
            }
        }

        public bool AllowCamera
        {
            get
            {
                return allowCamera;
            }
        }

        public bool AllowConsumerEmail
        {
            get
            {
                return allowConsumerEmail;
            }
        }

        public bool AllowDesktopSync
        {
            get
            {
                return allowDesktopSync;
            }
        }

        public bool AllowHTMLEmail
        {
            get
            {
                return allowHTMLEmail;
            }
        }

        public bool AllowInternetSharing
        {
            get
            {
                return allowInternetSharing;
            }
        }

        public bool AllowIrDA
        {
            get
            {
                return allowIrDA;
            }
        }

        public bool AllowPOPIMAPEmail
        {
            get
            {
                return allowPOPIMAPEmail;
            }
        }

        public bool AllowRemoteDesktop
        {
            get
            {
                return allowRemoteDesktop;
            }
        }

        public bool AllowSimpleDevicePassword
        {
            get
            {
                return allowSimpleDevicePassword;
            }
        }

        public Int32 AllowSMIMEEncryptionAlgorithmNegotiation
        {
            get
            {
                return allowSMIMEEncryptionAlgorithmNegotiation;
            }
        }

        public bool AllowSMIMESoftCerts
        {
            get
            {
                return allowSMIMESoftCerts;
            }
        }

        public bool AllowStorageCard
        {
            get
            {
                return allowStorageCard;
            }
        }

        public bool AllowTextMessaging
        {
            get
            {
                return allowTextMessaging;
            }
        }

        public bool AllowUnsignedApplications
        {
            get
            {
                return allowUnsignedApplications;
            }
        }

        public bool AllowUnsignedInstallationPackages
        {
            get
            {
                return allowUnsignedInstallationPackages;
            }
        }

        public bool AllowWifi
        {
            get
            {
                return allowWifi;
            }
        }

        public bool AlphanumericDevicePasswordRequired
        {
            get
            {
                return alphanumericDevicePasswordRequired;
            }
        }

        public bool AttachmentsEnabled
        {
            get
            {
                return attachmentsEnabled;
            }
        }

        public bool DevicePasswordEnabled
        {
            get
            {
                return devicePasswordEnabled;
            }
        }

        public UInt32 DevicePasswordExpiration
        {
            get
            {
                return devicePasswordExpiration;
            }
        }

        public UInt32 DevicePasswordHistory
        {
            get
            {
                return devicePasswordHistory;
            }
        }

        public UInt32 MaxAttachmentSize
        {
            get
            {
                return maxAttachmentSize;
            }
        }

        public UInt32 MaxCalendarAgeFilter
        {
            get
            {
                return maxCalendarAgeFilter;
            }
        }

        public UInt32 MaxDevicePasswordFailedAttempts
        {
            get
            {
                return maxDevicePasswordFailedAttempts;
            }
        }

        public UInt32 MaxEmailAgeFilter
        {
            get
            {
                return maxEmailAgeFilter;
            }
        }

        public Int32 MaxEmailBodyTruncationSize
        {
            get
            {
                return maxEmailBodyTruncationSize;
            }
        }

        public Int32 MaxEmailHTMLBodyTruncationSize
        {
            get
            {
                return maxEmailHTMLBodyTruncationSize;
            }
        }

        public UInt32 MaxInactivityTimeDeviceLock
        {
            get
            {
                return maxInactivityTimeDeviceLock;
            }
        }

        public byte MinDevicePasswordComplexCharacters
        {
            get
            {
                return minDevicePasswordComplexCharacters;
            }
        }

        public byte MinDevicePasswordLength
        {
            get
            {
                return minDevicePasswordLength;
            }
        }

        public bool PasswordRecoveryEnabled
        {
            get
            {
                return passwordRecoveryEnabled;
            }
        }

        public bool RequireDeviceEncryption
        {
            get
            {
                return requireDeviceEncryption;
            }
        }

        public bool RequireEncryptedSMIMEMessages
        {
            get
            {
                return requireEncryptedSMIMEMessages;
            }
        }

        public Int32 RequireEncryptionSMIMEAlgorithm
        {
            get
            {
                return requireEncryptionSMIMEAlgorithm;
            }
        }

        public bool RequireManualSyncWhenRoaming
        {
            get
            {
                return requireManualSyncWhenRoaming;
            }
        }

        public Int32 RequireSignedSMIMEAlgorithm
        {
            get
            {
                return requireSignedSMIMEAlgorithm;
            }
        }

        public bool RequireSignedSMIMEMessages
        {
            get
            {
                return requireSignedSMIMEMessages;
            }
        }

        public bool RequireStorageCardEncryption
        {
            get
            {
                return requireStorageCardEncryption;
            }
        }

        public string[] ApprovedApplicationList
        {
            get
            {
                return approvedApplicationList;
            }
        }

        public string[] UnapprovedInROMApplicationList
        {
            get
            {
                return unapprovedInROMApplicationList;
            }
        }

        public bool RemoteWipeRequested
        {
            get
            {
                return remoteWipeRequested;
            }
        }

        public bool HasPolicyInfo
        {
            get
            {
                return hasPolicyInfo;
            }
        }

        #endregion 
        
        // This function parses a Provision command
        // response (as specified in MS-ASPROV section 2.2)
        // and extracts the policy information.
        public bool LoadXML(string policyXml)
        {
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
                    if (policyTypeNode != null && policyTypeNode.InnerText == "MS-EAS-Provisioning-WBXML")
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

                            foreach (XmlNode policySettingNode in provisioningDocNode.ChildNodes)
                            {
                                // Loop through the child nodes and
                                // set the corresponding property.
                                switch (policySettingNode.LocalName)
                                {
                                    case ("AllowBluetooth"):
                                        if (policySettingNode.InnerText != "")
                                            allowBlueTooth = XmlConvert.ToByte(policySettingNode.InnerText);
                                        break;
                                    case ("AllowBrowser"):
                                        if (policySettingNode.InnerText != "")
                                            allowBrowser = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowCamera"):
                                        if (policySettingNode.InnerText != "")
                                            allowCamera = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowConsumerEmail"):
                                        if (policySettingNode.InnerText != "")
                                            allowConsumerEmail = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowDesktopSync"):
                                        if (policySettingNode.InnerText != "")
                                            allowDesktopSync = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowHTMLEmail"):
                                        if (policySettingNode.InnerText != "")
                                            allowHTMLEmail = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowInternetSharing"):
                                        if (policySettingNode.InnerText != "")
                                            allowInternetSharing = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowIrDA"):
                                        if (policySettingNode.InnerText != "")
                                            allowIrDA = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowPOPIMAPEmail"):
                                        if (policySettingNode.InnerText != "")
                                            allowPOPIMAPEmail = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowRemoteDesktop"):
                                        if (policySettingNode.InnerText != "")
                                            allowRemoteDesktop = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowSimpleDevicePassword"):
                                        if (policySettingNode.InnerText != "")
                                            allowSimpleDevicePassword = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowSMIMEEncryptionAlgorithmNegotiation"):
                                        if (policySettingNode.InnerText != "")
                                            allowSMIMEEncryptionAlgorithmNegotiation = XmlConvert.ToInt32(policySettingNode.InnerText);
                                        break;
                                    case ("AllowSMIMESoftCerts"):
                                        if (policySettingNode.InnerText != "")
                                            allowSMIMESoftCerts = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowStorageCard"):
                                        if (policySettingNode.InnerText != "")
                                            allowStorageCard = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowTextMessaging"):
                                        if (policySettingNode.InnerText != "")
                                            allowTextMessaging = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowUnsignedApplications"):
                                        if (policySettingNode.InnerText != "")
                                            allowUnsignedApplications = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowUnsignedInstallationPackages"):
                                        if (policySettingNode.InnerText != "")
                                            allowUnsignedInstallationPackages = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AllowWiFi"):
                                        if (policySettingNode.InnerText != "")
                                            allowWifi = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("AlphanumericDevicePasswordRequired"):
                                        if (policySettingNode.InnerText != "")
                                            alphanumericDevicePasswordRequired = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("ApprovedApplicationList"):
                                        if (policySettingNode.InnerText != "")
                                            approvedApplicationList = ParseAppList(policySettingNode);
                                        break;
                                    case ("AttachmentsEnabled"):
                                        if (policySettingNode.InnerText != "")
                                            attachmentsEnabled = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("DevicePasswordEnabled"):
                                        if (policySettingNode.InnerText != "")
                                            devicePasswordEnabled = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("DevicePasswordExpiration"):
                                        if (policySettingNode.InnerText != "")
                                            devicePasswordExpiration = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                        break;
                                    case ("DevicePasswordHistory"):
                                        if (policySettingNode.InnerText != "")
                                            devicePasswordHistory = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MaxAttachmentSize"):
                                        if (policySettingNode.InnerText != "")
                                            maxAttachmentSize = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MaxCalendarAgeFilter"):
                                        if (policySettingNode.InnerText != "")
                                            maxCalendarAgeFilter = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MaxDevicePasswordFailedAttempts"):
                                        if (policySettingNode.InnerText != "")
                                            maxDevicePasswordFailedAttempts = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MaxEmailAgeFilter"):
                                        if (policySettingNode.InnerText != "")
                                            maxEmailAgeFilter = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MaxEmailBodyTruncationSize"):
                                        if (policySettingNode.InnerText != "")
                                            maxEmailBodyTruncationSize = XmlConvert.ToInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MaxEmailHTMLBodyTruncationSize"):
                                        if (policySettingNode.InnerText != "")
                                            maxEmailHTMLBodyTruncationSize = XmlConvert.ToInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MaxInactivityTimeDeviceLock"):
                                        if (policySettingNode.InnerText != "")
                                            maxInactivityTimeDeviceLock = XmlConvert.ToUInt32(policySettingNode.InnerText);
                                        break;
                                    case ("MinDevicePasswordComplexCharacters"):
                                        if (policySettingNode.InnerText != "")
                                            minDevicePasswordComplexCharacters = XmlConvert.ToByte(policySettingNode.InnerText);
                                        break;
                                    case ("MinDevicePasswordLength"):
                                        if (policySettingNode.InnerText != "")
                                            minDevicePasswordLength = XmlConvert.ToByte(policySettingNode.InnerText);
                                        break;
                                    case ("PasswordRecoveryEnabled"):
                                        if (policySettingNode.InnerText != "")
                                            passwordRecoveryEnabled = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("RequireDeviceEncryption"):
                                        if (policySettingNode.InnerText != "")
                                            requireDeviceEncryption = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("RequireEncryptedSMIMEMessages"):
                                        if (policySettingNode.InnerText != "")
                                            requireEncryptedSMIMEMessages = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("RequireEncryptionSMIMEAlgorithm"):
                                        if (policySettingNode.InnerText != "")
                                            requireEncryptionSMIMEAlgorithm = XmlConvert.ToInt32(policySettingNode.InnerText);
                                        break;
                                    case ("RequireManualSyncWhenRoaming"):
                                        if (policySettingNode.InnerText != "")
                                            requireManualSyncWhenRoaming = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("RequireSignedSMIMEAlgorithm"):
                                        if (policySettingNode.InnerText != "")
                                            requireSignedSMIMEAlgorithm = XmlConvert.ToInt32(policySettingNode.InnerText);
                                        break;
                                    case ("RequireSignedSMIMEMessages"):
                                        if (policySettingNode.InnerText != "")
                                            requireSignedSMIMEMessages = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("RequireStorageCardEncryption"):
                                        if (policySettingNode.InnerText != "")
                                            requireStorageCardEncryption = XmlConvert.ToBoolean(policySettingNode.InnerText);
                                        break;
                                    case ("UnapprovedInROMApplicationList"):
                                        if (policySettingNode.InnerText != "")
                                            unapprovedInROMApplicationList = ParseAppList(policySettingNode);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception)
            {
                return false;
            }

            return true;
        }

        // This function parses the contents of the 
        // ApprovedApplicationList and the UnapprovedInROMApplicationList
        // nodes.
        private string[] ParseAppList(XmlNode appListNode)
        {
            List<string> appList = new List<string>();

            foreach (XmlNode appNode in appListNode.ChildNodes)
            {
                appList.Add(appNode.InnerText);
            }

            return appList.ToArray();
        }
    }
}
