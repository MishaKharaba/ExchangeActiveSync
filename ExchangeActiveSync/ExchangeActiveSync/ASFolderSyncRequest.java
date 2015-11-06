//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import CS2JNet.System.StringSupport;
import ExchangeActiveSync.ASCommandRequest;
import ExchangeActiveSync.ASCommandResponse;
import ExchangeActiveSync.ASFolderSyncResponse;
import ExchangeActiveSync.Namespaces;
import ExchangeActiveSync.Xmlns;

// This class represents the FolderSync command
// request specified in MS-ASCMD section 2.2.2.4.1.
public class ASFolderSyncRequest  extends ASCommandRequest 
{
    private String syncKey = "0";
    public String getSyncKey() throws Exception {
        return syncKey;
    }

    public void setSyncKey(String value) throws Exception {
        syncKey = value;
    }

    public ASFolderSyncRequest() throws Exception {
        setCommand("FolderSync");
    }

    // This function generates an ASFolderSyncResponse from an
    // HTTP response.
    protected ASCommandResponse wrapHttpResponse(HttpWebResponse httpResp) throws Exception {
        return new ASFolderSyncResponse(httpResp);
    }

    // This function generates the XML request body
    // for the FolderSync request.
    protected void generateXMLPayload() throws Exception {
        // If WBXML was explicitly set, use that
        if (getWbxmlBytes() != null)
            return ;
         
        // Otherwise, use the properties to build the XML and then WBXML encode it
        XmlDocument folderSyncXML = new XmlDocument();
        XmlDeclaration xmlDeclaration = folderSyncXML.CreateXmlDeclaration("1.0", "utf-8", null);
        folderSyncXML.InsertBefore(xmlDeclaration, null);
        XmlNode folderSyncNode = folderSyncXML.CreateElement(Xmlns.folderHierarchyXmlns, "FolderSync", Namespaces.folderHierarchyNamespace);
        folderSyncNode.Prefix = Xmlns.folderHierarchyXmlns;
        folderSyncXML.AppendChild(folderSyncNode);
        if (StringSupport.equals(syncKey, ""))
            syncKey = "0";
         
        XmlNode syncKeyNode = folderSyncXML.CreateElement(Xmlns.folderHierarchyXmlns, "SyncKey", Namespaces.folderHierarchyNamespace);
        syncKeyNode.Prefix = Xmlns.folderHierarchyXmlns;
        syncKeyNode.InnerText = syncKey;
        folderSyncNode.AppendChild(syncKeyNode);
        StringWriter sw = new StringWriter();
        XmlTextWriter xmlw = new XmlTextWriter(sw);
        xmlw.Formatting = Formatting.Indented;
        folderSyncXML.WriteTo(xmlw);
        xmlw.Flush();
        setXmlString(sw.ToString());
    }

}


