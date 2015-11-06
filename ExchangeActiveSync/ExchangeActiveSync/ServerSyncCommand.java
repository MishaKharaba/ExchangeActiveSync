//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:34
//

package ExchangeActiveSync;


// This class represents an <Add>, <Change>,
// <Delete>, or <SoftDelete> node in a
// Sync command response.
public class ServerSyncCommand   
{
    public enum ServerSyncCommandType
    {
        // This enumeration represents the types
        // of commands available.
        Invalid,
        Add,
        Change,
        Delete,
        SoftDelete
    }
    private ServerSyncCommandType type = ServerSyncCommandType.Invalid;
    private String serverId = null;
    private String itemClass = null;
    private XmlNode appDataXml = null;
    public ServerSyncCommandType getType() throws Exception {
        return type;
    }

    public String getServerId() throws Exception {
        return serverId;
    }

    public String getItemClass() throws Exception {
        return itemClass;
    }

    public XmlNode getAppDataXml() throws Exception {
        return appDataXml;
    }

    public ServerSyncCommand(ServerSyncCommandType commandType, String id, XmlNode appData, String changedItemClass) throws Exception {
        type = commandType;
        serverId = id;
        appDataXml = appData;
        itemClass = changedItemClass;
    }

}


