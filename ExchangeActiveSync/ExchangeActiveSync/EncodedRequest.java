//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import CS2JNet.JavaSupport.language.RefSupport;
import ExchangeActiveSync.EncodedParameter;

// This class represents a base64-encoded query value
// as specified in [MS-ASHTTP] section 2.2.1.1.1.1.
public class EncodedRequest   
{
    private byte protocolVersion = 0;
    private byte commandCode = 0;
    private Int32 locale = 0;
    private byte deviceIdLength = 0;
    private String deviceId = "";
    private byte policyKeyLength = 0;
    private UInt32 policyKey = 0;
    private byte deviceTypeLength = 0;
    private String deviceType = "";
    private EncodedParameter[] commandParameters = null;
    private Dictionary<String, byte> commandDictionary = new Dictionary<String, byte>();
    private Dictionary<String, byte> parameterDictionary = new Dictionary<String, byte>();
    private Dictionary<String, Int32> localeDictionary = new Dictionary<String, Int32>();
    public EncodedRequest() throws Exception {
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

    public byte getProtocolVersion() throws Exception {
        return protocolVersion;
    }

    public void setProtocolVersion(byte value) throws Exception {
        protocolVersion = value;
    }

    public byte getCommandCode() throws Exception {
        return commandCode;
    }

    public boolean setCommandCode(String strCommand) throws Exception {
        RefSupport refVar___0 = new RefSupport();
        resVar___0 = commandDictionary.TryGetValue(strCommand.ToUpper(), refVar___0);
        commandCode = refVar___0.getValue();
        return resVar___0;
    }

    public Int32 getLocale() throws Exception {
        return locale;
    }

    public boolean setLocale(String strLocale) throws Exception {
        RefSupport refVar___1 = new RefSupport();
        resVar___1 = localeDictionary.TryGetValue(strLocale.ToUpper(), refVar___1);
        locale = refVar___1.getValue();
        return resVar___1;
    }

    public Int32 getDeviceIdLength() throws Exception {
        return Convert.ToInt32(deviceIdLength);
    }

    public String getDeviceId() throws Exception {
        return deviceId;
    }

    public void setDeviceId(String value) throws Exception {
        deviceId = value;
        deviceIdLength = Convert.ToByte(deviceId.Length);
    }

    public Int32 getPolicyKeyLength() throws Exception {
        return Convert.ToInt32(policyKeyLength);
    }

    public UInt32 getPolicyKey() throws Exception {
        return policyKey;
    }

    public void setPolicyKey(UInt32 value) throws Exception {
        policyKey = value;
        policyKeyLength = Convert.ToByte();
    }

    public Int32 getDeviceTypeLength() throws Exception {
        return Convert.ToInt32(deviceTypeLength);
    }

    public String getDeviceType() throws Exception {
        return deviceType;
    }

    public void setDeviceType(String value) throws Exception {
        deviceType = value;
        deviceTypeLength = Convert.ToByte(deviceType.Length);
    }

    public EncodedParameter[] getCommandParameters() throws Exception {
        return commandParameters;
    }

    // This function returns the base64-encoded query value based on the
    // values of the properties of the class.
    public String getBase64EncodedString() throws Exception {
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
            for (int i = 0;i < commandParameters.Length;i++)
            {
                bytes.Add(commandParameters[i].tag);
                bytes.Add(commandParameters[i].length);
                bytes.AddRange(ASCIIEncoding.ASCII.GetBytes(commandParameters[i].value));
            }
        }
         
        return Convert.ToBase64String(bytes.ToArray());
    }

    // This function adds a command parameter to the array of EncodedParameters.
    public boolean addCommandParameter(String parameterName, String parameterValue) throws Exception {
        EncodedParameter newParameter = new EncodedParameter();
        byte parameterTag = new byte();
        RefSupport<byte> refVar___2 = new RefSupport<byte>();
        boolean boolVar___2 = parameterDictionary.TryGetValue(parameterName.ToUpper(), refVar___2);
        parameterTag = refVar___2.getValue();
        if (boolVar___2)
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
                RefSupport refVar___3 = new RefSupport(commandParameters);
                Array.Resize(refVar___3, commandParameters.Length + 1);
                commandParameters = refVar___3.getValue();
                commandParameters[commandParameters.Length - 1] = newParameter;
            } 
            return true;
        }
         
        return false;
    }

}


