using System;
using System.IO;
using System.Net;

namespace ExchangeActiveSync
{
    // This class represents an OPTIONS request as specified
    // in MS-ASHTTP section 2.2.3.
    class ASOptionsRequest
    {
        private NetworkCredential credential = null;
        private string server = null;
        private bool useSSL = true;

        #region Property Accessors
        public NetworkCredential Credentials
        {
            get
            {
                return credential;
            }
            set
            {
                credential = value;
            }
        }

        public string Server
        {
            get
            {
                return server;
            }
            set
            {
                server = value;
            }
        }

        public bool UseSSL
        {
            get
            {
                return useSSL;
            }
            set
            {
                useSSL = value;
            }
        }
        #endregion

        // This function sends the OPTIONS request and returns an
        // ASOptionsResponse class that represents the response.
        public ASOptionsResponse GetOptions()
        {
            if (credential == null || server == null)
                throw new InvalidDataException("ASOptionsRequest not initialized.");

            string uriString = string.Format("{0}//{1}/Microsoft-Server-ActiveSync", UseSSL ? "https:" : "http:", Server);
            Uri serverUri = new Uri(uriString);
            CredentialCache creds = new CredentialCache();
            // Using Basic authentication
            creds.Add(serverUri, "Basic", Credentials);

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
                ASError.ReportException(ex);
                return null;
            }
        }
    }
}
