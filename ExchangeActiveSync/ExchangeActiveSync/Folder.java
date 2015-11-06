//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:34
//

package ExchangeActiveSync;

import CS2JNet.System.StringSupport;
import ExchangeActiveSync.ASError;
import ExchangeActiveSync.BodyPreferences;
import ExchangeActiveSync.BodyType;
import ExchangeActiveSync.ConflictResolution;
import ExchangeActiveSync.Folder;
import ExchangeActiveSync.FolderSyncOptions;
import ExchangeActiveSync.MimeSupport;
import ExchangeActiveSync.MimeTruncationType;
import ExchangeActiveSync.Namespaces;
import ExchangeActiveSync.SyncFilterType;
import ExchangeActiveSync.Xmlns;

// This class represents a single folder
// in a mailbox. It is also used to represent
// the root of the mailbox's folder hierarchy
public class Folder   
{
    // The folder hierarchy is persisted as an XML file
    // named FolderInfo.xml.
    private static String folderInfoFileName = "FolderInfo.xml";
    // Since the root is not returned in a FolderSync response,
    // we create it with the reserved name "Mailbox".
    private static String rootFolderName = "Mailbox";
    // This is the basic XML structure used to initialize the XmlDocument
    // if the hiearchy has not been synced before.
    private static String emptyFolderTree = "<RootFolder><Path/><SyncKey/><LastSyncTime/><Folders/></RootFolder>";
    public enum FolderType
    {
        // This enumeration indicates the folder type, and is based on
        // the allowed values for the Type element specified in
        // [MS-ASCMD] section 2.2.3.170.3.
        __dummyEnum__0,
        UserGeneric,
        DefaultInbox,
        DefaultDrafts,
        DefaultDeletedItems,
        DefaultSentItems,
        DefaultOutbox,
        DefaultTasks,
        DefaultCalendar,
        DefaultContacts,
        DefaultNotes,
        DefaultJournal,
        UserMail,
        UserCalendar,
        UserContacts,
        UserTasks,
        UserJournal,
        UserNotes,
        Unknown,
        RecipientInfoCache
    }
    // The name of the folder
    private String name = "";
    // The id of the folder
    private String id = "";
    // The type of the folder
    private ExchangeActiveSync.Folder.FolderType type = ExchangeActiveSync.Folder.FolderType.Unknown;
    // The location on disk where this folder is saved
    private String saveLocation = "";
    // The Folder object that represents this folder's parent
    private Folder parentFolder = null;
    // A list of subfolders
    private List<Folder> subFolders = null;
    // The current sync key for this folder
    String syncKey = "0";
    // The last time the contents of this folder were synced
    private DateTime lastSyncTime = DateTime.MinValue;
    // Should items deleted from this folder
    // be permanently deleted?
    private boolean areDeletesPermanent = false;
    // Should changes be ignored?
    private boolean areChangesIgnored = false;
    // The max number of changes that should be
    // returned in a sync.
    private Int32 windowSize = 0;
    // Conversation mode
    private boolean useConversationMode = false;
    // Optional sync options
    private FolderSyncOptions options = null;
    // Constructor that creates a Folder object based on the basic
    // properties of the folder. This form is used when building
    // the folder hierarchy under the root.
    public Folder(String folderName, String folderId, ExchangeActiveSync.Folder.FolderType folderType, Folder parent) throws Exception {
        name = folderName;
        id = folderId;
        parentFolder = parent;
        type = folderType;
        saveLocation = parent.getSaveLocation() + "\\" + folderName;
        Directory.CreateDirectory(saveLocation);
        subFolders = new List<Folder>();
    }

    // Constructor that creates a Folder object based on a local disk
    // path. This is the form used to create a root folder for the
    // mailbox.
    public Folder(String folderPath) throws Exception {
        saveLocation = folderPath;
        subFolders = new List<Folder>();
        name = rootFolderName;
        id = "0";
        if (Directory.Exists(saveLocation))
        {
            // The directory already exists, so load
            // the subtree from FolderInfo.xml
            loadFolderInfo();
        }
        else
        {
            // The directory does not exist, create an empty one.
            Directory.CreateDirectory(saveLocation);
        } 
    }

    public String getName() throws Exception {
        return name;
    }

    public void setName(String value) throws Exception {
        name = value;
    }

    public String getId() throws Exception {
        return id;
    }

    public Folder getParentFolder() throws Exception {
        return parentFolder;
    }

    public ExchangeActiveSync.Folder.FolderType getType() throws Exception {
        return type;
    }

    public String getSaveLocation() throws Exception {
        return saveLocation;
    }

    public String getSyncKey() throws Exception {
        return syncKey;
    }

    public void setSyncKey(String value) throws Exception {
        syncKey = value;
    }

