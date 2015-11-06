//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;


// This class represents a WBXML code page
// and associates a namespace (and corresponding xmlns value)
// to that code page.
public class ASWBXMLCodePage   
{
    private String codePageNamespace = "";
    private String codePageXmlns = "";
    private Dictionary<byte, String> tokenLookup = new Dictionary<byte, String>();
    private Dictionary<String, byte> tagLookup = new Dictionary<String, byte>();
    public String getNamespace() throws Exception {
        return codePageNamespace;
    }

    public void setNamespace(String value) throws Exception {
        codePageNamespace = value;
    }

    public String getXmlns() throws Exception {
        return codePageXmlns;
    }

    public void setXmlns(String value) throws Exception {
        codePageXmlns = value;
    }

    // This function adds a token/tag pair to the
    // code page.
    public void addToken(byte token, String tag) throws Exception {
        tokenLookup.Add(token, tag);
        tagLookup.Add(tag, token);
    }

    // This function returns the token for a given
    // tag.
    public byte getToken(String tag) throws Exception {
        if (tagLookup.ContainsKey(tag))
            return tagLookup[tag];
         
        return 0xFF;
    }

    // This function returns the tag for a given
    // token.
    public String getTag(byte token) throws Exception {
        if (tokenLookup.ContainsKey(token))
            return tokenLookup[token];
         
        return null;
    }

}


