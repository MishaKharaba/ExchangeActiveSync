//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASCommandRequest;
import ExchangeActiveSync.ASCommandResponse;
import ExchangeActiveSync.ASSyncResponse;
import ExchangeActiveSync.Folder;
import ExchangeActiveSync.Namespaces;
import ExchangeActiveSync.Xmlns;

// This class represents the Sync command request
public class ASSyncRequest  extends ASCommandRequest 
{
    private Int32 wait = 0;
    // 1 - 59 minutes
    private Int32 heartBeatInterval = 0;
    // 60 - 3540 seconds
    private Int32 windowSize = 0;
    // 1 - 512 changes
    private boolean isPartial = false;
    List<Folder> folderList = null;
    public Int32 getWait() throws Exception {
        return wait;
    }

    public void setWait(Int32 value) throws Exception {
        wait = value;
    }

    public Int32 getHeartBeatInterval() throws Exception {
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(Int32 value) throws Exception {
        heartBeatInterval = value;
    }

    public Int32 getWindowSize() throws Exception {
        return windowSize;
    }

    public void setWindowSize(Int32 value) throws Exception {
        windowSize = value;
    }

    public boolean getIsPartial() throws Exception {
        return isPartial;
    }

    public void setIsPartial(boolean value) throws Exception {
        isPartial = value;
    }

    public List<Folder> getFolders() throws Exception {
        return folderList;
    }

    public ASSyncRequest() throws Exception {
        this.setCommand("Sync");
        this.folderList = new List<Folder>();
    }

    // This function generates an ASSyncResponse from an
    // HTTP response.
    protected ASCommandResponse wrapHttpResponse(HttpWebResponse httpResp) throws Exception {
        return new ASSyncResponse(httpResp,folderList);
    }

    // This function generates the XML request body
    // for the Sync request.
    protected void generateXMLPayload() throws Exception {
        // If WBXML was explicitly set, use that
        if (getWbxmlBytes() != null)
            return ;
         
        // Otherwise, use the properties to build the XML and then WBXML encode it
        XmlDocument syncXML = new XmlDocument();
        XmlDeclaration xmlDeclaration = syncXML.CreateXmlDeclaration("1.0", "utf-8", null);
        syncXML.InsertBefore(xmlDeclaration, null);
        XmlNode syncNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "Sync", Namespaces.airSyncNamespace);
        syncNode.Prefix = Xmlns.airSyncXmlns;
        syncXML.AppendChild(syncNode);
        // Only add a collections node if there are folders in the request.
        // If omitting, there should be a Partial element.
        if (folderList.Count == 0 && isPartial == false)
            throw new ArgumentException("Sync requests must specify collections or include the Partial element.");
         
        if (folderList.Count > 0)
        {
            XmlNode collectionsNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "Collections", Namespaces.airSyncNamespace);
            collectionsNode.Prefix = Xmlns.airSyncXmlns;
            syncNode.AppendChild(collectionsNode);
            for (Object __dummyForeachVar0 : folderList)
            {
                Folder folder = (Folder)__dummyForeachVar0;
                XmlNode collectionNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "Collection", Namespaces.airSyncNamespace);
                collectionNode.Prefix = Xmlns.airSyncXmlns;
                collectionsNode.AppendChild(collectionNode);
                XmlNode syncKeyNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "SyncKey", Namespaces.airSyncNamespace);
                syncKeyNode.Prefix = Xmlns.airSyncXmlns;
                syncKeyNode.InnerText = folder.getSyncKey();
                collectionNode.AppendChild(syncKeyNode);
                XmlNode collectionIdNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "CollectionId", Namespaces.airSyncNamespace);
                collectionIdNode.Prefix = Xmlns.airSyncXmlns;
                collectionIdNode.InnerText = folder.getId();
                collectionNode.AppendChild(collectionIdNode);
                // To override "ghosting", you must include a Supported element here.
                // This only applies to calendar items and contacts
                // NOT IMPLEMENTED
                // If folder is set to permanently delete items, then add a DeletesAsMoves
                // element here and set it to false.
                // Otherwise, omit. Per MS-ASCMD, the absence of this element is the same as true.
                if (folder.getAreDeletesPermanent() == true)
                {
                    XmlNode deletesAsMovesNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "DeletesAsMoves", Namespaces.airSyncNamespace);
                    deletesAsMovesNode.Prefix = Xmlns.airSyncXmlns;
                    deletesAsMovesNode.InnerText = "0";
                    collectionNode.AppendChild(deletesAsMovesNode);
                }
                 
                // In almost all cases the GetChanges element can be omitted.
                // It only makes sense to use it if SyncKey != 0 and you don't want
                // changes from the server for some reason.
                if (folder.getAreChangesIgnored() == true)
                {
                    XmlNode getChangesNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "GetChanges", Namespaces.airSyncNamespace);
                    getChangesNode.Prefix = Xmlns.airSyncXmlns;
                    getChangesNode.InnerText = "0";
                    collectionNode.AppendChild(getChangesNode);
                }
                 
                // If there's a folder-level window size, include it
                if (folder.getWindowSize() > 0)
                {
                    XmlNode folderWindowSizeNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "WindowSize", Namespaces.airSyncNamespace);
                    folderWindowSizeNode.Prefix = Xmlns.airSyncXmlns;
                    folderWindowSizeNode.InnerText = folder.getWindowSize().ToString();
                    collectionNode.AppendChild(folderWindowSizeNode);
                }
                 
                // If the folder is set to conversation mode, specify that
                if (folder.getUseConversationMode() == true)
                {
                    XmlNode conversationModeNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "ConversationMode", Namespaces.airSyncNamespace);
                    conversationModeNode.Prefix = Xmlns.airSyncXmlns;
                    conversationModeNode.InnerText = "1";
                    collectionNode.AppendChild(conversationModeNode);
                }
                 
                // Include sync options for the folder
                // Note that you can include two Options elements, but the 2nd one is for SMS
                // SMS is not implemented at this time, so we'll only include one.
                if (folder.getOptions() != null)
                {
                    folder.generateOptionsXml(collectionNode);
                }
                 
            }
        }
         
        // Include client-side changes
        // TODO: Implement client side changes on the Folder object
        //if (folder.Commands != null)
        //{
        //    folder.GenerateCommandsXml(collectionNode);
        //}
        // If a wait period was specified, include it here
        if (wait > 0)
        {
            XmlNode waitNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "Wait", Namespaces.airSyncNamespace);
            waitNode.Prefix = Xmlns.airSyncXmlns;
            waitNode.InnerText = wait.ToString();
            syncNode.AppendChild(waitNode);
        }
         
        // If a heartbeat interval period was specified, include it here
        if (heartBeatInterval > 0)
        {
            XmlNode heartBeatIntervalNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "HeartbeatInterval", Namespaces.airSyncNamespace);
            heartBeatIntervalNode.Prefix = Xmlns.airSyncXmlns;
            heartBeatIntervalNode.InnerText = heartBeatInterval.ToString();
            syncNode.AppendChild(heartBeatIntervalNode);
        }
         
        // If a windows size was specified, include it here
        if (windowSize > 0)
        {
            XmlNode windowSizeNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "WindowSize", Namespaces.airSyncNamespace);
            windowSizeNode.Prefix = Xmlns.airSyncXmlns;
            windowSizeNode.InnerText = windowSize.ToString();
            syncNode.AppendChild(windowSizeNode);
        }
         
        // If this request contains a partial list of collections, include the Partial element
        if (isPartial == true)
        {
            XmlNode partialNode = syncXML.CreateElement(Xmlns.airSyncXmlns, "Partial", Namespaces.airSyncNamespace);
            partialNode.Prefix = Xmlns.airSyncXmlns;
            syncNode.AppendChild(partialNode);
        }
         
        StringWriter stringWriter = new StringWriter();
        XmlTextWriter xmlWriter = new XmlTextWriter(stringWriter);
        xmlWriter.Formatting = Formatting.Indented;
        syncXML.WriteTo(xmlWriter);
        xmlWriter.Flush();
        setXmlString(stringWriter.ToString());
    }

}