    public DateTime getLastSyncTime() throws Exception {
        return lastSyncTime;
    }

    public void setLastSyncTime(DateTime value) throws Exception {
        lastSyncTime = value;
    }

    public boolean getAreDeletesPermanent() throws Exception {
        return areDeletesPermanent;
    }

    public void setAreDeletesPermanent(boolean value) throws Exception {
        areDeletesPermanent = value;
    }

    public boolean getAreChangesIgnored() throws Exception {
        return areChangesIgnored;
    }

    public void setAreChangesIgnored(boolean value) throws Exception {
        areChangesIgnored = value;
    }

    public Int32 getWindowSize() throws Exception {
        return windowSize;
    }

    public void setWindowSize(Int32 value) throws Exception {
        windowSize = value;
    }

    public boolean getUseConversationMode() throws Exception {
        return useConversationMode;
    }

    public void setUseConversationMode(boolean value) throws Exception {
        useConversationMode = value;
    }

    public FolderSyncOptions getOptions() throws Exception {
        return options;
    }

    public void setOptions(FolderSyncOptions value) throws Exception {
        options = value;
    }

    public Folder getRootFolder() throws Exception {
        if (parentFolder != null)
            return parentFolder.getRootFolder();
        else
            return this; 
    }

    public List<Folder> getSubFolders() throws Exception {
        return subFolders;
    }

    // Searches subfolders for a folder with the given Id.
    public Folder findFolderById(String folderId) throws Exception {
        // If the requested Id is 0, return the root.
        if (StringSupport.equals(folderId, "0"))
            return this.getRootFolder();
         
        // If this folder matches the requested Id, return it.
        if (StringSupport.equals(folderId, this.id))
            return this;
         
        // If this folder has no subfolders, there is nothing
        // to search.
        if (subFolders == null)
            return null;
         
        Folder returnFolder = null;
        for (Object __dummyForeachVar0 : subFolders)
        {
            // Check each subfolder. If a subfolder has its own
            // sbufolders, recurse into them looking for a match.
            Folder subFolder = (Folder)__dummyForeachVar0;
            if (StringSupport.equals(subFolder.getId(), folderId))
            {
                returnFolder = subFolder;
                break;
            }
            else
            {
                returnFolder = subFolder.findFolderById(folderId);
                if (returnFolder != null)
                    break;
                 
            } 
        }
        return returnFolder;
    }

    // This function is used to add folders to the hierarchy from the root.
    public Folder addFolder(String folderName, String folderId, String folderParentId, ExchangeActiveSync.Folder.FolderType folderType) throws Exception {
        if (!StringSupport.equals(id, "0"))
            throw new Exception("This function is only valid on root folders.");
         
        // Check if a folder with this ID already exists
        Folder existingFolder = findFolderById(folderId);
        if (existingFolder != null)
        {
            // If a folder with that Id, name, parent, and type already exist,
            // just return it instead of creating a new one.
            if (StringSupport.equals(existingFolder.getName(), folderName) && StringSupport.equals(existingFolder.getParentFolder().getId(), folderParentId) && existingFolder.getType() == folderType)
                return existingFolder;
            else
            {
                // There should never be two folders with the same Id, so
                // throw an exception.
                String exceptionString = String.Format("A folder with the Id = {0} already exists. Name = {1}, ParentId = {2}, Type = {3}.", folderId, existingFolder.getName(), existingFolder.getParentFolder().getId(), existingFolder.getType());
                throw new ArgumentException(exceptionString);
            } 
        }
         
        // The folder does not already exist, so find the parent.
        Folder parentFolder = findFolderById(folderParentId);
        if (parentFolder == null)
            throw new Exception(String.Format("Invalid parent folder Id: {0}", folderParentId));
         
        return parentFolder.addSubFolder(folderName,folderId,folderType);
    }

    // Add a new subfolder to the parent.
    // This function creates a new Folder object and adds it to the list of subfolders.
    public Folder addSubFolder(String folderName, String folderId, ExchangeActiveSync.Folder.FolderType folderType) throws Exception {
        Folder newFolder = new Folder(folderName,folderId,folderType,this);
        subFolders.Add(newFolder);
        return newFolder;
    }

    // This function adds a Folder object to the list of subfolders.
    public void addSubFolder(Folder newFolder) throws Exception {
        subFolders.Add(newFolder);
    }

    // This function ensures that the options member
    // is initialized.
    public void ensureOptions() throws Exception {
        if (options == null)
            options = new FolderSyncOptions();
         
    }

