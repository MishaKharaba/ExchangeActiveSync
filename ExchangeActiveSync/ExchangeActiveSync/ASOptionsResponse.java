//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;


// This class represents an OPTIONS response as
// specified in MS-ASHTTP section 2.2.4.
public class ASOptionsResponse   
{
    private String commands = null;
    private String versions = null;
    public ASOptionsResponse(HttpWebResponse httpResponse) throws Exception {
        // Get the MS-ASProtocolCommands header to determine the
        // supported commands
        commands = httpResponse.GetResponseHeader("MS-ASProtocolCommands");
        // Get the MS-ASProtocolVersions header to determine the
        // supported versions
        versions = httpResponse.GetResponseHeader("MS-ASProtocolVersions");
    }

    public String getSupportedCommands() throws Exception {
        return commands;
    }

    public String getSupportedVersions() throws Exception {
        return versions;
    }

    public String getHighestSupportedVersion() throws Exception {
        // Split the value of the MS-ASProtocolVersions header
        // into an array of values
        char[] delimiters;
        String[] versions = getSupportedVersions().Split(delimiters);
        String highestVersion = "0";
        for (Object __dummyForeachVar0 : versions)
        {
            // Loop through the values to find the highest one
            String version = (String)__dummyForeachVar0;
            if (Convert.ToSingle(version) > Convert.ToSingle(highestVersion))
            {
                highestVersion = version;
            }
             
        }
        return highestVersion;
    }

}


