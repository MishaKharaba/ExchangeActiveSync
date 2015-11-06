using System;
using System.IO;
using System.Net;

namespace ExchangeActiveSync
{
    // This structure is used to store command-specific
    // parameters (MS-ASCMD section 2.2.1.1.1.2.5)
    struct CommandParameter
    {
        public string Parameter;
        public string Value;
    }

    // This class represents a generic Exchange ActiveSync command request.
    class ASCommandRequest
    {
        private NetworkCredential credential = null;
        private string server = null;
        private bool useSSL = true;
        private byte[] wbxmlBytes = null;
        private string xmlString = null;
        private string protocolVersion = null;
        private string requestLine = null;
        private bool useEncodedRequestLine = true;
        private string command = null;
        private string user = null;
        private string deviceID = null;
        private string deviceType = null;
        private UInt32 policyKey = 0;
        private CommandParameter[] parameters = null;

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

        public byte[] WbxmlBytes
        {
            get
            {
                return wbxmlBytes;
            }
            set
            {
                wbxmlBytes = value;
                // Loading WBXML bytes causes immediate decoding
                xmlString = DecodeWBXML(wbxmlBytes);
            }
        }

        public string XmlString
        {
            get
            {
                return xmlString;
            }
            set
            {
                xmlString = value;
                // Loading XML causes immediate encoding
                wbxmlBytes = EncodeXMLString(xmlString);
            }
        }

        public string ProtocolVersion
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

        public string RequestLine
        {
            get
            {
                // Generate on demand
                BuildRequestLine();
                return requestLine;
            }
            set
            {
                requestLine = value;
            }
        }

        public bool UseEncodedRequestLine
        {
            get
            {
                return useEncodedRequestLine;
            }
            set
            {
                useEncodedRequestLine = value;
            }
        }

        public string Command
        {
            get
            {
                return command;
            }
            set
            {
                command = value;
            }
        }

        public string User
        {
            get
            {
                return user;
            }
            set
            {
                user = value;
            }
        }

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

        public UInt32 PolicyKey
        {
            get
            {
                return policyKey;
            }
            set
            {
                policyKey = value;
            }
        }

        public CommandParameter[] CommandParameters
        {
            get
            {
                return parameters;
            }
            set
            {
                parameters = value;
            }
        }
        #endregion

        // This function sends the request and returns
        // the response.
        public ASCommandResponse GetResponse()
        {
            GenerateXMLPayload();

            if (Credentials == null || Server == null || ProtocolVersion == null || WbxmlBytes == null)
                throw new InvalidDataException("ASCommandRequest not initialized.");

            // Generate the URI for the request
            string uriString = string.Format("{0}//{1}/Microsoft-Server-ActiveSync?{2}", 
                useSSL ? "https:" : "http:", server, RequestLine);
            Uri serverUri = new Uri(uriString);
            CredentialCache creds = new CredentialCache();
            // Using Basic authentication
            creds.Add(serverUri, "Basic", credential);

            HttpWebRequest httpReq = (HttpWebRequest)WebRequest.Create(uriString);
            httpReq.Credentials = creds;
            httpReq.Method = "POST";

            // MS-ASHTTP section 2.2.1.1.2.2
            httpReq.ContentType = "application/vnd.ms-sync.wbxml";

            if (!UseEncodedRequestLine)
            {
                // Encoded request lines include the protocol version
                // and policy key in the request line. 
                // Non-encoded request lines require that those
                // values be passed as headers.
                httpReq.Headers.Add("MS-ASProtocolVersion", ProtocolVersion);
                httpReq.Headers.Add("X-MS-PolicyKey", PolicyKey.ToString());
            }

            try
            {
                Stream requestStream = httpReq.GetRequestStream();
                requestStream.Write(WbxmlBytes, 0, WbxmlBytes.Length);
                requestStream.Close();

                HttpWebResponse httpResp = (HttpWebResponse)httpReq.GetResponse();

                ASCommandResponse response = WrapHttpResponse(httpResp);

                httpResp.Close();

                return response;
            }
            catch (Exception ex)
            {
                ASError.ReportException(ex);
                return null;
            }
        }

        // This function generates an ASCommandResponse from an
        // HTTP response.
        protected virtual ASCommandResponse WrapHttpResponse(HttpWebResponse httpResp)
        {
            return new ASCommandResponse(httpResp);
        }

        // This function builds a request line from the class properties.
        protected virtual void BuildRequestLine()
        {
            if (Command == null || User == null || DeviceID == null || DeviceType == null)
                throw new InvalidDataException("ASCommandRequest not initialized.");

            if (UseEncodedRequestLine == true)
            {
                // Use the EncodedRequest class to generate
                // an encoded request line
                EncodedRequest encodedRequest = new EncodedRequest();

                encodedRequest.ProtocolVersion = Convert.ToByte(Convert.ToSingle(ProtocolVersion) * 10);
                encodedRequest.SetCommandCode(Command);
                encodedRequest.SetLocale("en-us");
                encodedRequest.DeviceId = DeviceID;
                encodedRequest.DeviceType = DeviceType;
                encodedRequest.PolicyKey = PolicyKey;

                // Add the User parameter to the request line
                encodedRequest.AddCommandParameter("User", user);

                // Add any command-specific parameters
                if (CommandParameters != null)
                {
                    for (int i = 0; i < parameters.Length; i++)
                    {
                        encodedRequest.AddCommandParameter(CommandParameters[i].Parameter, CommandParameters[i].Value);
                    }
                }

                // Generate the request line
                RequestLine = encodedRequest.GetBase64EncodedString();
            }
            else
            {
                // Generate a plain-text request line.
                RequestLine = string.Format("Cmd={0}&User={1}&DeviceId={2}&DeviceType={3}",
                Command, User, DeviceID, DeviceType);

                if (CommandParameters != null)
                {
                    for (int i = 0; i < parameters.Length; i++)
                    {
                        RequestLine = string.Format("{0}&{1}={2}", RequestLine,
                            CommandParameters[i].Parameter, CommandParameters[i].Value);
                    }
                }
            }
        }

        // This function generates an XML payload.
        protected virtual void GenerateXMLPayload()
        {
            // For the base class, this is a no-op.
            // Classes that extend this class to implement
            // commands override this function to generate
            // the XML payload based on the command's request schema
        }

        // This function uses the ASWBXML class to decode
        // a WBXML stream into XML.
        private string DecodeWBXML(byte[] wbxml)
        {
            try
            {
                ASWBXML decoder = new ASWBXML();
                decoder.LoadBytes(wbxml);
                return decoder.GetXml();
            }
            catch (Exception ex)
            {
                ASError.ReportException(ex);
                return "";
            }
        }

        // This function uses the ASWBXML class to encode
        // XML into a WBXML stream.
        private byte[] EncodeXMLString(string xmlString)
        {
            try
            {
                ASWBXML encoder = new ASWBXML();
                encoder.LoadXml(xmlString);
                return encoder.GetBytes();
            }
            catch (Exception ex)
            {
                ASError.ReportException(ex);
                return null;
            }
        }
    }
}