    // This function takes an XmlNode object that represents
    // a <Folder> element and adds a subfolder to the hierarchy
    // based on the values contained in the element.
    public void addSubFolderFromXml(XmlNode folderNode) throws Exception {
        Folder newFolder = null;
        // Load the name, id, and type.
        XmlNode nameNode = folderNode.SelectSingleNode("./Name");
        XmlNode idNode = folderNode.SelectSingleNode("./Id");
        XmlNode typeNode = folderNode.SelectSingleNode("./Type");
        if (nameNode != null && !StringSupport.equals(nameNode.InnerText, "") && idNode != null && !StringSupport.equals(idNode.InnerText, "") && typeNode != null && !StringSupport.equals(typeNode.InnerText, ""))
        {
            // If the values are all present, add the subfolder.
            newFolder = this.AddSubFolder(nameNode.InnerText, idNode.InnerText, (ExchangeActiveSync.Folder.FolderType)XmlConvert.ToInt32(typeNode.InnerText));
            // Load the current sync key
            XmlNode syncKeyNode = folderNode.SelectSingleNode("./SyncKey");
            if (syncKeyNode != null && !StringSupport.equals(syncKeyNode.InnerText, ""))
                newFolder.setSyncKey(syncKeyNode.InnerText);
             
            // Load the last sync time
            XmlNode lastSyncTimeNode = folderNode.SelectSingleNode("./LastSyncTime");
            if (lastSyncTimeNode != null && !StringSupport.equals(lastSyncTimeNode.InnerText, ""))
                newFolder.setLastSyncTime(XmlConvert.ToDateTime(lastSyncTimeNode.InnerText, XmlDateTimeSerializationMode.Utc));
             
            // Load any options
            XmlNode permanentDeleteNode = folderNode.SelectSingleNode("./PermanentDelete");
            if (permanentDeleteNode != null)
                newFolder.setAreDeletesPermanent(true);
             
            XmlNode ignoreServerChangesNode = folderNode.SelectSingleNode("./IgnoreServerChanges");
            if (ignoreServerChangesNode != null)
                newFolder.setAreChangesIgnored(true);
             
            XmlNode conversationModeNode = folderNode.SelectSingleNode("./ConversationMode");
            if (conversationModeNode != null)
                newFolder.setUseConversationMode(true);
             
            XmlNode windowSizeNode = folderNode.SelectSingleNode("./WindowSize");
            if (windowSizeNode != null)
            {
                newFolder.setWindowSize(XmlConvert.ToInt32(windowSizeNode.InnerText));
            }
             
            XmlNode filterTypeNode = folderNode.SelectSingleNode("./FilterType");
            if (filterTypeNode != null)
            {
                newFolder.ensureOptions();
                newFolder.options.FilterType = (SyncFilterType)XmlConvert.ToInt32(filterTypeNode.InnerText);
            }
             
            XmlNode conflictHandlingNode = folderNode.SelectSingleNode("./ConflictHandling");
            if (conflictHandlingNode != null)
            {
                newFolder.ensureOptions();
                newFolder.options.ConflictHandling = (ConflictResolution)XmlConvert.ToInt32(conflictHandlingNode.InnerText);
            }
             
            XmlNode mimeTruncationNode = folderNode.SelectSingleNode("./MimeTruncation");
            if (mimeTruncationNode != null)
            {
                newFolder.ensureOptions();
                newFolder.options.MimeTruncation = (MimeTruncationType)XmlConvert.ToInt32(mimeTruncationNode.InnerText);
            }
             
            XmlNode classNode = folderNode.SelectSingleNode("./ClassToSync");
            if (classNode != null)
            {
                newFolder.ensureOptions();
                newFolder.options.Class = classNode.InnerText;
            }
             
            XmlNode maxItemsNode = folderNode.SelectSingleNode("./MaxItems");
            if (maxItemsNode != null)
            {
                newFolder.ensureOptions();
                newFolder.options.MaxItems = XmlConvert.ToInt32(maxItemsNode.InnerText);
            }
             
            XmlNode bodyPreferencesNode = folderNode.SelectSingleNode("./BodyPreferences");
            if (bodyPreferencesNode != null)
            {
                newFolder.ensureOptions();
                if (newFolder.options.BodyPreference == null)
                {
                    newFolder.options.BodyPreference = new BodyPreferences[1];
                    newFolder.options.BodyPreference[0] = new BodyPreferences();
                }
                 
                newFolder.options.BodyPreference[0].Type = (BodyType)XmlConvert.ToInt32(bodyPreferencesNode.InnerText);
                XmlNode truncationSizeAttribute = bodyPreferencesNode.SelectSingleNode("@TruncationSize");
                if (truncationSizeAttribute != null)
                {
                    newFolder.options.BodyPreference[0].TruncationSize = XmlConvert.ToUInt32(truncationSizeAttribute.InnerText);
                }
                 
                XmlNode allOrNoneAttribute = bodyPreferencesNode.SelectSingleNode("@AllOrNone");
                if (allOrNoneAttribute != null)
                {
                    newFolder.options.BodyPreference[0].AllOrNone = true;
                }
                 
                XmlNode previewSizeAttribute = bodyPreferencesNode.SelectSingleNode("@PreviewSize");
                if (previewSizeAttribute != null)
                {
                    newFolder.options.BodyPreference[0].Preview = XmlConvert.ToInt32(previewSizeAttribute.InnerText);
                }
                 
            }
             
            XmlNode bodyPartPreferencesNode = folderNode.SelectSingleNode("./BodyPartPreferences");
            if (bodyPartPreferencesNode != null)
            {
                newFolder.ensureOptions();
                if (newFolder.options.BodyPartPreference == null)
                    newFolder.options.BodyPartPreference = new BodyPreferences();
                 
                newFolder.options.BodyPartPreference.Type = (BodyType)XmlConvert.ToInt32(bodyPartPreferencesNode.InnerText);
                XmlNode truncationSizeAttribute = bodyPartPreferencesNode.SelectSingleNode("@TruncationSize");
                if (truncationSizeAttribute != null)
                {
                    newFolder.options.BodyPartPreference.TruncationSize = XmlConvert.ToUInt32(truncationSizeAttribute.InnerText);
                }
                 
                XmlNode allOrNoneAttribute = bodyPartPreferencesNode.SelectSingleNode("@AllOrNone");
                if (allOrNoneAttribute != null)
                {
                    newFolder.options.BodyPartPreference.AllOrNone = true;
                }
                 
                XmlNode previewSizeAttribute = bodyPartPreferencesNode.SelectSingleNode("@PreviewSize");
                if (previewSizeAttribute != null)
                {
                    newFolder.options.BodyPartPreference.Preview = XmlConvert.ToInt32(previewSizeAttribute.InnerText);
                }
                 
            }
             
            XmlNode rightsManagementSupportedNode = folderNode.SelectSingleNode("./RightsManagementSupported");
            if (rightsManagementSupportedNode != null)
            {
                newFolder.ensureOptions();
                newFolder.options.IsRightsManagementSupported = true;
            }
             
            XmlNode mimeSupportNode = folderNode.SelectSingleNode("./MimeSupport");
            if (mimeSupportNode != null)
            {
                newFolder.ensureOptions();
                newFolder.options.MimeSupportLevel = (MimeSupport)XmlConvert.ToInt32(mimeSupportNode.InnerText);
            }
             
            // If there are any subfolders, load them
            XmlNode subFoldersNode = folderNode.SelectSingleNode("./Folders");
            if (subFoldersNode != null)
            {
                XmlNodeList subFoldersList = subFoldersNode.SelectNodes("./Folder");
                for (Object __dummyForeachVar1 : subFoldersList)
                {
                    XmlNode subFolderNode = (XmlNode)__dummyForeachVar1;
                    newFolder.addSubFolderFromXml(subFolderNode);
                }
            }
             
        }
         
    }

