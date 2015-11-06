using System.Xml;

namespace ExchangeActiveSync
{
    // This class represents an <Add>, <Change>,
    // <Delete>, or <SoftDelete> node in a
    // Sync command response.
    class ServerSyncCommand
    {
        // This enumeration represents the types
        // of commands available.
        public enum ServerSyncCommandType
        {
            Invalid,
            Add,
            Change,
            Delete,
            SoftDelete
        }

        private ServerSyncCommandType type = ServerSyncCommandType.Invalid;
        private string serverId = null;
        private string itemClass = null;
        private XmlNode appDataXml = null;

        #region Property Accessors
        public ServerSyncCommandType Type
        {
            get
            {
                return type;
            }
        }

        public string ServerId
        {
            get
            {
                return serverId;
            }
        }

        public string ItemClass
        {
            get
            {
                return itemClass;
            }
        }

        public XmlNode AppDataXml
        {
            get
            {
                return appDataXml;
            }
        }
        #endregion

        public ServerSyncCommand(ServerSyncCommandType commandType, string id, XmlNode appData,
            string changedItemClass)
        {
            type = commandType;
            serverId = id;
            appDataXml = appData;
            itemClass = changedItemClass;
        }
    }
}
