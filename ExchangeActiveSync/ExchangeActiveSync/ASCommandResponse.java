//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASError;
import ExchangeActiveSync.ASWBXML;

// This class represents a generic Exchange ActiveSync command response.
public class ASCommandResponse   
{
    private byte[] wbxmlBytes = null;
    private String xmlString = null;
    private HttpStatusCode httpStatus = HttpStatusCode.OK;
    public byte[] getWbxmlBytes() throws Exception {
        return wbxmlBytes;
    }

    public String getXmlString() throws Exception {
        return xmlString;
    }

    public HttpStatusCode getHttpStatus() throws Exception {
        return httpStatus;
    }

    public ASCommandResponse(HttpWebResponse httpResponse) throws Exception {
        httpStatus = httpResponse.StatusCode;
        Stream responseStream = httpResponse.GetResponseStream();
        List<byte> bytes = new List<byte>();
        byte[] byteBuffer = new byte[256];
        int count = 0;
        // Read the WBXML data from the response stream
        // 256 bytes at a time.
        count = responseStream.Read(byteBuffer, 0, 256);
        while (count > 0)
        {
            // Add the 256 bytes to the List
            bytes.AddRange(byteBuffer);
            if (count < 256)
            {
                // If the last read did not actually read 256 bytes
                // remove the extra.
                int excess = 256 - count;
                bytes.RemoveRange(bytes.Count - excess, excess);
            }
             
            // Read the next 256 bytes from the response stream
            count = responseStream.Read(byteBuffer, 0, 256);
        }
        wbxmlBytes = bytes.ToArray();
        // Decode the WBXML
        xmlString = decodeWBXML(wbxmlBytes);
    }

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

}