    // This function removes a folders local storage and
    // removes the folder from the parent's subfolders.
    public boolean remove() throws Exception {
        if (parentFolder != null)
        {
            Directory.Delete(saveLocation, true);
            return parentFolder.removeSubFolder(this);
        }
         
        return false;
    }

    // This function removes a folder from the list of
    // subfolders.
    public boolean removeSubFolder(Folder removeFolder) throws Exception {
        return subFolders.Remove(removeFolder);
    }

    // This function removes all subfolders
    public void removeAllSubFolders() throws Exception {
        for (Object __dummyForeachVar2 : subFolders.ToList())
        {
            Folder subFolder = (Folder)__dummyForeachVar2;
            subFolder.remove();
        }
    }

    // This function updates an existing folder by either
    // renaming it or moving it.
    public void update(String folderName, Folder newParent, ExchangeActiveSync.Folder.FolderType folderType) throws Exception {
        // Set the new name
        name = folderName;
        // Check to see if we are moving the folder.
        if (newParent != parentFolder)
        {
            // Remove this folder from the current parent's
            // subfolders
            parentFolder.removeSubFolder(this);
            parentFolder = newParent;
            // Add this folder to the new parent's subfolders
            newParent.addSubFolder(this);
        }
         
        // Call MoveLocalFolder to move the local data to a new
        // directory.
        moveLocalFolder(newParent.getSaveLocation() + "\\" + name);
    }

    // This function moves local storage to a new directory.
    public void moveLocalFolder(String destination) throws Exception {
        try
        {
            Directory.Move(saveLocation, destination);
        }
        catch (Exception e)
        {
            ASError.reportException(e);
        }

        saveLocation = destination;
    }

