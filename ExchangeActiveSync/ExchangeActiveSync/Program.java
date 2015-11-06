//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:34
//

package ExchangeActiveSync;

import CS2JNet.System.StringSupport;
import ExchangeActiveSync.ASFolderSyncRequest;
import ExchangeActiveSync.ASFolderSyncResponse;
import ExchangeActiveSync.ASFolderSyncResponse.FolderSyncStatus;
import ExchangeActiveSync.ASOptionsRequest;
import ExchangeActiveSync.ASOptionsResponse;
import ExchangeActiveSync.ASProvisionRequest;
import ExchangeActiveSync.ASProvisionRequest.PolicyAcknowledgement;
import ExchangeActiveSync.ASProvisionResponse;
import ExchangeActiveSync.ASProvisionResponse.ProvisionStatus;
import ExchangeActiveSync.ASSyncRequest;
import ExchangeActiveSync.ASSyncResponse;
import ExchangeActiveSync.ASSyncResponse.SyncStatus;
import ExchangeActiveSync.BodyPreferences;
import ExchangeActiveSync.BodyType;
import ExchangeActiveSync.Device;
import ExchangeActiveSync.Folder;
import ExchangeActiveSync.FolderSyncOptions;
import ExchangeActiveSync.Program;
import ExchangeActiveSync.ServerSyncCommand;

public class Program   
{
    // Replace "mail.contoso.com" with the name of your
    // server.
    static final String activeSyncServer = "webmail.eleks.com";
    // Set to false to disable SSL
    static final boolean useSSL = true;
    // Replace "jason" with your user name
    static final String userName = "mykhaylo.kharaba";
    // Replace "P@ssw0rd!" with your password
    static final String password = "!Michael21-03";
    // Replace "contoso" with your domain name
    static final String domainName = "eleks-software";
    // Set to true to use a base64-encoded request line
    static final boolean useBase64RequestLine = false;
    // Replace this value with a local directory
    // to store mailbox contents
    static final String mailboxCacheLocation = "D:\\temp\\Mailbox";
    public static void main(String[] args) throws Exception {
        Program.Main(args);
    }

