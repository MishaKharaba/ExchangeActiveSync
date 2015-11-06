using System;
using System.Collections.Generic;
using System.IO;
using System.Net;

namespace ExchangeActiveSync
{
    // This class represents a generic Exchange ActiveSync command response.
    class ASCommandResponse
    {
        private byte[] wbxmlBytes = null;
        private string xmlString = null;
        private HttpStatusCode httpStatus = HttpStatusCode.OK;

        #region Property Accessors
        public byte[] WbxmlBytes
        {
            get
            {
                return wbxmlBytes;
            }
        }

        public string XmlString
        {
            get
            {
                return xmlString;
            }
        }

        public HttpStatusCode HttpStatus
        {
            get
            {
                return httpStatus;
            }
        }
        #endregion

        public ASCommandResponse(HttpWebResponse httpResponse)
        {
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
            xmlString = DecodeWBXML(wbxmlBytes);
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
    }
}