    // This function is used to load a local folder hierarchy from
    // an Xml file saved in the root mailbox directory.
    public void loadFolderInfo() throws Exception {
        if (!StringSupport.equals(this.id, "0"))
            throw new Exception("This method is only valid on the root folder.");
         
        XmlDocument folderInfo = new XmlDocument();
        try
        {
            folderInfo.Load(this.saveLocation + "\\" + folderInfoFileName);
        }
        catch (System.IO.FileNotFoundException __dummyCatchVar0)
        {
            // If the folder info XML was not found, create
            // an empty one.
            folderInfo.LoadXml(emptyFolderTree);
        }

        // Load the sync key for the hierarchy. If it is not present,
        // set to 0.
        XmlNode syncKeyNode = folderInfo.SelectSingleNode(".//SyncKey");
        if (syncKeyNode != null)
        {
            if (!StringSupport.equals(syncKeyNode.InnerText, ""))
                this.syncKey = syncKeyNode.InnerText;
            else
                this.syncKey = "0"; 
        }
         
        // Load the last sync time for the hierarchy.
        XmlNode lastSyncTimeNode = folderInfo.SelectSingleNode(".//LastSyncTime");
        if (lastSyncTimeNode != null)
        {
            if (!StringSupport.equals(lastSyncTimeNode.InnerText, ""))
                lastSyncTime = XmlConvert.ToDateTime(lastSyncTimeNode.InnerText, XmlDateTimeSerializationMode.Utc);
            else
                lastSyncTime = DateTime.MinValue; 
        }
         
        // Load any subfolders
        XmlNode subFoldersNode = folderInfo.SelectSingleNode(".//Folders");
        if (subFoldersNode != null)
        {
            XmlNodeList subFoldersList = subFoldersNode.SelectNodes("./Folder");
            for (Object __dummyForeachVar3 : subFoldersList)
            {
                XmlNode folderNode = (XmlNode)__dummyForeachVar3;
                addSubFolderFromXml(folderNode);
            }
        }
         
    }

    // This function saves the current folder hierarchy to a local Xml file.
    public void saveFolderInfo() throws Exception {
        if (!StringSupport.equals(this.id, "0"))
            throw new Exception("This method is only valid on the root folder.");
         
        XmlDocument folderInfo = new XmlDocument();
        XmlNode rootNode = folderInfo.CreateElement("RootFolder");
        folderInfo.AppendChild(rootNode);
        XmlNode pathNode = folderInfo.CreateElement("Path");
        pathNode.InnerText = this.saveLocation;
        rootNode.AppendChild(pathNode);
        XmlNode syncKeyNode = folderInfo.CreateElement("SyncKey");
        syncKeyNode.InnerText = this.syncKey;
        rootNode.AppendChild(syncKeyNode);
        XmlNode lastSyncTimeNode = folderInfo.CreateElement("LastSyncTime");
        lastSyncTimeNode.InnerText = XmlConvert.ToString(this.lastSyncTime, XmlDateTimeSerializationMode.Utc);
        rootNode.AppendChild(lastSyncTimeNode);
        XmlNode foldersNode = folderInfo.CreateElement("Folders");
        rootNode.AppendChild(foldersNode);
        for (Object __dummyForeachVar4 : this.subFolders)
        {
            Folder subFolder = (Folder)__dummyForeachVar4;
            subFolder.generateXmlNode(foldersNode);
        }
        folderInfo.Save(this.saveLocation + "\\" + folderInfoFileName);
    }

