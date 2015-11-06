using System;
using System.Net;

namespace ExchangeActiveSync
{
    // This class represents an OPTIONS response as
    // specified in MS-ASHTTP section 2.2.4.
    class ASOptionsResponse
    {
        private string commands = null;
        private string versions = null;

        public ASOptionsResponse(HttpWebResponse httpResponse)
        {
            // Get the MS-ASProtocolCommands header to determine the
            // supported commands
            commands = httpResponse.GetResponseHeader("MS-ASProtocolCommands");

            // Get the MS-ASProtocolVersions header to determine the
            // supported versions
            versions = httpResponse.GetResponseHeader("MS-ASProtocolVersions");
        }

        #region Property Accessors
        public string SupportedCommands
        {
            get
            {
                return commands;
            }
        }

        public string SupportedVersions
        {
            get
            {
                return versions;
            }
        }

        public string HighestSupportedVersion
        {
            get
            {
                // Split the value of the MS-ASProtocolVersions header
                // into an array of values
                char[] delimiters = { ',' };
                string[] versions = SupportedVersions.Split(delimiters);

                string highestVersion = "0";

                // Loop through the values to find the highest one
                foreach (string version in versions)
                {
                    if (Convert.ToSingle(version) > Convert.ToSingle(highestVersion))
                    {
                        highestVersion = version;
                    }
                }

                return highestVersion;
            }
        }
        #endregion
    }
}
