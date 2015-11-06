using System;
using System.Collections.Generic;
using System.Net;
using System.Xml;

namespace ExchangeActiveSync
{
    // This class represents the Sync command response
    class ASSyncResponse : ASCommandResponse
    {
        // This enumeration covers the possible Status
        // values for FolderSync responses.
        public enum SyncStatus
        {
            Success = 1,
            InvalidSyncKey = 3,
            ProtocolError = 4,
            ServerError = 5,
            ClientServerConversionError = 6,
            ServerOverwriteConflict = 7,
            ObjectNotFound = 8,
            SyncCannotComplete = 9,
            FolderHierarchyOutOfDate = 12,
            PartialSyncNotValid = 13,
            InvalidDelayValue = 14,
            InvalidSync = 15,
            Retry = 16
        }

        private XmlDocument responseXml = null;
        private XmlNamespaceManager xmlNsMgr = null;
        private Int32 status = 0;
        private List<Folder> folderList = null;

        #region Property Accessors
        public Int32 Status
        {
            get
            {
                return status;
            }
        }
        #endregion

        public ASSyncResponse(HttpWebResponse httpResponse, List<Folder> folders)
            : base(httpResponse)
        {
            folderList = folders;

            // Sync responses can be empty
            if (XmlString != "")
            {
                responseXml = new XmlDocument();
                responseXml.LoadXml(XmlString);

                xmlNsMgr = new XmlNamespaceManager(responseXml.NameTable);
                xmlNsMgr.AddNamespace(Xmlns.airSyncXmlns, Namespaces.airSyncNamespace);
                xmlNsMgr.AddNamespace(Xmlns.airSyncBaseXmlns, Namespaces.airSyncBaseNamespace);

                SetStatus();
            }
        }

        // This function gets the sync key for 
        // a folder.
        public string GetSyncKeyForFolder(string folderId)
        {
            string folderSyncKey = "0";

            string collectionXPath = ".//airsync:Collection[airsync:CollectionId = \""
                + folderId + "\"]";
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
        public List<ServerSyncCommand> GetServerAddsForFolder(string folderId)
        {
            List<ServerSyncCommand> addCommands = new List<ServerSyncCommand>();

            string collectionXPath = ".//airsync:Collection[airsync:CollectionId = \"" 
                + folderId + "\"]";
            XmlNode folderNode = responseXml.SelectSingleNode(collectionXPath, xmlNsMgr);

            if (folderNode != null)
            {
                XmlNodeList addNodes = folderNode.SelectNodes(".//airsync:Add", xmlNsMgr);

                foreach (XmlNode addNode in addNodes)
                {
                    XmlNode serverIdNode = addNode.SelectSingleNode("./airsync:ServerId", xmlNsMgr);
                    XmlNode applicationDataNode = addNode.SelectSingleNode("./airsync:ApplicationData", xmlNsMgr);

                    if (serverIdNode != null && applicationDataNode != null)
                    {
                        ServerSyncCommand addCommand = new ServerSyncCommand(
                            ServerSyncCommand.ServerSyncCommandType.Add,
                            serverIdNode.InnerText,
                            applicationDataNode,
                            null);

                        addCommands.Add(addCommand);
                    }
                }
            }

            return addCommands;
        }

        // This function extracts the response status from the 
        // XML and sets the status property.
        private void SetStatus()
        {
            XmlNode syncNode = responseXml.SelectSingleNode(".//airsync:Sync", xmlNsMgr);
            XmlNode statusNode = null;
            if (syncNode != null)
                statusNode = syncNode.SelectSingleNode(".//airsync:Status", xmlNsMgr);

            if (statusNode != null)
                status = XmlConvert.ToInt32(statusNode.InnerText);
        }
    }
}