    // This function generates an XmlNode for the <Folder> element
    // for this folder.
    private void generateXmlNode(XmlNode parentNode) throws Exception {
        XmlNode folderNode = parentNode.OwnerDocument.CreateElement("Folder");
        parentNode.AppendChild(folderNode);
        XmlNode nameNode = parentNode.OwnerDocument.CreateElement("Name");
        nameNode.InnerText = this.name;
        folderNode.AppendChild(nameNode);
        XmlNode idNode = parentNode.OwnerDocument.CreateElement("Id");
        idNode.InnerText = this.id;
        folderNode.AppendChild(idNode);
        XmlNode typeNode = parentNode.OwnerDocument.CreateElement("Type");
        Int32 typeAsInt = (Int32)this.type;
        typeNode.InnerText = typeAsInt.ToString();
        folderNode.AppendChild(typeNode);
        XmlNode syncKeyNode = parentNode.OwnerDocument.CreateElement("SyncKey");
        syncKeyNode.InnerText = this.syncKey;
        folderNode.AppendChild(syncKeyNode);
        XmlNode lastSyncTimeNode = parentNode.OwnerDocument.CreateElement("LastSyncTime");
        lastSyncTimeNode.InnerText = XmlConvert.ToString(this.lastSyncTime, XmlDateTimeSerializationMode.Utc);
        folderNode.AppendChild(lastSyncTimeNode);
        if (this.areDeletesPermanent == true)
        {
            XmlNode permanentDeleteNode = parentNode.OwnerDocument.CreateElement("PermanentDelete");
            folderNode.AppendChild(permanentDeleteNode);
        }
         
        if (this.areChangesIgnored == true)
        {
            XmlNode ignoreServerChangesNode = parentNode.OwnerDocument.CreateElement("IgnoreServerChanges");
            folderNode.AppendChild(ignoreServerChangesNode);
        }
         
        if (this.useConversationMode == true)
        {
            XmlNode conversationModeNode = parentNode.OwnerDocument.CreateElement("ConversationMode");
            folderNode.AppendChild(conversationModeNode);
        }
         
        if (this.windowSize > 0)
        {
            XmlNode windowSizeNode = parentNode.OwnerDocument.CreateElement("WindowSize");
            windowSizeNode.InnerText = this.windowSize.ToString();
            folderNode.AppendChild(windowSizeNode);
        }
         
        if (this.options != null)
        {
            if (this.options.FilterType != SyncFilterType.NoFilter)
            {
                XmlNode filterTypeNode = parentNode.OwnerDocument.CreateElement("FilterType");
                Int32 filterTypeAsInteger = (Int32)this.options.FilterType;
                filterTypeNode.InnerText = filterTypeAsInteger.ToString();
                folderNode.AppendChild(filterTypeNode);
            }
             
            if (this.options.ConflictHandling != ConflictResolution.LetServerDecide)
            {
                XmlNode conflictHandlingNode = parentNode.OwnerDocument.CreateElement("ConflictHandling");
                Int32 conflictHandlingAsInteger = (Int32)this.options.ConflictHandling;
                conflictHandlingNode.InnerText = conflictHandlingAsInteger.ToString();
                folderNode.AppendChild(conflictHandlingNode);
            }
             
            if (this.options.MimeTruncation != MimeTruncationType.NoTruncate)
            {
                XmlNode mimeTruncationNode = parentNode.OwnerDocument.CreateElement("MimeTruncation");
                Int32 mimeTruncationAsInteger = (Int32)this.options.MimeTruncation;
                mimeTruncationNode.InnerText = mimeTruncationAsInteger.ToString();
                folderNode.AppendChild(mimeTruncationNode);
            }
             
            if (this.options.Class != null)
            {
                XmlNode classToSyncNode = parentNode.OwnerDocument.CreateElement("ClassToSync");
                classToSyncNode.InnerText = this.options.Class;
                folderNode.AppendChild(classToSyncNode);
            }
             
            if (this.options.MaxItems > -1)
            {
                XmlNode maxItemsNode = parentNode.OwnerDocument.CreateElement("MaxItems");
                maxItemsNode.InnerText = this.options.MaxItems.ToString();
                folderNode.AppendChild(maxItemsNode);
            }
             
            if (this.options.BodyPreference != null && this.options.BodyPreference[0] != null)
            {
                XmlNode bodyPreferencesNode = parentNode.OwnerDocument.CreateElement("BodyPreferences");
                Int32 typeAsInteger = (Int32)this.options.BodyPreference[0].Type;
                bodyPreferencesNode.InnerText = typeAsInteger.ToString();
                folderNode.AppendChild(bodyPreferencesNode);
                if (this.options.BodyPreference[0].TruncationSize > 0)
                {
                    XmlAttribute truncationSizeAttribute = parentNode.OwnerDocument.CreateAttribute("TruncationSize");
                    truncationSizeAttribute.InnerText = this.options.BodyPreference[0].TruncationSize.ToString();
                    ((XmlElement)bodyPreferencesNode).SetAttributeNode(truncationSizeAttribute);
                }
                 
                if (this.options.BodyPreference[0].AllOrNone == true)
                {
                    XmlAttribute allOrNoneAttribute = parentNode.OwnerDocument.CreateAttribute("AllOrNone");
                    allOrNoneAttribute.InnerText = "1";
                    ((XmlElement)bodyPreferencesNode).SetAttributeNode(allOrNoneAttribute);
                }
                 
                if (this.options.BodyPreference[0].Preview > -1)
                {
                    XmlAttribute previewSizeNode = parentNode.OwnerDocument.CreateAttribute("PreviewSize");
                    previewSizeNode.InnerText = this.options.BodyPreference[0].Preview.ToString();
                    ((XmlElement)bodyPreferencesNode).SetAttributeNode(previewSizeNode);
                }
                 
            }
             
            if (this.options.BodyPartPreference != null)
            {
                XmlNode bodyPartPreferencesNode = parentNode.OwnerDocument.CreateElement("BodyPartPreferences");
                Int32 typeAsInteger = (Int32)this.options.BodyPartPreference.Type;
                bodyPartPreferencesNode.InnerText = typeAsInteger.ToString();
                folderNode.AppendChild(bodyPartPreferencesNode);
                if (this.options.BodyPartPreference.TruncationSize > 0)
                {
                    XmlAttribute truncationSizeAttribute = parentNode.OwnerDocument.CreateAttribute("TruncationSize");
                    truncationSizeAttribute.InnerText = this.options.BodyPartPreference.TruncationSize.ToString();
                    ((XmlElement)bodyPartPreferencesNode).AppendChild(truncationSizeAttribute);
                }
                 
                if (this.options.BodyPartPreference.AllOrNone == true)
                {
                    XmlAttribute allOrNoneAttribute = parentNode.OwnerDocument.CreateAttribute("AllOrNone");
                    ((XmlElement)bodyPartPreferencesNode).AppendChild(allOrNoneAttribute);
                }
                 
                if (this.options.BodyPartPreference.Preview > -1)
                {
                    XmlAttribute previewSizeNode = parentNode.OwnerDocument.CreateAttribute("PreviewSize");
                    previewSizeNode.InnerText = this.options.BodyPartPreference.Preview.ToString();
                    ((XmlElement)bodyPartPreferencesNode).AppendChild(previewSizeNode);
                }
                 
            }
             
            if (this.options.IsRightsManagementSupported == true)
            {
                XmlNode rightsManagmenetSupportedNode = parentNode.OwnerDocument.CreateElement("RightsManagementSupported");
                folderNode.AppendChild(rightsManagmenetSupportedNode);
            }
             
            if (this.options.MimeSupportLevel != MimeSupport.NeverSendMime)
            {
                XmlNode mimeSupportNode = parentNode.OwnerDocument.CreateElement("MimeSupport");
                Int32 mimeSupportLevelAsInteger = (Int32)this.options.MimeSupportLevel;
                mimeSupportNode.InnerText = mimeSupportLevelAsInteger.ToString();
                folderNode.AppendChild(mimeSupportNode);
            }
             
        }
         
        XmlNode foldersNode = parentNode.OwnerDocument.CreateElement("Folders");
        folderNode.AppendChild(foldersNode);
        for (Object __dummyForeachVar5 : this.subFolders)
        {
            Folder subFolder = (Folder)__dummyForeachVar5;
            subFolder.generateXmlNode(foldersNode);
        }
    }

