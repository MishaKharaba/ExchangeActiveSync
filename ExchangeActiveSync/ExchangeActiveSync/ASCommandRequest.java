//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASCommandResponse;
import ExchangeActiveSync.ASError;
import ExchangeActiveSync.ASWBXML;
import ExchangeActiveSync.CommandParameter;
import ExchangeActiveSync.EncodedRequest;

// This class represents a generic Exchange ActiveSync command request.
public class ASCommandRequest   
{
    private NetworkCredential credential = null;
    private String server = null;
    private boolean useSSL = true;
    private byte[] wbxmlBytes = null;
    private String xmlString = null;
    private String protocolVersion = null;
    private String requestLine = null;
    private boolean useEncodedRequestLine = true;
    private String command = null;
    private String user = null;
    private String deviceID = null;
    private String deviceType = null;
    private UInt32 policyKey = 0;
    private CommandParameter[] parameters = null;
    public NetworkCredential getCredentials() throws Exception {
        return credential;
    }

    public void setCredentials(NetworkCredential value) throws Exception {
        credential = value;
    }

    public String getServer() throws Exception {
        return server;
    }

    public void setServer(String value) throws Exception {
        server = value;
    }

    public boolean getUseSSL() throws Exception {
        return useSSL;
    }

    public void setUseSSL(boolean value) throws Exception {
        useSSL = value;
    }

    public byte[] getWbxmlBytes() throws Exception {
        return wbxmlBytes;
    }

    public void setWbxmlBytes(byte[] value) throws Exception {
        wbxmlBytes = value;
        // Loading WBXML bytes causes immediate decoding
        xmlString = decodeWBXML(wbxmlBytes);
    }

    public String getXmlString() throws Exception {
        return xmlString;
    }

    public void setXmlString(String value) throws Exception {
        xmlString = value;
        // Loading XML causes immediate encoding
        wbxmlBytes = encodeXMLString(xmlString);
    }

    public String getProtocolVersion() throws Exception {
        return protocolVersion;
    }

    public void setProtocolVersion(String value) throws Exception {
        protocolVersion = value;
    }

    public String getRequestLine() throws Exception {
        // Generate on demand
        buildRequestLine();
        return requestLine;
    }

    public void setRequestLine(String value) throws Exception {
        requestLine = value;
    }

    public boolean getUseEncodedRequestLine() throws Exception {
        return useEncodedRequestLine;
    }

    public void setUseEncodedRequestLine(boolean value) throws Exception {
        useEncodedRequestLine = value;
    }

    public String getCommand() throws Exception {
        return command;
    }

    public void setCommand(String value) throws Exception {
        command = value;
    }

    public String getUser() throws Exception {
        return user;
    }

    public void setUser(String value) throws Exception {
        user = value;
    }

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

    public UInt32 getPolicyKey() throws Exception {
        return policyKey;
    }

    public void setPolicyKey(UInt32 value) throws Exception {
        policyKey = value;
    }

    public CommandParameter[] getCommandParameters() throws Exception {
        return parameters;
    }

    public void setCommandParameters(CommandParameter[] value) throws Exception {
        parameters = value;
    }

