//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASError;
import ExchangeActiveSync.ASOptionsResponse;

// This class represents an OPTIONS request as specified
// in MS-ASHTTP section 2.2.3.
public class ASOptionsRequest   
{
    private NetworkCredential credential = null;
    private String server = null;
    private boolean useSSL = true;
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

    // This function sends the OPTIONS request and returns an
    // ASOptionsResponse class that represents the response.
    public ASOptionsResponse getOptions() throws Exception {
        if (credential == null || server == null)
            throw new InvalidDataException("ASOptionsRequest not initialized.");
         
        String uriString = String.Format("{0}//{1}/Microsoft-Server-ActiveSync", getUseSSL() ? "https:" : "http:", getServer());
        Uri serverUri = new Uri(uriString);
        CredentialCache creds = new CredentialCache();
        // Using Basic authentication
        creds.Add(serverUri, "Basic", getCredentials());
        HttpWebRequest httpReq = (HttpWebRequest)WebRequest.Create(uriString);
        httpReq.Credentials = creds;
        httpReq.Method = "OPTIONS";
        try
        {
            HttpWebResponse httpResp = (HttpWebResponse)httpReq.GetResponse();
            ASOptionsResponse response = new ASOptionsResponse(httpResp);
            httpResp.Close();
            return response;
        }
        catch (Exception ex)
        {
            ASError.reportException(ex);
            return null;
        }
    
    }

}


