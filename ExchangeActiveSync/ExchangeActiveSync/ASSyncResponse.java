//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import CS2JNet.System.StringSupport;
import ExchangeActiveSync.ASCommandResponse;
import ExchangeActiveSync.Folder;
import ExchangeActiveSync.Namespaces;
import ExchangeActiveSync.ServerSyncCommand;
import ExchangeActiveSync.ServerSyncCommand.ServerSyncCommandType;
import ExchangeActiveSync.Xmlns;

// This class represents the Sync command response
public class ASSyncResponse  extends ASCommandResponse 
{
    public enum SyncStatus
    {
        // This enumeration covers the possible Status
        // values for FolderSync responses.
        __dummyEnum__0,
        Success,
        __dummyEnum__1,
        InvalidSyncKey,
        ProtocolError,
        ServerError,
        ClientServerConversionError,
        ServerOverwriteConflict,
        ObjectNotFound,
        SyncCannotComplete,
        __dummyEnum__2,
        __dummyEnum__3,
        FolderHierarchyOutOfDate,
        PartialSyncNotValid,
        InvalidDelayValue,
        InvalidSync,
        Retry
    }
    private XmlDocument responseXml = null;
    private XmlNamespaceManager xmlNsMgr = null;
    private Int32 status = 0;
    private List<Folder> folderList = null;
    public Int32 getStatus() throws Exception {
        return status;
    }

    public ASSyncResponse(HttpWebResponse httpResponse, List<Folder> folders) throws Exception {
        super(httpResponse);
        folderList = folders;
        // Sync responses can be empty
        if (!StringSupport.equals(getXmlString(), ""))
        {
            responseXml = new XmlDocument();
            responseXml.LoadXml(getXmlString());
            xmlNsMgr = new XmlNamespaceManager(responseXml.NameTable);
            xmlNsMgr.AddNamespace(Xmlns.airSyncXmlns, Namespaces.airSyncNamespace);
            xmlNsMgr.AddNamespace(Xmlns.airSyncBaseXmlns, Namespaces.airSyncBaseNamespace);
            setStatus();
        }
         
    }

    // This function gets the sync key for
    // a folder.
    public String getSyncKeyForFolder(String folderId) throws Exception {
        String folderSyncKey = "0";
        String collectionXPath = ".//airsync:Collection[airsync:CollectionId = \"" + folderId + "\"]";
        XmlNode folderNode = responseXml.SelectSingleNode(collectionXPath, xmlNsMgr);
        if (folderNode != null)
        {
            XmlNode syncKeyNode = folderNode.SelectSingleNode("./airsync:SyncKey", xmlNsMgr);
            if (syncKeyNode != null)
                folderSyncKey = syncKeyNode.InnerText;
             
        }
         
        return folderSyncKey;
    }

    // This function returns the new items (Adds)
    // for a folder.
    public List<ServerSyncCommand> getServerAddsForFolder(String folderId) throws Exception {
        List<ServerSyncCommand> addCommands = new List<ServerSyncCommand>();
        String collectionXPath = ".//airsync:Collection[airsync:CollectionId = \"" + folderId + "\"]";
        XmlNode folderNode = responseXml.SelectSingleNode(collectionXPath, xmlNsMgr);
        if (folderNode != null)
        {
            XmlNodeList addNodes = folderNode.SelectNodes(".//airsync:Add", xmlNsMgr);
            for (Object __dummyForeachVar0 : addNodes)
            {
                XmlNode addNode = (XmlNode)__dummyForeachVar0;
                XmlNode serverIdNode = addNode.SelectSingleNode("./airsync:ServerId", xmlNsMgr);
                XmlNode applicationDataNode = addNode.SelectSingleNode("./airsync:ApplicationData", xmlNsMgr);
                if (serverIdNode != null && applicationDataNode != null)
                {
                    ServerSyncCommand addCommand = new ServerSyncCommand(ServerSyncCommandType.Add, serverIdNode.InnerText, applicationDataNode, null);
                    addCommands.Add(addCommand);
                }
                 
            }
        }
         
        return addCommands;
    }

    // This function extracts the response status from the
    // XML and sets the status property.
    private void setStatus() throws Exception {
        XmlNode syncNode = responseXml.SelectSingleNode(".//airsync:Sync", xmlNsMgr);
        XmlNode statusNode = null;
        if (syncNode != null)
            statusNode = syncNode.SelectSingleNode(".//airsync:Status", xmlNsMgr);
         
        if (statusNode != null)
            status = XmlConvert.ToInt32(statusNode.InnerText);
         
    }

}