    // This function sends the request and returns
    // the response.
    public ASCommandResponse getResponse() throws Exception {
        generateXMLPayload();
        if (getCredentials() == null || getServer() == null || getProtocolVersion() == null || getWbxmlBytes() == null)
            throw new InvalidDataException("ASCommandRequest not initialized.");
         
        // Generate the URI for the request
        String uriString = String.Format("{0}//{1}/Microsoft-Server-ActiveSync?{2}", useSSL ? "https:" : "http:", server, getRequestLine());
        Uri serverUri = new Uri(uriString);
        CredentialCache creds = new CredentialCache();
        // Using Basic authentication
        creds.Add(serverUri, "Basic", credential);
        HttpWebRequest httpReq = (HttpWebRequest)WebRequest.Create(uriString);
        httpReq.Credentials = creds;
        httpReq.Method = "POST";
        // MS-ASHTTP section 2.2.1.1.2.2
        httpReq.ContentType = "application/vnd.ms-sync.wbxml";
        if (!getUseEncodedRequestLine())
        {
            // Encoded request lines include the protocol version
            // and policy key in the request line.
            // Non-encoded request lines require that those
            // values be passed as headers.
            httpReq.Headers.Add("MS-ASProtocolVersion", getProtocolVersion());
            httpReq.Headers.Add("X-MS-PolicyKey", getPolicyKey().ToString());
        }
         
        try
        {
            Stream requestStream = httpReq.GetRequestStream();
            requestStream.Write(getWbxmlBytes(), 0, getWbxmlBytes().Length);
            requestStream.Close();
            HttpWebResponse httpResp = (HttpWebResponse)httpReq.GetResponse();
            ASCommandResponse response = wrapHttpResponse(httpResp);
            httpResp.Close();
            return response;
        }
        catch (Exception ex)
        {
            ASError.reportException(ex);
            return null;
        }
    
    }

    // This function generates an ASCommandResponse from an
    // HTTP response.
    protected ASCommandResponse wrapHttpResponse(HttpWebResponse httpResp) throws Exception {
        return new ASCommandResponse(httpResp);
    }

    // This function builds a request line from the class properties.
    protected void buildRequestLine() throws Exception {
        if (getCommand() == null || getUser() == null || getDeviceID() == null || getDeviceType() == null)
            throw new InvalidDataException("ASCommandRequest not initialized.");
         
        if (getUseEncodedRequestLine() == true)
        {
            // Use the EncodedRequest class to generate
            // an encoded request line
            EncodedRequest encodedRequest = new EncodedRequest();
            encodedRequest.setProtocolVersion(Convert.ToByte(Convert.ToSingle(getProtocolVersion()) * 10));
            encodedRequest.setCommandCode(getCommand());
            encodedRequest.setLocale("en-us");
            encodedRequest.setDeviceId(getDeviceID());
            encodedRequest.setDeviceType(getDeviceType());
            encodedRequest.setPolicyKey(getPolicyKey());
            // Add the User parameter to the request line
            encodedRequest.addCommandParameter("User",user);
            // Add any command-specific parameters
            if (getCommandParameters() != null)
            {
                for (int i = 0;i < parameters.Length;i++)
                {
                    encodedRequest.AddCommandParameter(getCommandParameters()[i].Parameter, getCommandParameters()[i].Value);
                }
            }
             
            // Generate the request line
            setRequestLine(encodedRequest.getBase64EncodedString());
        }
        else
        {
            // Generate a plain-text request line.
            setRequestLine(String.Format("Cmd={0}&User={1}&DeviceId={2}&DeviceType={3}", getCommand(), getUser(), getDeviceID(), getDeviceType()));
            if (getCommandParameters() != null)
            {
                for (int i = 0;i < parameters.Length;i++)
                {
                    setRequestLine(String.Format("{0}&{1}={2}", getRequestLine(), getCommandParameters()[i].Parameter, getCommandParameters()[i].Value));
                }
            }
             
        } 
    }

    // This function generates an XML payload.
    protected void generateXMLPayload() throws Exception {
    }

    // For the base class, this is a no-op.
    // Classes that extend this class to implement
    // commands override this function to generate
    // the XML payload based on the command's request schema
    // This function uses the ASWBXML class to decode
    // a WBXML stream into XML.
    private String decodeWBXML(byte[] wbxml) throws Exception {
        try
        {
            ASWBXML decoder = new ASWBXML();
            decoder.LoadBytes(wbxml);
            return decoder.getXml();
        }
        catch (Exception ex)
        {
            ASError.reportException(ex);
            return "";
        }
    
    }

    // This function uses the ASWBXML class to encode
    // XML into a WBXML stream.
    private byte[] encodeXMLString(String xmlString) throws Exception {
        try
        {
            ASWBXML encoder = new ASWBXML();
            encoder.loadXml(xmlString);
            return encoder.getBytes();
        }
        catch (Exception ex)
        {
            ASError.reportException(ex);
            return null;
        }
    
    }

}