    static void Main(String[] args) throws Exception {
        NetworkCredential userCredentials = new NetworkCredential(String.Format("{0}\\{1}", domainName, userName), password);
        Console.WriteLine("Sending OPTIONS request to server...");
        ASOptionsRequest optionsRequest = new ASOptionsRequest();
        optionsRequest.setServer(activeSyncServer);
        optionsRequest.setUseSSL(useSSL);
        optionsRequest.setCredentials(userCredentials);
        // Send the OPTIONS request
        ASOptionsResponse optionsResponse = optionsRequest.getOptions();
        if (optionsResponse == null)
        {
            reportError("Unable to connect to server.");
            return ;
        }
         
        Console.WriteLine("Supported ActiveSync versions: {0}", optionsResponse.getSupportedVersions());
        Console.WriteLine("Supported ActiveSync commands: {0}\n", optionsResponse.getSupportedCommands());
        // Make sure version 14.1 is supported
        if (!StringSupport.equals(optionsResponse.getHighestSupportedVersion(), "14.1"))
        {
            reportError("+++ The server does not support ActiveSync version 14.1. +++");
            return ;
        }
         
        // Initialize the policy key
        uint policyKey = 0;
        // Provide details about the device
        Device deviceToProvision = new Device();
        deviceToProvision.setDeviceID("EX2010activesyncfoldercs");
        deviceToProvision.setDeviceType("Sample Application");
        deviceToProvision.setModel("Sample Model");
        deviceToProvision.setFriendlyName("FolderSync/Sync Example");
        deviceToProvision.setOperatingSystem("Sample OS 1.0");
        deviceToProvision.setOperatingSystemLanguage("English");
        deviceToProvision.setMobileOperator("Phone Company");
        deviceToProvision.setUserAgent("EX2010_activesyncfolder_cs_1.0");
        deviceToProvision.setPhoneNumber("425-555-1000");
        // Phase 1: Initial provision request
        // During this phase, the client requests the policy
        // and the server sends the policy details, along with a temporary
        // policy key.
        ASProvisionRequest initialProvisionRequest = new ASProvisionRequest();
        // Initialize the request with information
        // that applies to all requests.
        initialProvisionRequest.setCredentials(userCredentials);
        initialProvisionRequest.setServer(activeSyncServer);
        initialProvisionRequest.setUser(userName);
        initialProvisionRequest.setDeviceID(deviceToProvision.getDeviceID());
        initialProvisionRequest.setDeviceType(deviceToProvision.getDeviceType());
        initialProvisionRequest.setProtocolVersion("14.1");
        initialProvisionRequest.setPolicyKey(policyKey);
        initialProvisionRequest.setUseEncodedRequestLine(useBase64RequestLine);
        // Initialize the Provision command-specific
        // information.
        initialProvisionRequest.setProvisionDevice(deviceToProvision);
        Console.WriteLine("Sending initial provision request...");
        // Send the request
        ASProvisionResponse initialProvisionResponse = (ASProvisionResponse)initialProvisionRequest.getResponse();
        Console.WriteLine("Initial Provision Request:");
        Console.WriteLine(initialProvisionRequest.getXmlString());
        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
        Console.ReadLine();
        Console.WriteLine("Initial Provision Response:");
        Console.WriteLine(initialProvisionResponse.getXmlString());
        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
        Console.ReadLine();
        // Check the result of the initial request.
        // The server may have requested a remote wipe, or
        // may have returned an error.
        Console.WriteLine("Response Status: {0}", initialProvisionResponse.getStatus());
        if (initialProvisionResponse.getStatus() == (Int32)ProvisionStatus.Success)
        {
            if (initialProvisionResponse.getIsPolicyLoaded())
            {
                if (initialProvisionResponse.getPolicy().getStatus() == (Int32)ProvisionStatus.Success)
                {
                    if (initialProvisionResponse.getPolicy().getRemoteWipeRequested())
                    {
                        // The server requested a remote wipe.
                        // The client must acknowledge it.
                        Console.WriteLine("+++ The server has requested a remote wipe. +++\n");
                        ASProvisionRequest remoteWipeAcknowledgment = new ASProvisionRequest();
                        // Initialize the request with information
                        // that applies to all requests.
                        remoteWipeAcknowledgment.setCredentials(userCredentials);
                        remoteWipeAcknowledgment.setServer(activeSyncServer);
                        remoteWipeAcknowledgment.setUser(userName);
                        remoteWipeAcknowledgment.setDeviceID(deviceToProvision.getDeviceID());
                        remoteWipeAcknowledgment.setDeviceType(deviceToProvision.getDeviceType());
                        remoteWipeAcknowledgment.setProtocolVersion("14.1");
                        remoteWipeAcknowledgment.setPolicyKey(policyKey);
                        remoteWipeAcknowledgment.setUseEncodedRequestLine(useBase64RequestLine);
                        // Initialize the Provision command-specific
                        // information.
                        remoteWipeAcknowledgment.setIsRemoteWipe(true);
                        // Indicate successful wipe
                        remoteWipeAcknowledgment.setStatus(((Enum)ProvisionStatus.Success).ordinal());
                        Console.WriteLine("Sending remote wipe acknowledgment...");
                        // Send the acknowledgment
                        ASProvisionResponse remoteWipeAckResponse = (ASProvisionResponse)remoteWipeAcknowledgment.getResponse();
                        Console.WriteLine("Remote Wipe Acknowledgment Request:");
                        Console.WriteLine(remoteWipeAcknowledgment.getXmlString());
                        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                        Console.ReadLine();
                        Console.WriteLine("Remote Wipe Acknowledgment Response:");
                        Console.WriteLine(remoteWipeAckResponse.getXmlString());
                        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                        Console.ReadLine();
                        Console.WriteLine("Remote wipe acknowledgment response status: {0}", remoteWipeAckResponse.getStatus());
                    }
                    else
                    {
                        // The server has provided a policy
                        // and a temporary policy key.
                        // The client must acknowledge this policy
                        // in order to get a permanent policy
                        // key.
                        Console.WriteLine("Policy retrieved from the server.");
                        Console.WriteLine("Temporary policy key: {0}\n", initialProvisionResponse.getPolicy().getPolicyKey());
                        ASProvisionRequest policyAcknowledgment = new ASProvisionRequest();
                        // Initialize the request with information
                        // that applies to all requests.
                        policyAcknowledgment.setCredentials(userCredentials);
                        policyAcknowledgment.setServer(activeSyncServer);
                        policyAcknowledgment.setUser(userName);
                        policyAcknowledgment.setDeviceID(deviceToProvision.getDeviceID());
                        policyAcknowledgment.setDeviceType(deviceToProvision.getDeviceType());
                        policyAcknowledgment.setProtocolVersion("14.1");
                        // Set the policy key to the temporary policy key from
                        // the previous response.
                        policyAcknowledgment.setPolicyKey(initialProvisionResponse.getPolicy().getPolicyKey());
                        policyAcknowledgment.setUseEncodedRequestLine(useBase64RequestLine);
                        // Initialize the Provision command-specific
                        // information.
                        policyAcknowledgment.setIsAcknowledgement(true);
                        // Indicate successful application of the policy.
                        policyAcknowledgment.setStatus(((Enum)PolicyAcknowledgement.Success).ordinal());
                        Console.WriteLine("Sending policy acknowledgment...");
                        // Send the request
                        ASProvisionResponse policyAckResponse = (ASProvisionResponse)policyAcknowledgment.getResponse();
                        Console.WriteLine("Policy Acknowledgment Request:");
                        Console.WriteLine(policyAcknowledgment.getXmlString());
                        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                        Console.ReadLine();
                        Console.WriteLine("Policy Acknowledgment Response:");
                        Console.WriteLine(policyAckResponse.getXmlString());
                        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                        Console.ReadLine();
                        Console.WriteLine("Response status: {0}", policyAckResponse.getStatus());
                        if (policyAckResponse.getStatus() == ((Enum)ProvisionStatus.Success).ordinal() && policyAckResponse.getIsPolicyLoaded())
                        {
                            Console.WriteLine("Policy acknowledgment successful.");
                            Console.WriteLine("Permanent Policy Key: {0}", policyAckResponse.getPolicy().getPolicyKey());
                            // Save the permanent policy key for use
                            // in subsequent command requests.
                            policyKey = policyAckResponse.getPolicy().getPolicyKey();
                        }
                        else
                        {
                            Console.WriteLine("Error returned from policy acknowledgment request: {0}", policyAckResponse.getStatus());
                        } 
                    } 
                }
                else
                {
                    Console.WriteLine("Policy Error returned from initial provision request: {0}", initialProvisionResponse.getPolicy().getStatus());
                } 
            }
             
        }
        else
        {
            Console.WriteLine("Error returned from initial provision request: {0}", initialProvisionResponse.getStatus());
        } 
        // If we did not end up with a permanent policy
        // key, then exit.
        if (policyKey == 0)
        {
            Console.WriteLine("Provisioning failed, cannot continue.");
            Console.WriteLine("+++ HIT ENTER TO EXIT +++");
            Console.ReadLine();
            return ;
        }
         
        // Initialize local storage of mailbox
        Folder rootFolder = new Folder(mailboxCacheLocation);
        ASFolderSyncRequest folderSyncRequest = new ASFolderSyncRequest();
        // Initialize the request with information
        // that applies to all requests.
        folderSyncRequest.setCredentials(userCredentials);
        folderSyncRequest.setServer(activeSyncServer);
        folderSyncRequest.setUser(userName);
        folderSyncRequest.setDeviceID(deviceToProvision.getDeviceID());
        folderSyncRequest.setDeviceType(deviceToProvision.getDeviceType());
        folderSyncRequest.setProtocolVersion("14.1");
        folderSyncRequest.setPolicyKey(policyKey);
        folderSyncRequest.setUseEncodedRequestLine(useBase64RequestLine);
        // Initialize the FolderSync command-specific
        // information.
        folderSyncRequest.setSyncKey(rootFolder.getSyncKey());
        // Send the request
        Console.WriteLine("Sending FolderSync request...");
        ASFolderSyncResponse folderSyncResponse = (ASFolderSyncResponse)folderSyncRequest.getResponse();
        if (folderSyncResponse == null)
        {
            reportError("Unable to connect to server.");
            return ;
        }
         
        Console.WriteLine("FolderSync Request:");
        Console.WriteLine(folderSyncRequest.getXmlString());
        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
        Console.ReadLine();
        Console.WriteLine("FolderSync Response:");
        Console.WriteLine(folderSyncResponse.getXmlString());
        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
        Console.ReadLine();
        Console.WriteLine("Response Status: {0}", folderSyncResponse.getStatus());
        if (folderSyncResponse.getStatus() == ((Enum)FolderSyncStatus.Success).ordinal())
        {
            // Populate the folder hierarchy from the
            // response
            folderSyncResponse.updateFolderTree(rootFolder);
        }
        else
        {
            Console.WriteLine("Error returned from FolderSync request: {0}", folderSyncResponse.getStatus());
        } 
        // Check that we have subfolders and
        // that "Inbox" is one of them.
        Folder inboxFolder = null;
        for (Object __dummyForeachVar0 : rootFolder.getSubFolders())
        {
            Folder subFolder = (Folder)__dummyForeachVar0;
            if (StringSupport.equals(subFolder.getName(), "Inbox"))
            {
                inboxFolder = subFolder;
                break;
            }
             
        }
        if (inboxFolder == null)
        {
            // Folders didn't sync or somehow
            // Inbox wasn't one of them, exit.
            reportError("+++ Inbox not found in hierarchy. +++");
            return ;
        }
         
        Console.WriteLine("Inbox found, Id: {0}, Sync Key: {1}, Last sync: {2}\n", inboxFolder.getId(), inboxFolder.getSyncKey(), inboxFolder.getLastSyncTime());
        // If this is the first time syncing Inbox
        // (Sync Key = 0), then you must
        // send an initial sync request to "prime"
        // the sync state.
        if (StringSupport.equals(inboxFolder.getSyncKey(), "0"))
        {
            ASSyncRequest initialSyncRequest = new ASSyncRequest();
            // Initialize the request with information
            // that applies to all requests.
            initialSyncRequest.setCredentials(userCredentials);
            initialSyncRequest.setServer(activeSyncServer);
            initialSyncRequest.setUser(userName);
            initialSyncRequest.setDeviceID(deviceToProvision.getDeviceID());
            initialSyncRequest.setDeviceType(deviceToProvision.getDeviceType());
            initialSyncRequest.setProtocolVersion("14.1");
            initialSyncRequest.setPolicyKey(policyKey);
            initialSyncRequest.setUseEncodedRequestLine(useBase64RequestLine);
            // Initialize the Sync command-specific
            // information.
            // Add the Inbox to the folders to be synced
            initialSyncRequest.getFolders().Add(inboxFolder);
            Console.WriteLine("Sending initial Sync request to prime the sync state for Inbox...");
            // Send the request
            ASSyncResponse initialSyncResponse = (ASSyncResponse)initialSyncRequest.getResponse();
            if (initialSyncResponse == null)
            {
                reportError("Unable to connect to server.");
                return ;
            }
             
            Console.WriteLine("Response status: {0}", initialSyncResponse.getStatus());
            Console.WriteLine("Initial Sync Request:");
            Console.WriteLine(initialSyncRequest.getXmlString());
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();
            Console.WriteLine("Initial Sync Response:");
            Console.WriteLine(initialSyncResponse.getXmlString());
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();
            if (initialSyncResponse.getStatus() == ((Enum)SyncStatus.Success).ordinal())
            {
                inboxFolder.setSyncKey(initialSyncResponse.getSyncKeyForFolder(inboxFolder.getId()));
            }
            else
            {
                String errorMessage = String.Format("Error returned from empty sync reqeust: {0}", initialSyncResponse.getStatus());
                reportError(errorMessage);
                return ;
            } 
        }
         
        ASSyncRequest inboxSyncRequest = new ASSyncRequest();
        // Initialize the request with information
        // that applies to all requests.
        inboxSyncRequest.setCredentials(userCredentials);
        inboxSyncRequest.setServer(activeSyncServer);
        inboxSyncRequest.setUser(userName);
        inboxSyncRequest.setDeviceID(deviceToProvision.getDeviceID());
        inboxSyncRequest.setDeviceType(deviceToProvision.getDeviceType());
        inboxSyncRequest.setProtocolVersion("14.1");
        inboxSyncRequest.setPolicyKey(policyKey);
        inboxSyncRequest.setUseEncodedRequestLine(useBase64RequestLine);
        // Initialize the Sync command-specific
        // information.
        // Add the Inbox to the folders to be synced
        inboxSyncRequest.getFolders().Add(inboxFolder);
        // You can specify a Wait or HeartBeatInterval
        // to direct the server to delay a response
        // until something changes.
        // NOTE: Per MS-ASCMD, don't include both
        // a Wait and a HeartBeatInterval.
        // The value of HeartBeatInterval is in seconds
        inboxSyncRequest.setHeartBeatInterval(60);
        // The value of Wait is in minutes
        //inboxSyncRequest.Wait = 5;
        // You can specify a maximum number of changes
        // that the server should send in the response.
        // Uncomment the line below to specify a maximum.
        //inboxSyncRequest.WindowSize = 50;
        // You can also specify sync options on a specific
        // folder. This is useful if you are syncing
        // multiple folders in a single Sync command request
        // and need to specify different options for each.
        inboxFolder.setUseConversationMode(true);
        inboxFolder.setAreDeletesPermanent(false);
        // Further options are available in the <Options>
        // element as a child of the <Colletion> element.
        // (See MS-ASCMD section 2.2.3.115.5)
        inboxFolder.setOptions(new FolderSyncOptions());
        inboxFolder.getOptions().BodyPreference = new BodyPreferences[1];
        inboxFolder.getOptions().BodyPreference[0] = new BodyPreferences();
        inboxFolder.getOptions().BodyPreference[0].Type = BodyType.HTML;
        inboxFolder.getOptions().BodyPreference[0].TruncationSize = 500;
        inboxFolder.getOptions().BodyPreference[0].AllOrNone = false;
        inboxFolder.getOptions().BodyPreference[0].Preview = 100;
        Console.WriteLine("Sending Inbox Sync request...");
        // Send the request
        ASSyncResponse inboxSyncResponse = (ASSyncResponse)inboxSyncRequest.getResponse();
        if (inboxSyncResponse == null)
        {
            reportError("Unable to connect to server.");
            return ;
        }
         
        Console.WriteLine("Response status: {0}", inboxSyncResponse.getStatus());
        Console.WriteLine("Inbox Sync Request:");
        Console.WriteLine(inboxSyncRequest.getXmlString());
        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
        Console.ReadLine();
        Console.WriteLine("Inbox Sync Response:");
        Console.WriteLine(inboxSyncResponse.getXmlString());
        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
        Console.ReadLine();
        if (inboxSyncResponse.getStatus() == ((Enum)SyncStatus.Success).ordinal())
        {
            List<ServerSyncCommand> addCommands = inboxSyncResponse.getServerAddsForFolder(inboxFolder.getId());
            if (addCommands != null)
            {
                Console.WriteLine("Adding {0} items...", addCommands.Count);
                SaveItemsInLocalFolder(inboxFolder, addCommands);
            }
             
            inboxFolder.setSyncKey(inboxSyncResponse.getSyncKeyForFolder(inboxFolder.getId()));
            inboxFolder.setLastSyncTime(DateTime.Now);
            // Call SaveFolderInfo to update the last sync time
            // and sync key of the inbox.
            rootFolder.saveFolderInfo();
        }
        else if (inboxSyncResponse.getHttpStatus() == HttpStatusCode.OK)
        {
            // The server had no changes
            // to send. At this point you could
            // repeat the last sync request by sending
            // an empty sync request (MS-ASCMD section 2.2.2.19.1.1)
            // This sample does not implement repeating the request,
            // so just notify the user.
            Console.WriteLine("No changes available on server.");
            inboxFolder.setLastSyncTime(DateTime.Now);
            // Call SaveFolderInfo to update the last sync time
            rootFolder.saveFolderInfo();
        }
        else
        {
            Console.WriteLine("Error returned from Inbox sync request: {0}", inboxSyncResponse.getStatus());
        }  
        Console.WriteLine("+++ HIT ENTER TO EXIT +++");
        Console.ReadLine();
    }

    // This function saves new items into the
    // local storage for a folder.
    static void saveItemsInLocalFolder(Folder targetFolder, List<ServerSyncCommand> itemList) throws Exception {
        for (Object __dummyForeachVar1 : itemList)
        {
            ServerSyncCommand newItem = (ServerSyncCommand)__dummyForeachVar1;
            // Build a filename for the new item
            // that is based on server id.
            String itemFileName = newItem.getServerId();
            // Since server ids typically have a
            // ':' in them, replace with a legal character
            itemFileName = itemFileName.Replace(':', '_');
            itemFileName = itemFileName + ".xml";
            XmlWriter xmlToFile = XmlWriter.Create(targetFolder.getSaveLocation() + "\\" + itemFileName);
            newItem.getAppDataXml().WriteTo(xmlToFile);
            xmlToFile.Flush();
            xmlToFile.Close();
        }
    }

    static void reportError(String errorMessage) throws Exception {
        Console.WriteLine(errorMessage);
        Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
        Console.ReadLine();
    }

}


