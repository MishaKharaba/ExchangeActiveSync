using System;
using System.Net;
using System.Xml;

namespace ExchangeActiveSync
{
    // This class represents a FolderSync command
    // response as specified in MS-ASCMD section 2.2.2.4.2.
    class ASFolderSyncResponse : ASCommandResponse
    {
        // This enumeration covers the possible Status
        // values for FolderSync responses.
        public enum FolderSyncStatus
        {
            Success = 1,
            ServerError = 6,
            InvalidSyncKey = 9,
            InvalidFormat = 10,
            UnknownError = 11,
            UnknownCode = 12
        }

        private XmlDocument responseXml = null;
        private XmlNamespaceManager xmlNsMgr = null;
        private Int32 status = 0;

        #region Property Accessors
        public Int32 Status
        {
            get
            {
                return status;
            }
        }
        #endregion

        public ASFolderSyncResponse(HttpWebResponse httpResponse)
            : base(httpResponse)
        {
            responseXml = new XmlDocument();
            responseXml.LoadXml(XmlString);

            xmlNsMgr = new XmlNamespaceManager(responseXml.NameTable);
            xmlNsMgr.AddNamespace(Xmlns.folderHierarchyXmlns, Namespaces.folderHierarchyNamespace);

            SetStatus();
        }

        // This function updates a folder tree based on the
        // changes received from the server in the response.
        public void UpdateFolderTree(Folder rootFolder)
        {
            rootFolder.LastSyncTime = DateTime.Now.ToUniversalTime();

            try
            {
                // Get sync key
                XmlNode syncKeyNode = responseXml.SelectSingleNode(".//folderhierarchy:SyncKey", xmlNsMgr);
                rootFolder.SyncKey = syncKeyNode.InnerText;

                // Process adds (new folders) first
                XmlNodeList addNodes = responseXml.SelectNodes(".//folderhierarchy:Add", xmlNsMgr);
                foreach (XmlNode addNode in addNodes)
                {
                    XmlNode nameNode = addNode.SelectSingleNode(".//folderhierarchy:DisplayName", xmlNsMgr);
                    XmlNode serverIdNode = addNode.SelectSingleNode(".//folderhierarchy:ServerId", xmlNsMgr);
                    XmlNode parentIdNode = addNode.SelectSingleNode(".//folderhierarchy:ParentId", xmlNsMgr);
                    XmlNode typeNode = addNode.SelectSingleNode(".//folderhierarchy:Type", xmlNsMgr);

                    rootFolder.AddFolder(nameNode.InnerText, serverIdNode.InnerText, parentIdNode.InnerText,
                        (Folder.FolderType)XmlConvert.ToInt32(typeNode.InnerText));
                }

                // Then process deletes
                XmlNodeList deleteNodes = responseXml.SelectNodes(".//folderhierarchy:Delete", xmlNsMgr);
                foreach (XmlNode deleteNode in deleteNodes)
                {
                    XmlNode serverIdNode = deleteNode.SelectSingleNode(".//folderhierarchy:ServerId", xmlNsMgr);

                    Folder removeFolder = rootFolder.FindFolderById(serverIdNode.InnerText);
                    removeFolder.Remove();
                }

                // Finally process any updates to existing folders
                XmlNodeList updateNodes = responseXml.SelectNodes(".//folderhierarchy:Update", xmlNsMgr);
                foreach (XmlNode updateNode in updateNodes)
                {
                    XmlNode nameNode = updateNode.SelectSingleNode(".//folderhierarchy:DisplayName", xmlNsMgr);
                    XmlNode serverIdNode = updateNode.SelectSingleNode(".//folderhierarchy:ServerId", xmlNsMgr);
                    XmlNode parentIdNode = updateNode.SelectSingleNode(".//folderhierarchy:ParentId", xmlNsMgr);
                    XmlNode typeNode = updateNode.SelectSingleNode(".//folderhierarchy:Type", xmlNsMgr);

                    Folder updateFolder = rootFolder.FindFolderById(serverIdNode.InnerText);
                    Folder updateParent = rootFolder.FindFolderById(parentIdNode.InnerText);

                    updateFolder.Update(nameNode.InnerText, updateParent, 
                        (Folder.FolderType)XmlConvert.ToInt32(typeNode.InnerText));
                }
            }
            catch (Exception e)
            {
                // Rather than attempting to recover, reset sync key 
                // and empty folders. The next FolderSync should
                // re-sync folders.
                rootFolder.SyncKey = "0";
                rootFolder.RemoveAllSubFolders();
            }

            rootFolder.SaveFolderInfo();
        }

        // This function extracts the response status from the 
        // XML and sets the status property.
        private void SetStatus()
        {
            XmlNode folderSyncNode = responseXml.SelectSingleNode(".//folderhierarchy:FolderSync", xmlNsMgr);
            XmlNode statusNode = null;
            if (folderSyncNode != null)
                statusNode = folderSyncNode.SelectSingleNode(".//folderhierarchy:Status", xmlNsMgr);

            if (statusNode != null)
                status = XmlConvert.ToInt32(statusNode.InnerText);
        }
    }
}
