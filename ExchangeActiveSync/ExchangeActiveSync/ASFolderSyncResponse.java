//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASCommandResponse;
import ExchangeActiveSync.Folder;
import ExchangeActiveSync.Namespaces;
import ExchangeActiveSync.Xmlns;

// This class represents a FolderSync command
// response as specified in MS-ASCMD section 2.2.2.4.2.
public class ASFolderSyncResponse  extends ASCommandResponse 
{
    public enum FolderSyncStatus
    {
        // This enumeration covers the possible Status
        // values for FolderSync responses.
        __dummyEnum__0,
        Success,
        __dummyEnum__1,
        __dummyEnum__2,
        __dummyEnum__3,
        __dummyEnum__4,
        ServerError,
        __dummyEnum__5,
        __dummyEnum__6,
        InvalidSyncKey,
        InvalidFormat,
        UnknownError,
        UnknownCode
    }
    private XmlDocument responseXml = null;
    private XmlNamespaceManager xmlNsMgr = null;
    private Int32 status = 0;
    public Int32 getStatus() throws Exception {
        return status;
    }

    public ASFolderSyncResponse(HttpWebResponse httpResponse) throws Exception {
        super(httpResponse);
        responseXml = new XmlDocument();
        responseXml.LoadXml(getXmlString());
        xmlNsMgr = new XmlNamespaceManager(responseXml.NameTable);
        xmlNsMgr.AddNamespace(Xmlns.folderHierarchyXmlns, Namespaces.folderHierarchyNamespace);
        setStatus();
    }

    // This function updates a folder tree based on the
    // changes received from the server in the response.
    public void updateFolderTree(Folder rootFolder) throws Exception {
        rootFolder.setLastSyncTime(DateTime.Now.ToUniversalTime());
        try
        {
            // Get sync key
            XmlNode syncKeyNode = responseXml.SelectSingleNode(".//folderhierarchy:SyncKey", xmlNsMgr);
            rootFolder.setSyncKey(syncKeyNode.InnerText);
            // Process adds (new folders) first
            XmlNodeList addNodes = responseXml.SelectNodes(".//folderhierarchy:Add", xmlNsMgr);
            for (Object __dummyForeachVar0 : addNodes)
            {
                XmlNode addNode = (XmlNode)__dummyForeachVar0;
                XmlNode nameNode = addNode.SelectSingleNode(".//folderhierarchy:DisplayName", xmlNsMgr);
                XmlNode serverIdNode = addNode.SelectSingleNode(".//folderhierarchy:ServerId", xmlNsMgr);
                XmlNode parentIdNode = addNode.SelectSingleNode(".//folderhierarchy:ParentId", xmlNsMgr);
                XmlNode typeNode = addNode.SelectSingleNode(".//folderhierarchy:Type", xmlNsMgr);
                rootFolder.AddFolder(nameNode.InnerText, serverIdNode.InnerText, parentIdNode.InnerText, (ExchangeActiveSync.Folder.FolderType)XmlConvert.ToInt32(typeNode.InnerText));
            }
            // Then process deletes
            XmlNodeList deleteNodes = responseXml.SelectNodes(".//folderhierarchy:Delete", xmlNsMgr);
            for (Object __dummyForeachVar1 : deleteNodes)
            {
                XmlNode deleteNode = (XmlNode)__dummyForeachVar1;
                XmlNode serverIdNode = deleteNode.SelectSingleNode(".//folderhierarchy:ServerId", xmlNsMgr);
                Folder removeFolder = rootFolder.FindFolderById(serverIdNode.InnerText);
                removeFolder.remove();
            }
            // Finally process any updates to existing folders
            XmlNodeList updateNodes = responseXml.SelectNodes(".//folderhierarchy:Update", xmlNsMgr);
            for (Object __dummyForeachVar2 : updateNodes)
            {
                XmlNode updateNode = (XmlNode)__dummyForeachVar2;
                XmlNode nameNode = updateNode.SelectSingleNode(".//folderhierarchy:DisplayName", xmlNsMgr);
                XmlNode serverIdNode = updateNode.SelectSingleNode(".//folderhierarchy:ServerId", xmlNsMgr);
                XmlNode parentIdNode = updateNode.SelectSingleNode(".//folderhierarchy:ParentId", xmlNsMgr);
                XmlNode typeNode = updateNode.SelectSingleNode(".//folderhierarchy:Type", xmlNsMgr);
                Folder updateFolder = rootFolder.FindFolderById(serverIdNode.InnerText);
                Folder updateParent = rootFolder.FindFolderById(parentIdNode.InnerText);
                updateFolder.Update(nameNode.InnerText, updateParent, (ExchangeActiveSync.Folder.FolderType)XmlConvert.ToInt32(typeNode.InnerText));
            }
        }
        catch (Exception e)
        {
            // Rather than attempting to recover, reset sync key
            // and empty folders. The next FolderSync should
            // re-sync folders.
            rootFolder.setSyncKey("0");
            rootFolder.removeAllSubFolders();
        }

        rootFolder.saveFolderInfo();
    }

    // This function extracts the response status from the
    // XML and sets the status property.
    private void setStatus() throws Exception {
        XmlNode folderSyncNode = responseXml.SelectSingleNode(".//folderhierarchy:FolderSync", xmlNsMgr);
        XmlNode statusNode = null;
        if (folderSyncNode != null)
            statusNode = folderSyncNode.SelectSingleNode(".//folderhierarchy:Status", xmlNsMgr);
         
        if (statusNode != null)
            status = XmlConvert.ToInt32(statusNode.InnerText);
         
    }

}


