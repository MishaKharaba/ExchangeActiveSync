using System;
using System.Collections.Generic;
using System.Net;
using System.Xml;

namespace ExchangeActiveSync
{
    class Program
    {
        // Replace "mail.contoso.com" with the name of your
        // server.
        const string activeSyncServer = "";
        // Set to false to disable SSL
        const bool useSSL = true;
        // Replace "jason" with your user name
        const string userName = "";
        // Replace "P@ssw0rd!" with your password
        const string password = "";
        // Replace "contoso" with your domain name
        const string domainName = "";
        // Set to true to use a base64-encoded request line
        const bool useBase64RequestLine = false;
        // Replace this value with a local directory
        // to store mailbox contents
        const string mailboxCacheLocation = @"D:\temp\Mailbox";

        static void Main(string[] args)
        {
            NetworkCredential userCredentials = new NetworkCredential(
                string.Format("{0}\\{1}", domainName, userName), password);

            #region OPTIONS Request
            Console.WriteLine("Sending OPTIONS request to server...");

            ASOptionsRequest optionsRequest = new ASOptionsRequest();
            optionsRequest.Server = activeSyncServer;
            optionsRequest.UseSSL = useSSL;
            optionsRequest.Credentials = userCredentials;

            // Send the OPTIONS request
            ASOptionsResponse optionsResponse = optionsRequest.GetOptions();
            if (optionsResponse == null)
            {
                ReportError("Unable to connect to server.");
                return;
            }

            Console.WriteLine("Supported ActiveSync versions: {0}", optionsResponse.SupportedVersions);
            Console.WriteLine("Supported ActiveSync commands: {0}\n", optionsResponse.SupportedCommands);

            // Make sure version 14.1 is supported
            if (optionsResponse.HighestSupportedVersion != "14.1")
            {
                ReportError("+++ The server does not support ActiveSync version 14.1. +++");
                return;
            }
            #endregion

            #region Provisioning
            // Initialize the policy key
            uint policyKey = 0;

            // Provide details about the device
            Device deviceToProvision = new Device();
            deviceToProvision.DeviceID = "EX2010activesyncfoldercs";
            deviceToProvision.DeviceType = "Sample Application";
            deviceToProvision.Model = "Sample Model";
            deviceToProvision.FriendlyName = "FolderSync/Sync Example";
            deviceToProvision.OperatingSystem = "Sample OS 1.0";
            deviceToProvision.OperatingSystemLanguage = "English";
            deviceToProvision.MobileOperator = "Phone Company";
            deviceToProvision.UserAgent = "EX2010_activesyncfolder_cs_1.0";
            deviceToProvision.PhoneNumber = "425-555-1000";

            // Phase 1: Initial provision request
            // During this phase, the client requests the policy
            // and the server sends the policy details, along with a temporary
            // policy key.
            ASProvisionRequest initialProvisionRequest = new ASProvisionRequest();

            // Initialize the request with information
            // that applies to all requests.
            initialProvisionRequest.Credentials = userCredentials;
            initialProvisionRequest.Server = activeSyncServer;
            initialProvisionRequest.User = userName;
            initialProvisionRequest.DeviceID = deviceToProvision.DeviceID;
            initialProvisionRequest.DeviceType = deviceToProvision.DeviceType;
            initialProvisionRequest.ProtocolVersion = "14.1";
            initialProvisionRequest.PolicyKey = policyKey;
            initialProvisionRequest.UseEncodedRequestLine = useBase64RequestLine;

            // Initialize the Provision command-specific
            // information.
            initialProvisionRequest.ProvisionDevice = deviceToProvision;

            Console.WriteLine("Sending initial provision request...");
            // Send the request
            ASProvisionResponse initialProvisionResponse =
                (ASProvisionResponse)initialProvisionRequest.GetResponse();

            Console.WriteLine("Initial Provision Request:");
            Console.WriteLine(initialProvisionRequest.XmlString);
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();

            Console.WriteLine("Initial Provision Response:");
            Console.WriteLine(initialProvisionResponse.XmlString);
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();

            // Check the result of the initial request. 
            // The server may have requested a remote wipe, or
            // may have returned an error.
            Console.WriteLine("Response Status: {0}", initialProvisionResponse.Status);

            if (initialProvisionResponse.Status == (Int32)ASProvisionResponse.ProvisionStatus.Success)
            {
                if (initialProvisionResponse.IsPolicyLoaded)
                {
                    if (initialProvisionResponse.Policy.Status ==
                        (Int32)ASProvisionResponse.ProvisionStatus.Success)
                    {
                        if (initialProvisionResponse.Policy.RemoteWipeRequested)
                        {
                            // The server requested a remote wipe.
                            // The client must acknowledge it.
                            Console.WriteLine("+++ The server has requested a remote wipe. +++\n");

                            ASProvisionRequest remoteWipeAcknowledgment =
                                new ASProvisionRequest();

                            // Initialize the request with information
                            // that applies to all requests.
                            remoteWipeAcknowledgment.Credentials = userCredentials;
                            remoteWipeAcknowledgment.Server = activeSyncServer;
                            remoteWipeAcknowledgment.User = userName;
                            remoteWipeAcknowledgment.DeviceID = deviceToProvision.DeviceID;
                            remoteWipeAcknowledgment.DeviceType = deviceToProvision.DeviceType;
                            remoteWipeAcknowledgment.ProtocolVersion = "14.1";
                            remoteWipeAcknowledgment.PolicyKey = policyKey;
                            remoteWipeAcknowledgment.UseEncodedRequestLine = useBase64RequestLine;

                            // Initialize the Provision command-specific
                            // information.
                            remoteWipeAcknowledgment.IsRemoteWipe = true;
                            // Indicate successful wipe
                            remoteWipeAcknowledgment.Status =
                                (int)ASProvisionResponse.ProvisionStatus.Success;

                            Console.WriteLine("Sending remote wipe acknowledgment...");
                            // Send the acknowledgment
                            ASProvisionResponse remoteWipeAckResponse =
                                (ASProvisionResponse)remoteWipeAcknowledgment.GetResponse();

                            Console.WriteLine("Remote Wipe Acknowledgment Request:");
                            Console.WriteLine(remoteWipeAcknowledgment.XmlString);
                            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                            Console.ReadLine();

                            Console.WriteLine("Remote Wipe Acknowledgment Response:");
                            Console.WriteLine(remoteWipeAckResponse.XmlString);
                            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                            Console.ReadLine();

                            Console.WriteLine("Remote wipe acknowledgment response status: {0}",
                                remoteWipeAckResponse.Status);
                        }
                        else
                        {
                            // The server has provided a policy
                            // and a temporary policy key.
                            // The client must acknowledge this policy
                            // in order to get a permanent policy
                            // key.
                            Console.WriteLine("Policy retrieved from the server.");
                            Console.WriteLine("Temporary policy key: {0}\n",
                                initialProvisionResponse.Policy.PolicyKey);

                            ASProvisionRequest policyAcknowledgment =
                                new ASProvisionRequest();

                            // Initialize the request with information
                            // that applies to all requests.
                            policyAcknowledgment.Credentials = userCredentials;
                            policyAcknowledgment.Server = activeSyncServer;
                            policyAcknowledgment.User = userName;
                            policyAcknowledgment.DeviceID = deviceToProvision.DeviceID;
                            policyAcknowledgment.DeviceType = deviceToProvision.DeviceType;
                            policyAcknowledgment.ProtocolVersion = "14.1";
                            // Set the policy key to the temporary policy key from
                            // the previous response.
                            policyAcknowledgment.PolicyKey = initialProvisionResponse.Policy.PolicyKey;
                            policyAcknowledgment.UseEncodedRequestLine = useBase64RequestLine;

                            // Initialize the Provision command-specific
                            // information.
                            policyAcknowledgment.IsAcknowledgement = true;
                            // Indicate successful application of the policy.
                            policyAcknowledgment.Status =
                                (int)ASProvisionRequest.PolicyAcknowledgement.Success;

                            Console.WriteLine("Sending policy acknowledgment...");
                            // Send the request
                            ASProvisionResponse policyAckResponse =
                                (ASProvisionResponse)policyAcknowledgment.GetResponse();

                            Console.WriteLine("Policy Acknowledgment Request:");
                            Console.WriteLine(policyAcknowledgment.XmlString);
                            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                            Console.ReadLine();

                            Console.WriteLine("Policy Acknowledgment Response:");
                            Console.WriteLine(policyAckResponse.XmlString);
                            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                            Console.ReadLine();

                            Console.WriteLine("Response status: {0}", policyAckResponse.Status);

                            if (policyAckResponse.Status == (int)ASProvisionResponse.ProvisionStatus.Success
                                && policyAckResponse.IsPolicyLoaded)
                            {
                                Console.WriteLine("Policy acknowledgment successful.");
                                Console.WriteLine("Permanent Policy Key: {0}", policyAckResponse.Policy.PolicyKey);

                                // Save the permanent policy key for use
                                // in subsequent command requests.
                                policyKey = policyAckResponse.Policy.PolicyKey;
                            }
                            else
                            {
                                Console.WriteLine("Error returned from policy acknowledgment request: {0}",
                                    policyAckResponse.Status);
                            }
                        }
                    }
                    else
                    {
                        Console.WriteLine("Policy Error returned from initial provision request: {0}",
                            initialProvisionResponse.Policy.Status);
                    }
                }
            }
            else
            {
                Console.WriteLine("Error returned from initial provision request: {0}",
                    initialProvisionResponse.Status);
            }

            // If we did not end up with a permanent policy
            // key, then exit.
            if (policyKey == 0)
            {
                Console.WriteLine("Provisioning failed, cannot continue.");
                Console.WriteLine("+++ HIT ENTER TO EXIT +++");
                Console.ReadLine();
                return;
            }
            #endregion

            #region Sync Folder Hierarchy
            // Initialize local storage of mailbox
            Folder rootFolder = new Folder(mailboxCacheLocation);

            ASFolderSyncRequest folderSyncRequest = new ASFolderSyncRequest();

            // Initialize the request with information
            // that applies to all requests.
            folderSyncRequest.Credentials = userCredentials;
            folderSyncRequest.Server = activeSyncServer;
            folderSyncRequest.User = userName;
            folderSyncRequest.DeviceID = deviceToProvision.DeviceID;
            folderSyncRequest.DeviceType = deviceToProvision.DeviceType;
            folderSyncRequest.ProtocolVersion = "14.1";
            folderSyncRequest.PolicyKey = policyKey;
            folderSyncRequest.UseEncodedRequestLine = useBase64RequestLine;

            // Initialize the FolderSync command-specific
            // information.
            folderSyncRequest.SyncKey = rootFolder.SyncKey;

            // Send the request
            Console.WriteLine("Sending FolderSync request...");

            ASFolderSyncResponse folderSyncResponse = (ASFolderSyncResponse)folderSyncRequest.GetResponse();

            if (folderSyncResponse == null)
            {
                ReportError("Unable to connect to server.");
                return;
            }

            Console.WriteLine("FolderSync Request:");
            Console.WriteLine(folderSyncRequest.XmlString);
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();

            Console.WriteLine("FolderSync Response:");
            Console.WriteLine(folderSyncResponse.XmlString);
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();

            Console.WriteLine("Response Status: {0}", folderSyncResponse.Status);

            if (folderSyncResponse.Status ==
                (int)ASFolderSyncResponse.FolderSyncStatus.Success)
            {
                // Populate the folder hierarchy from the 
                // response
                folderSyncResponse.UpdateFolderTree(rootFolder);
            }
            else
            {
                Console.WriteLine("Error returned from FolderSync request: {0}", folderSyncResponse.Status);
            }

            // Check that we have subfolders and
            // that "Inbox" is one of them.
            Folder inboxFolder = null;

            foreach (Folder subFolder in rootFolder.SubFolders)
            {
                if (subFolder.Name == "Inbox")
                {
                    inboxFolder = subFolder;
                    break;
                }
            }

            if (inboxFolder == null)
            {
                // Folders didn't sync or somehow
                // Inbox wasn't one of them, exit.
                ReportError("+++ Inbox not found in hierarchy. +++");
                return;
            }

            Console.WriteLine("Inbox found, Id: {0}, Sync Key: {1}, Last sync: {2}\n",
                inboxFolder.Id, inboxFolder.SyncKey, inboxFolder.LastSyncTime);
            #endregion

            #region Sync Inbox Contents
            // If this is the first time syncing Inbox
            // (Sync Key = 0), then you must
            // send an initial sync request to "prime"
            // the sync state.
            if (inboxFolder.SyncKey == "0")
            {
                ASSyncRequest initialSyncRequest = new ASSyncRequest();

                // Initialize the request with information
                // that applies to all requests.
                initialSyncRequest.Credentials = userCredentials;
                initialSyncRequest.Server = activeSyncServer;
                initialSyncRequest.User = userName;
                initialSyncRequest.DeviceID = deviceToProvision.DeviceID;
                initialSyncRequest.DeviceType = deviceToProvision.DeviceType;
                initialSyncRequest.ProtocolVersion = "14.1";
                initialSyncRequest.PolicyKey = policyKey;
                initialSyncRequest.UseEncodedRequestLine = useBase64RequestLine;

                // Initialize the Sync command-specific
                // information.
                // Add the Inbox to the folders to be synced
                initialSyncRequest.Folders.Add(inboxFolder);

                Console.WriteLine("Sending initial Sync request to prime the sync state for Inbox...");

                // Send the request
                ASSyncResponse initialSyncResponse = (ASSyncResponse)initialSyncRequest.GetResponse();
                if (initialSyncResponse == null)
                {
                    ReportError("Unable to connect to server.");
                    return;
                }

                Console.WriteLine("Response status: {0}", initialSyncResponse.Status);

                Console.WriteLine("Initial Sync Request:");
                Console.WriteLine(initialSyncRequest.XmlString);
                Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                Console.ReadLine();

                Console.WriteLine("Initial Sync Response:");
                Console.WriteLine(initialSyncResponse.XmlString);
                Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
                Console.ReadLine();

                if (initialSyncResponse.Status ==
                    (int)ASSyncResponse.SyncStatus.Success)
                {
                    inboxFolder.SyncKey = initialSyncResponse.GetSyncKeyForFolder(inboxFolder.Id);
                }
                else
                {
                    string errorMessage = string.Format("Error returned from empty sync reqeust: {0}",
                        initialSyncResponse.Status);
                    ReportError(errorMessage);
                    return;
                }
            }

            ASSyncRequest inboxSyncRequest = new ASSyncRequest();

            // Initialize the request with information
            // that applies to all requests.
            inboxSyncRequest.Credentials = userCredentials;
            inboxSyncRequest.Server = activeSyncServer;
            inboxSyncRequest.User = userName;
            inboxSyncRequest.DeviceID = deviceToProvision.DeviceID;
            inboxSyncRequest.DeviceType = deviceToProvision.DeviceType;
            inboxSyncRequest.ProtocolVersion = "14.1";
            inboxSyncRequest.PolicyKey = policyKey;
            inboxSyncRequest.UseEncodedRequestLine = useBase64RequestLine;

            // Initialize the Sync command-specific
            // information.
            // Add the Inbox to the folders to be synced
            inboxSyncRequest.Folders.Add(inboxFolder);

            // You can specify a Wait or HeartBeatInterval
            // to direct the server to delay a response
            // until something changes.
            // NOTE: Per MS-ASCMD, don't include both
            // a Wait and a HeartBeatInterval.

            // The value of HeartBeatInterval is in seconds
            inboxSyncRequest.HeartBeatInterval = 60;

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
            inboxFolder.UseConversationMode = true;
            inboxFolder.AreDeletesPermanent = false;

            // Further options are available in the <Options>
            // element as a child of the <Colletion> element.
            // (See MS-ASCMD section 2.2.3.115.5)
            inboxFolder.Options = new FolderSyncOptions();
            inboxFolder.Options.BodyPreference = new BodyPreferences[1];
            inboxFolder.Options.BodyPreference[0] = new BodyPreferences();

            inboxFolder.Options.BodyPreference[0].Type = BodyType.HTML;
            inboxFolder.Options.BodyPreference[0].TruncationSize = 500;
            inboxFolder.Options.BodyPreference[0].AllOrNone = false;
            inboxFolder.Options.BodyPreference[0].Preview = 100;

            Console.WriteLine("Sending Inbox Sync request...");

            // Send the request
            ASSyncResponse inboxSyncResponse =
                (ASSyncResponse)inboxSyncRequest.GetResponse();
            if (inboxSyncResponse == null)
            {
                ReportError("Unable to connect to server.");
                return;
            }

            Console.WriteLine("Response status: {0}", inboxSyncResponse.Status);

            Console.WriteLine("Inbox Sync Request:");
            Console.WriteLine(inboxSyncRequest.XmlString);
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();

            Console.WriteLine("Inbox Sync Response:");
            Console.WriteLine(inboxSyncResponse.XmlString);
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();

            if (inboxSyncResponse.Status ==
                (int)ASSyncResponse.SyncStatus.Success)
            {
                List<ServerSyncCommand> addCommands =
                    inboxSyncResponse.GetServerAddsForFolder(inboxFolder.Id);

                if (addCommands != null)
                {
                    Console.WriteLine("Adding {0} items...", addCommands.Count);

                    SaveItemsInLocalFolder(inboxFolder, addCommands);
                }

                inboxFolder.SyncKey = inboxSyncResponse.GetSyncKeyForFolder(inboxFolder.Id);
                inboxFolder.LastSyncTime = DateTime.Now;

                // Call SaveFolderInfo to update the last sync time
                // and sync key of the inbox.
                rootFolder.SaveFolderInfo();
            }
            else if (inboxSyncResponse.HttpStatus == HttpStatusCode.OK)
            {
                // The server had no changes
                // to send. At this point you could
                // repeat the last sync request by sending
                // an empty sync request (MS-ASCMD section 2.2.2.19.1.1)
                // This sample does not implement repeating the request,
                // so just notify the user.
                Console.WriteLine("No changes available on server.");
                inboxFolder.LastSyncTime = DateTime.Now;

                // Call SaveFolderInfo to update the last sync time
                rootFolder.SaveFolderInfo();
            }
            else
            {
                Console.WriteLine("Error returned from Inbox sync request: {0}",
                    inboxSyncResponse.Status);
            }
            #endregion

            Console.WriteLine("+++ HIT ENTER TO EXIT +++");
            Console.ReadLine();
        }

        // This function saves new items into the
        // local storage for a folder.
        static void SaveItemsInLocalFolder(Folder targetFolder, List<ServerSyncCommand> itemList)
        {
            foreach (ServerSyncCommand newItem in itemList)
            {
                // Build a filename for the new item
                // that is based on server id.
                string itemFileName = newItem.ServerId;

                // Since server ids typically have a
                // ':' in them, replace with a legal character
                itemFileName = itemFileName.Replace(':', '_');
                itemFileName = itemFileName + ".xml";

                XmlWriter xmlToFile = XmlWriter.Create(targetFolder.SaveLocation +
                    "\\" + itemFileName);

                newItem.AppDataXml.WriteTo(xmlToFile);

                xmlToFile.Flush();
                xmlToFile.Close();
            }
        }

        static void ReportError(string errorMessage)
        {
            Console.WriteLine(errorMessage);
            Console.WriteLine("+++ HIT ENTER TO CONTINUE +++");
            Console.ReadLine();
        }
    }
}
