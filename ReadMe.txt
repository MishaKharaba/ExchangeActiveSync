Exchange ActiveSync Folder Sync Example Readme
This sample shows you how to use the Microsoft Exchange Server protocol documentation (http://go.microsoft.com/fwlink/?LinkId=117318) to download the items in a user's inbox.

Exchange ActiveSync folder sync sample
This sample extends the sample code from the Implementing an Exchange ActiveSync client: the transport mechanism (http://msdn.microsoft.com/en-us/library/hh361570(EXCHG.140).aspx) sample, and the Implementing an Exchange ActiveSync client: provisioning (http://msdn.microsoft.com/en-us/library/hh531590(EXCHG.140).aspx) sample to add the following functionality:
* The ability to synchronize additions, deletes, and updates to the folder hierarchy from the server using the FolderSync command.
* The ability to synchronize additions to the contents of a folder from the server using the Sync command.
This sample uses information in the [MS-ASCMD]: ActiveSync Command Reference Protocol Specification (http://msdn.microsoft.com/en-us/library/dd299441(EXCHG.80).aspx) and the [MS-ASAIRS]: ActiveSync AirSyncBase Namespace Protocol Specification (http://msdn.microsoft.com/en-us/library/dd299454(EXCHG.80).aspx) to implement the functionality described previously.

Prerequisites
This sample requires the following:
* A target server that is running Microsoft Exchange Server 2010 Service Pack 1 (SP1) or a later version of Exchange.
* The .NET Framework version 4.0.
* Visual Studio 2010 with the C# component.
Or
* A text editor to create and edit source code files and a command prompt window to run a .NET Framework command-line compiler.

Sample components
This sample contains the following files:
* EX2010_activesyncfolder_cs.sln — The Visual Studio 2010 solution file for the EX2010_activesyncfolder_cs project.
* EX2010_activesyncfolder_cs.csproj — The Visual Studio 2010 project file for the sample application.
* ASCommandRequest.cs — Contains the using statements, namespace, class, and functions to send a generic Exchange ActiveSync command request to the server.
* ASCommandResponse.cs — Contains the using statements, namespace, class, and functions to parse an Exchange ActiveSync command response from the server.
* ASError.cs — Contains the using statements, namespace, class, and functions to display an exception to the user.
* ASFolderSyncRequest.cs — Contains the using statements, namespace, class, and functions to send a FolderSync command request to the server.
* ASFolderSyncResponse.cs — Contains the using statements, namespace, class, and functions to parse a FolderSync command response from the server.
* ASOptionsRequest.cs — Contains the using statements, namespace, class, and functions to send an HTTP OPTIONS request to the server.
* ASOptionsResponse.cs — Contains the using statements, namespace, class, and functions to parse an HTTP OPTIONS response from the server.
* ASPolicy.cs — Contains the using statements, namespace, class, and functions to parse an XML document containing an Exchange ActiveSync policy.
* ASProvisionRequest.cs — Contains the using statements, namespace, class, and functions to send a Provision command request to the server.
* ASProvisionResponse.cs — Contains the using statements, namespace, class, and functions to parse a Provision command response from the server.
* ASSyncRequest.cs — Contains the using statements, namespace, class, and functions to send a Sync command request to the server.
* ASSyncResponse.cs — Contains the using statements, namespace, class, and functions to parse a Sync command response from the server.
* ASWBXML.cs — Contains the using statements, namespace, class, and functions to encode an XML document into a WBXML binary stream, and vice-versa.
* ASWBXMLByteQueue.cs — Contains the using statements, namespace, class, and functions to manage a WBXML binary stream as a .NET Queue object.
* ASWBXMLCodePage.cs — Contains the using statements, namespace, class, and functions to manage WBXML code pages.
* Device.cs — Contains the using statements, namespace, class, and functions to generate a <DeviceInformation> element.
* EncodedRequest.cs — Contains the using statements, namespace, class, and functions to generate a base64-encoded request URI for ActiveSync command requests.
* Folder.cs — Contains the using statements, namespace, class, and functions to manage the client's local copy of a folder in a user's mailbox.
* Program.cs — Contains the using statements, namespace, class, and functions to send an OPTIONS request, provision, sync the folder hierarchy, and download the contents in the user's inbox.
* ServerSyncCommand.cs — Contains the using statements, namespace, class, and functions to parse an Add element within a Command element in a Sync command response from the server.
* Utilities.cs — Contains the using statements, namespace, class, and functions to display binary data as a hexadecimal string and to convert a hexadecimal string into binary data.

Configuring the sample
Follow these steps to configure the Exchange ActiveSync folder sync sample.
1. Replace the value of the activeSyncServer variable in the Program.cs file with the fully-qualified domain name of your Exchange 2010 SP1 server.
2. Replace the value of the userName variable in the Program.cs file with the username of the mailbox you are using.
3. Replace the value of the password variable in the Program.cs file with the password of the user account indicated in the userName variable.
4. Replace the value of the domainName variable in the Program.cs file with the domain name of the user account indicated in the userName variable.
5. Replace the value of the mailboxCacheLocation variable in the Program.cs file with the full path to a local directory on your computer where you have write access.

Building the sample
Press F6 to build and deploy the sample.

Running and testing the sample
Press F5 to run the sample.

Related topics
Implementing an Exchange ActiveSync client: folder synchronization (http://msdn.microsoft.com/en-us/library/3718e941-b25a-4760-bc0a-7b650e4825c1)

Change log
First release.
