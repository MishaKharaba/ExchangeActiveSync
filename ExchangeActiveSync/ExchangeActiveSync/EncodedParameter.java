//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;


// This struct represents an encoded parameter
// as specified in [MS-ASHTTP] section 2.2.1.1.1.1.1.
public class EncodedParameter   
{
    public EncodedParameter() {
    }

    public byte tag = new byte();
    public byte length = new byte();
    public String value = new String();
}


