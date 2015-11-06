using System;
using System.Collections.Generic;
using System.Text;

namespace ExchangeActiveSync
{
    // This struct represents an encoded parameter
    // as specified in [MS-ASHTTP] section 2.2.1.1.1.1.1.
    struct EncodedParameter
    {
        public byte tag;
        public byte length;
        public string value;
    }

    // This class represents a base64-encoded query value
    // as specified in [MS-ASHTTP] section 2.2.1.1.1.1.
    class EncodedRequest
    {
        private byte protocolVersion = 0;
        private byte commandCode = 0;
        private Int32 locale = 0;
        private byte deviceIdLength = 0;
        private string deviceId = "";
        private byte policyKeyLength = 0;
        private UInt32 policyKey = 0;
        private byte deviceTypeLength = 0;
        private string deviceType = "";
        private EncodedParameter[] commandParameters = null;

        private Dictionary<string,byte> commandDictionary = new Dictionary<string,byte>();
        private Dictionary<string,byte> parameterDictionary = new Dictionary<string,byte>();
        private Dictionary<string, Int32> localeDictionary = new Dictionary<string, Int32>();

        public EncodedRequest()
        {
            // Load command dictionary
            // Values taken from [MS-ASHTTP] section 2.2.1.1.1.1.2
            commandDictionary.Add("SYNC", 0);
            commandDictionary.Add("SENDMAIL", 1);
            commandDictionary.Add("SMARTFORWARD", 2);
            commandDictionary.Add("SMARTREPLY", 3);
            commandDictionary.Add("GETATTACHMENT", 4);
            commandDictionary.Add("FOLDERSYNC", 9);
            commandDictionary.Add("FOLDERCREATE", 10);
            commandDictionary.Add("FOLDERDELETE", 11);
            commandDictionary.Add("FOLDERUPDATE", 12);
            commandDictionary.Add("MOVEITEMS", 13);
            commandDictionary.Add("GETITEMESTIMATE", 14);
            commandDictionary.Add("MEETINGRESPONSE", 15);
            commandDictionary.Add("SEARCH", 16);
            commandDictionary.Add("SETTINGS", 17);
            commandDictionary.Add("PING", 18);
            commandDictionary.Add("ITEMOPERATIONS", 19);
            commandDictionary.Add("PROVISION", 20);
            commandDictionary.Add("RESOLVERECIPIENTS", 21);
            commandDictionary.Add("VALIDATECERT", 22);

            // Load parameter dictionary
            // Values taken from [MS-ASHTTP] section 2.2.1.1.1.1.3
            parameterDictionary.Add("ATTACHMENTNAME", 0);
            parameterDictionary.Add("ITEMID", 3);
            parameterDictionary.Add("LONGID", 4);
            parameterDictionary.Add("OCCURRENCE", 6);
            parameterDictionary.Add("OPTIONS", 7);
            parameterDictionary.Add("USER", 8);

            // Load locale dictionary
            // TODO: Add other locales
            localeDictionary.Add("EN-US", 0x0409);
        }

        #region Property Accessors
        public byte ProtocolVersion
        {
            get
            {
                return protocolVersion;
            }
            set
            {
                protocolVersion = value;
            }
        }

        public byte CommandCode
        {
            get
            {
                return commandCode;
            }
        }

        public bool SetCommandCode(string strCommand)
        {
            return commandDictionary.TryGetValue(strCommand.ToUpper(), out commandCode);
        }

        public Int32 Locale
        {
            get
            {
                return locale;
            }
        }

        public bool SetLocale(string strLocale)
        {
            return localeDictionary.TryGetValue(strLocale.ToUpper(), out locale);
        }

        public Int32 DeviceIdLength
        {
            get
            {
                return Convert.ToInt32(deviceIdLength);
            }
        }

        public string DeviceId
        {
            get
            {
                return deviceId;
            }
            set
            {
                deviceId = value;
                deviceIdLength = Convert.ToByte(deviceId.Length);
            }
        }

        public Int32 PolicyKeyLength
        {
            get
            {
                return Convert.ToInt32(policyKeyLength);
            }
        }

        public UInt32 PolicyKey
        {
            get
            {
                return policyKey;
            }
            set
            {
                policyKey = value;
                policyKeyLength = Convert.ToByte(sizeof(Int32));
            }
        }

        public Int32 DeviceTypeLength
        {
            get
            {
                return Convert.ToInt32(deviceTypeLength);
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
                deviceTypeLength = Convert.ToByte(deviceType.Length);
            }
        }

        public EncodedParameter[] CommandParameters
        {
            get
            {
                return commandParameters;
            }
        }
        #endregion

        // This function returns the base64-encoded query value based on the
        // values of the properties of the class.
        public string GetBase64EncodedString()
        {
            List<byte> bytes = new List<byte>();

            // Fill in the byte array
            bytes.Add(protocolVersion);
            bytes.Add(commandCode);
            bytes.AddRange(BitConverter.GetBytes((short)locale));
            bytes.Add(deviceIdLength);

            if (deviceIdLength > 0)
            {
                bytes.AddRange(ASCIIEncoding.ASCII.GetBytes(deviceId));
            }

            bytes.Add(policyKeyLength);

            if (policyKeyLength > 0)
            {
                bytes.AddRange(BitConverter.GetBytes(policyKey));
            }

            bytes.Add(deviceTypeLength);

            if (deviceTypeLength > 0)
            {
                bytes.AddRange(ASCIIEncoding.ASCII.GetBytes(deviceType));
            }

            if (commandParameters != null)
            {
                for (int i = 0; i < commandParameters.Length; i++)
                {
                    bytes.Add(commandParameters[i].tag);
                    bytes.Add(commandParameters[i].length);
                    bytes.AddRange(ASCIIEncoding.ASCII.GetBytes(commandParameters[i].value));
                }
            }

            return Convert.ToBase64String(bytes.ToArray());
        }

        // This function adds a command parameter to the array of EncodedParameters.
        public bool AddCommandParameter(string parameterName, string parameterValue)
        {
            EncodedParameter newParameter;
            byte parameterTag;

            if (parameterDictionary.TryGetValue(parameterName.ToUpper(), out parameterTag))
            {
                newParameter.tag = parameterTag;
                newParameter.value = parameterValue;
                newParameter.length = Convert.ToByte(parameterValue.Length);

                if (commandParameters == null)
                {
                    commandParameters = new EncodedParameter[1];

                    commandParameters[0] = newParameter;
                }
                else
                {
                    Array.Resize(ref commandParameters, commandParameters.Length + 1);

                    commandParameters[commandParameters.Length - 1] = newParameter;
                }

                return true;
            }

            return false;
        }
    }
}
