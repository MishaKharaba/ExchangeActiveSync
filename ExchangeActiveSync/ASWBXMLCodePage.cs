using System.Collections.Generic;

namespace ExchangeActiveSync
{
    // This class represents a WBXML code page
    // and associates a namespace (and corresponding xmlns value)
    // to that code page.
    class ASWBXMLCodePage
    {
        private string codePageNamespace = "";
        private string codePageXmlns = "";
        private Dictionary<byte, string> tokenLookup = new Dictionary<byte,string>();
        private Dictionary<string, byte> tagLookup = new Dictionary<string, byte>();

        #region Property accessors
        public string Namespace
        {
            get
            {
                return codePageNamespace;
            }
            set
            {
                codePageNamespace = value;
            }
        }

        public string Xmlns
        {
            get
            {
                return codePageXmlns;
            }
            set
            {
                codePageXmlns = value;
            }
        }
        #endregion

        // This function adds a token/tag pair to the
        // code page.
        public void AddToken(byte token, string tag)
        {
            tokenLookup.Add(token, tag);
            tagLookup.Add(tag, token);
        }

        // This function returns the token for a given
        // tag.
        public byte GetToken(string tag)
        {
            if (tagLookup.ContainsKey(tag))
                return tagLookup[tag];

            return 0xFF;
        }

        // This function returns the tag for a given
        // token.
        public string GetTag(byte token)
        {
            if (tokenLookup.ContainsKey(token))
                return tokenLookup[token];

            return null;
        }
    }
}
