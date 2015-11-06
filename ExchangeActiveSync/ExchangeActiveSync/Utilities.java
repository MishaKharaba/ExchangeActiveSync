//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:34
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASError;

public class Utilities   
{
    // This function generates a string representation
    // of hexadecimal bytes.
    public static String printHex(byte[] bytes) throws Exception {
        StringBuilder returnString = new StringBuilder();
        for (Object __dummyForeachVar0 : bytes)
        {
            byte byteItem = (Byte)__dummyForeachVar0;
            // Append the 2-digit hex value of the byte
            returnString.Append(byteItem.ToString("X2"));
        }
        return returnString.ToString();
    }

    // This function converts a string representation
    // of hexadecimal bytes into a byte array
    public static byte[] convertHexToBytes(String hexString) throws Exception {
        int numChars = hexString.Length;
        byte[] byteArray = null;
        if (numChars > 0)
        {
            try
            {
                byteArray = new byte[numChars / 2];
                for (int i = 0;i < numChars;i += 2)
                {
                    byteArray[i / 2] = Convert.ToByte(hexString.Substring(i, 2), 16);
                }
            }
            catch (Exception ex)
            {
                ASError.reportException(ex);
                return null;
            }
        
        }
         
        return byteArray;
    }

}