    // This function generates an <Options> node for a Sync
    // command based on the settings for this folder.
    public void generateOptionsXml(XmlNode rootNode) throws Exception {
        XmlNode optionsNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncXmlns, "Options", Namespaces.airSyncNamespace);
        optionsNode.Prefix = Xmlns.airSyncXmlns;
        rootNode.AppendChild(optionsNode);
        if (this.options.FilterType != SyncFilterType.NoFilter)
        {
            XmlNode filterTypeNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncXmlns, "FilterType", Namespaces.airSyncNamespace);
            filterTypeNode.Prefix = Xmlns.airSyncXmlns;
            Int32 filterTypeAsInteger = (Int32)this.options.FilterType;
            filterTypeNode.InnerText = filterTypeAsInteger.ToString();
            optionsNode.AppendChild(filterTypeNode);
        }
         
        if (this.options.Class != null)
        {
            XmlNode classNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncXmlns, "Class", Namespaces.airSyncNamespace);
            classNode.Prefix = Xmlns.airSyncXmlns;
            classNode.InnerText = this.options.Class;
            optionsNode.AppendChild(classNode);
        }
         
        if (this.options.BodyPreference != null && this.options.BodyPreference[0] != null)
        {
            XmlNode bodyPreferenceNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "BodyPreference", Namespaces.airSyncBaseNamespace);
            bodyPreferenceNode.Prefix = Xmlns.airSyncBaseXmlns;
            optionsNode.AppendChild(bodyPreferenceNode);
            XmlNode typeNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "Type", Namespaces.airSyncBaseNamespace);
            typeNode.Prefix = Xmlns.airSyncBaseXmlns;
            Int32 typeAsInteger = (Int32)this.options.BodyPreference[0].Type;
            typeNode.InnerText = typeAsInteger.ToString();
            bodyPreferenceNode.AppendChild(typeNode);
            if (this.options.BodyPreference[0].TruncationSize > 0)
            {
                XmlNode truncationSizeNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "TruncationSize", Namespaces.airSyncBaseNamespace);
                truncationSizeNode.Prefix = Xmlns.airSyncBaseXmlns;
                truncationSizeNode.InnerText = this.options.BodyPreference[0].TruncationSize.ToString();
                bodyPreferenceNode.AppendChild(truncationSizeNode);
            }
             
            if (this.options.BodyPreference[0].AllOrNone == true)
            {
                XmlNode allOrNoneNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "AllOrNone", Namespaces.airSyncBaseNamespace);
                allOrNoneNode.Prefix = Xmlns.airSyncBaseXmlns;
                allOrNoneNode.InnerText = "1";
                bodyPreferenceNode.AppendChild(allOrNoneNode);
            }
             
            if (this.options.BodyPreference[0].Preview > -1)
            {
                XmlNode previewNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "Preview", Namespaces.airSyncBaseNamespace);
                previewNode.Prefix = Xmlns.airSyncBaseXmlns;
                previewNode.InnerText = this.options.BodyPreference[0].Preview.ToString();
                bodyPreferenceNode.AppendChild(previewNode);
            }
             
        }
         
        if (this.options.BodyPartPreference != null)
        {
            XmlNode bodyPartPreferenceNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "BodyPartPreference", Namespaces.airSyncBaseNamespace);
            bodyPartPreferenceNode.Prefix = Xmlns.airSyncBaseXmlns;
            optionsNode.AppendChild(bodyPartPreferenceNode);
            XmlNode typeNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "Type", Namespaces.airSyncBaseNamespace);
            typeNode.Prefix = Xmlns.airSyncBaseXmlns;
            Int32 typeAsInteger = (Int32)this.options.BodyPartPreference.Type;
            typeNode.InnerText = typeAsInteger.ToString();
            bodyPartPreferenceNode.AppendChild(typeNode);
            if (this.options.BodyPartPreference.TruncationSize > 0)
            {
                XmlNode truncationSizeNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "TruncationSize", Namespaces.airSyncBaseNamespace);
                truncationSizeNode.Prefix = Xmlns.airSyncBaseXmlns;
                truncationSizeNode.InnerText = this.options.BodyPreference[0].TruncationSize.ToString();
                bodyPartPreferenceNode.AppendChild(truncationSizeNode);
            }
             
            if (this.options.BodyPartPreference.AllOrNone == true)
            {
                XmlNode allOrNoneNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "AllOrNone", Namespaces.airSyncBaseNamespace);
                allOrNoneNode.Prefix = Xmlns.airSyncBaseXmlns;
                allOrNoneNode.InnerText = "1";
                bodyPartPreferenceNode.AppendChild(allOrNoneNode);
            }
             
            if (this.options.BodyPartPreference.Preview > -1)
            {
                XmlNode previewNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncBaseXmlns, "Preview", Namespaces.airSyncBaseNamespace);
                previewNode.Prefix = Xmlns.airSyncBaseXmlns;
                previewNode.InnerText = this.options.BodyPreference[0].Preview.ToString();
                bodyPartPreferenceNode.AppendChild(previewNode);
            }
             
        }
         
        if (this.options.MimeSupportLevel != MimeSupport.NeverSendMime)
        {
            XmlNode mimeSupportNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncXmlns, "MIMESupport", Namespaces.airSyncNamespace);
            mimeSupportNode.Prefix = Xmlns.airSyncXmlns;
            Int32 mimeSupportLevelAsInteger = (Int32)this.options.MimeSupportLevel;
            mimeSupportNode.InnerText = mimeSupportLevelAsInteger.ToString();
            optionsNode.AppendChild(mimeSupportNode);
        }
         
        if (this.options.MimeTruncation != MimeTruncationType.NoTruncate)
        {
            XmlNode mimeTruncationNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncXmlns, "MIMETruncation", Namespaces.airSyncNamespace);
            mimeTruncationNode.Prefix = Xmlns.airSyncXmlns;
            Int32 mimeTruncationAsInteger = (Int32)this.options.MimeTruncation;
            mimeTruncationNode.InnerText = mimeTruncationAsInteger.ToString();
            optionsNode.AppendChild(mimeTruncationNode);
        }
         
        if (this.options.MaxItems > -1)
        {
            XmlNode maxItemsNode = rootNode.OwnerDocument.CreateElement(Xmlns.airSyncXmlns, "MaxItems", Namespaces.airSyncNamespace);
            maxItemsNode.Prefix = Xmlns.airSyncXmlns;
            maxItemsNode.InnerText = this.options.MaxItems.ToString();
            optionsNode.AppendChild(maxItemsNode);
        }
         
        if (this.options.IsRightsManagementSupported == true)
        {
            XmlNode rightsManagementSupportNode = rootNode.OwnerDocument.CreateElement(Xmlns.rightsManagementXmlns, "RightsManagementSupport", Namespaces.rightsManagementNamespace);
            rightsManagementSupportNode.Prefix = Xmlns.rightsManagementXmlns;
            rightsManagementSupportNode.InnerText = "1";
            optionsNode.AppendChild(rightsManagementSupportNode);
        }
         
    }

}


