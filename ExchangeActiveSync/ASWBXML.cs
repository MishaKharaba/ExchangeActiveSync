﻿using System.Collections.Generic;
using System.IO;
using System.Xml;

namespace ExchangeActiveSync
{
    static class Namespaces
    {
        public const string airSyncNamespace = "AirSync";
        public const string airSyncBaseNamespace = "AirSyncBase";
        public const string folderHierarchyNamespace = "FolderHierarchy";
        public const string provisionNamespace = "Provision";
        public const string rightsManagementNamespace = "RightsManagement";
    }

    static class Xmlns
    {
        public const string airSyncXmlns = "airsync";
        public const string airSyncBaseXmlns = "airsyncbase";
        public const string folderHierarchyXmlns = "folderhierarchy";
        public const string provisionXmlns = "provision";
        public const string rightsManagementXmlns = "rightsmanagement";
    }

    // This enumeration is straight from the WBXML spec's
    // list of global tokens.
    enum GlobalTokens
    {
        SWITCH_PAGE = 0x00,
        END         = 0x01,
        ENTITY      = 0x02,
        STR_I       = 0x03,
        LITERAL     = 0x04,
        EXT_I_0     = 0x40,
        EXT_I_1     = 0x41,
        EXT_I_2     = 0x42,
        PI          = 0x43,
        LITERAL_C   = 0x44,
        EXT_T_0     = 0x80,
        EXT_T_1     = 0x81,
        EXT_T_2     = 0x82,
        STR_T       = 0x83,
        LITERAL_A   = 0x84,
        EXT_0       = 0xC0,
        EXT_1       = 0xC1,
        EXT_2       = 0xC2,
        OPAQUE      = 0xC3,
        LITERAL_AC  = 0xC4
    }

    // This class impelements a WBXML parser.
    class ASWBXML
    {
        const byte versionByte = 0x03;
        const byte publicIdentifierByte = 0x01;
        const byte characterSetByte = 0x6A;     // UTF-8
        const byte stringTableLengthByte = 0x00;

        private XmlDocument xmlDoc = new XmlDocument();
        private ASWBXMLCodePage[] codePages;
        private int currentCodePage = 0;
        private int defaultCodePage = -1;

        public ASWBXML()
        {
            // Load up code pages
            // Currently there are 25 code pages as per MS-ASWBXML
            codePages = new ASWBXMLCodePage[25];

            #region Code Page Initialization
            // Code Page 0: AirSync
            #region AirSync Code Page
            codePages[0] = new ASWBXMLCodePage();
            codePages[0].Namespace = "AirSync";
            codePages[0].Xmlns = "airsync";

            codePages[0].AddToken(0x05, "Sync");
            codePages[0].AddToken(0x06, "Responses");
            codePages[0].AddToken(0x07, "Add");
            codePages[0].AddToken(0x08, "Change");
            codePages[0].AddToken(0x09, "Delete");
            codePages[0].AddToken(0x0A, "Fetch");
            codePages[0].AddToken(0x0B, "SyncKey");
            codePages[0].AddToken(0x0C, "ClientId");
            codePages[0].AddToken(0x0D, "ServerId");
            codePages[0].AddToken(0x0E, "Status");
            codePages[0].AddToken(0x0F, "Collection");
            codePages[0].AddToken(0x10, "Class");
            codePages[0].AddToken(0x12, "CollectionId");
            codePages[0].AddToken(0x13, "GetChanges");
            codePages[0].AddToken(0x14, "MoreAvailable");
            codePages[0].AddToken(0x15, "WindowSize");
            codePages[0].AddToken(0x16, "Commands");
            codePages[0].AddToken(0x17, "Options");
            codePages[0].AddToken(0x18, "FilterType");
            codePages[0].AddToken(0x1B, "Conflict");
            codePages[0].AddToken(0x1C, "Collections");
            codePages[0].AddToken(0x1D, "ApplicationData");
            codePages[0].AddToken(0x1E, "DeletesAsMoves");
            codePages[0].AddToken(0x20, "Supported");
            codePages[0].AddToken(0x21, "SoftDelete");
            codePages[0].AddToken(0x22, "MIMESupport");
            codePages[0].AddToken(0x23, "MIMETruncation");
            codePages[0].AddToken(0x24, "Wait");
            codePages[0].AddToken(0x25, "Limit");
            codePages[0].AddToken(0x26, "Partial");
            codePages[0].AddToken(0x27, "ConversationMode");
            codePages[0].AddToken(0x28, "MaxItems");
            codePages[0].AddToken(0x29, "HeartbeatInterval");
            #endregion

            // Code Page 1: Contacts
            #region Contacts Code Page
            codePages[1] = new ASWBXMLCodePage();
            codePages[1].Namespace = "Contacts";
            codePages[1].Xmlns = "contacts";

            codePages[1].AddToken(0x05, "Anniversary");
            codePages[1].AddToken(0x06, "AssistantName");
            codePages[1].AddToken(0x07, "AssistantPhoneNumber");
            codePages[1].AddToken(0x08, "Birthday");
            codePages[1].AddToken(0x0C, "Business2PhoneNumber");
            codePages[1].AddToken(0x0D, "BusinessAddressCity");
            codePages[1].AddToken(0x0E, "BusinessAddressCountry");
            codePages[1].AddToken(0x0F, "BusinessAddressPostalCode");
            codePages[1].AddToken(0x10, "BusinessAddressState");
            codePages[1].AddToken(0x11, "BusinessAddressStreet");
            codePages[1].AddToken(0x12, "BusinessFaxNumber");
            codePages[1].AddToken(0x13, "BusinessPhoneNumber");
            codePages[1].AddToken(0x14, "CarPhoneNumber");
            codePages[1].AddToken(0x15, "Categories");
            codePages[1].AddToken(0x16, "Category");
            codePages[1].AddToken(0x17, "Children");
            codePages[1].AddToken(0x18, "Child");
            codePages[1].AddToken(0x19, "CompanyName");
            codePages[1].AddToken(0x1A, "Department");
            codePages[1].AddToken(0x1B, "Email1Address");
            codePages[1].AddToken(0x1C, "Email2Address");
            codePages[1].AddToken(0x1D, "Email3Address");
            codePages[1].AddToken(0x1E, "FileAs");
            codePages[1].AddToken(0x1F, "FirstName");
            codePages[1].AddToken(0x20, "Home2PhoneNumber");
            codePages[1].AddToken(0x21, "HomeAddressCity");
            codePages[1].AddToken(0x22, "HomeAddressCountry");
            codePages[1].AddToken(0x23, "HomeAddressPostalCode");
            codePages[1].AddToken(0x24, "HomeAddressState");
            codePages[1].AddToken(0x25, "HomeAddressStreet");
            codePages[1].AddToken(0x26, "HomeFaxNumber");
            codePages[1].AddToken(0x27, "HomePhoneNumber");
            codePages[1].AddToken(0x28, "JobTitle");
            codePages[1].AddToken(0x29, "LastName");
            codePages[1].AddToken(0x2A, "MiddleName");
            codePages[1].AddToken(0x2B, "MobilePhoneNumber");
            codePages[1].AddToken(0x2C, "OfficeLocation");
            codePages[1].AddToken(0x2D, "OtherAddressCity");
            codePages[1].AddToken(0x2E, "OtherAddressCountry");
            codePages[1].AddToken(0x2F, "OtherAddressPostalCode");
            codePages[1].AddToken(0x30, "OtherAddressState");
            codePages[1].AddToken(0x31, "OtherAddressStreet");
            codePages[1].AddToken(0x32, "PagerNumber");
            codePages[1].AddToken(0x33, "RadioPhoneNumber");
            codePages[1].AddToken(0x34, "Spouse");
            codePages[1].AddToken(0x35, "Suffix");
            codePages[1].AddToken(0x36, "Title");
            codePages[1].AddToken(0x37, "WebPage");
            codePages[1].AddToken(0x38, "YomiCompanyName");
            codePages[1].AddToken(0x39, "YomiFirstName");
            codePages[1].AddToken(0x3A, "YomiLastName");
            codePages[1].AddToken(0x3C, "Picture");
            codePages[1].AddToken(0x3D, "Alias");
            codePages[1].AddToken(0x3E, "WeightedRank");
            #endregion

            // Code Page 2: Email
            #region Email Code Page
            codePages[2] = new ASWBXMLCodePage();
            codePages[2].Namespace = "Email";
            codePages[2].Xmlns = "email";

            codePages[2].AddToken(0x0F, "DateReceived");
            codePages[2].AddToken(0x11, "DisplayTo");
            codePages[2].AddToken(0x12, "Importance");
            codePages[2].AddToken(0x13, "MessageClass");
            codePages[2].AddToken(0x14, "Subject");
            codePages[2].AddToken(0x15, "Read");
            codePages[2].AddToken(0x16, "To");
            codePages[2].AddToken(0x17, "Cc");
            codePages[2].AddToken(0x18, "From");
            codePages[2].AddToken(0x19, "ReplyTo");
            codePages[2].AddToken(0x1A, "AllDayEvent");
            codePages[2].AddToken(0x1B, "Categories");
            codePages[2].AddToken(0x1C, "Category");
            codePages[2].AddToken(0x1D, "DtStamp");
            codePages[2].AddToken(0x1E, "EndTime");
            codePages[2].AddToken(0x1F, "InstanceType");
            codePages[2].AddToken(0x20, "BusyStatus");
            codePages[2].AddToken(0x21, "Location");
            codePages[2].AddToken(0x22, "MeetingRequest");
            codePages[2].AddToken(0x23, "Organizer");
            codePages[2].AddToken(0x24, "RecurrenceId");
            codePages[2].AddToken(0x25, "Reminder");
            codePages[2].AddToken(0x26, "ResponseRequested");
            codePages[2].AddToken(0x27, "Recurrences");
            codePages[2].AddToken(0x28, "Recurrence");
            codePages[2].AddToken(0x29, "Type");
            codePages[2].AddToken(0x2A, "Until");
            codePages[2].AddToken(0x2B, "Occurrences");
            codePages[2].AddToken(0x2C, "Interval");
            codePages[2].AddToken(0x2D, "DayOfWeek");
            codePages[2].AddToken(0x2E, "DayOfMonth");
            codePages[2].AddToken(0x2F, "WeekOfMonth");
            codePages[2].AddToken(0x30, "MonthOfYear");
            codePages[2].AddToken(0x31, "StartTime");
            codePages[2].AddToken(0x32, "Sensitivity");
            codePages[2].AddToken(0x33, "TimeZone");
            codePages[2].AddToken(0x34, "GlobalObjId");
            codePages[2].AddToken(0x35, "ThreadTopic");
            codePages[2].AddToken(0x39, "InternetCPID");
            codePages[2].AddToken(0x3A, "Flag");
            codePages[2].AddToken(0x3B, "Status");
            codePages[2].AddToken(0x3C, "ContentClass");
            codePages[2].AddToken(0x3D, "FlagType");
            codePages[2].AddToken(0x3E, "CompleteTime");
            codePages[2].AddToken(0x3F, "DisallowNewTimeProposal");
            #endregion

            // Code Page 3: AirNotify
            #region AirNotify Code Page
            codePages[3] = new ASWBXMLCodePage();
            codePages[3].Namespace = "";
            codePages[3].Xmlns = "";
            #endregion

            // Code Page 4: Calendar
            #region Calendar Code Page
            codePages[4] = new ASWBXMLCodePage();
            codePages[4].Namespace = "Calendar";
            codePages[4].Xmlns = "calendar";

            codePages[4].AddToken(0x05, "TimeZone");
            codePages[4].AddToken(0x06, "AllDayEvent");
            codePages[4].AddToken(0x07, "Attendees");
            codePages[4].AddToken(0x08, "Attendee");
            codePages[4].AddToken(0x09, "Email");
            codePages[4].AddToken(0x0A, "Name");
            codePages[4].AddToken(0x0D, "BusyStatus");
            codePages[4].AddToken(0x0E, "Categories");
            codePages[4].AddToken(0x0F, "Category");
            codePages[4].AddToken(0x11, "DtStamp");
            codePages[4].AddToken(0x12, "EndTime");
            codePages[4].AddToken(0x13, "Exception");
            codePages[4].AddToken(0x14, "Exceptions");
            codePages[4].AddToken(0x15, "Deleted");
            codePages[4].AddToken(0x16, "ExceptionStartTime");
            codePages[4].AddToken(0x17, "Location");
            codePages[4].AddToken(0x18, "MeetingStatus");
            codePages[4].AddToken(0x19, "OrganizerEmail");
            codePages[4].AddToken(0x1A, "OrganizerName");
            codePages[4].AddToken(0x1B, "Recurrence");
            codePages[4].AddToken(0x1C, "Type");
            codePages[4].AddToken(0x1D, "Until");
            codePages[4].AddToken(0x1E, "Occurrences");
            codePages[4].AddToken(0x1F, "Interval");
            codePages[4].AddToken(0x20, "DayOfWeek");
            codePages[4].AddToken(0x21, "DayOfMonth");
            codePages[4].AddToken(0x22, "WeekOfMonth");
            codePages[4].AddToken(0x23, "MonthOfYear");
            codePages[4].AddToken(0x24, "Reminder");
            codePages[4].AddToken(0x25, "Sensitivity");
            codePages[4].AddToken(0x26, "Subject");
            codePages[4].AddToken(0x27, "StartTime");
            codePages[4].AddToken(0x28, "UID");
            codePages[4].AddToken(0x29, "AttendeeStatus");
            codePages[4].AddToken(0x2A, "AttendeeType");
            codePages[4].AddToken(0x33, "DisallowNewTimeProposal");
            codePages[4].AddToken(0x34, "ResponseRequested");
            codePages[4].AddToken(0x35, "AppointmentReplyTime");
            codePages[4].AddToken(0x36, "ResponseType");
            codePages[4].AddToken(0x37, "CalendarType");
            codePages[4].AddToken(0x38, "IsLeapMonth");
            codePages[4].AddToken(0x39, "FirstDayOfWeek");
            codePages[4].AddToken(0x3A, "OnlineMeetingConfLink");
            codePages[4].AddToken(0x3B, "OnlineMeetingExternalLink");
            #endregion

            // Code Page 5: Move
            #region Move Code Page
            codePages[5] = new ASWBXMLCodePage();
            codePages[5].Namespace = "Move";
            codePages[5].Xmlns = "move";

            codePages[5].AddToken(0x05, "MoveItems");
            codePages[5].AddToken(0x06, "Move");
            codePages[5].AddToken(0x07, "SrcMsgId");
            codePages[5].AddToken(0x08, "SrcFldId");
            codePages[5].AddToken(0x09, "DstFldId");
            codePages[5].AddToken(0x0A, "Response");
            codePages[5].AddToken(0x0B, "Status");
            codePages[5].AddToken(0x0C, "DstMsgId");
            #endregion

            // Code Page 6: ItemEstimate
            #region ItemEstimate Code Page
            codePages[6] = new ASWBXMLCodePage();
            codePages[6].Namespace = "GetItemEstimate";
            codePages[6].Xmlns = "getitemestimate";

            codePages[6].AddToken(0x05, "GetItemEstimate");
            codePages[6].AddToken(0x06, "Version");
            codePages[6].AddToken(0x07, "Collections");
            codePages[6].AddToken(0x08, "Collection");
            codePages[6].AddToken(0x09, "Class");
            codePages[6].AddToken(0x0A, "CollectionId");
            codePages[6].AddToken(0x0B, "DateTime");
            codePages[6].AddToken(0x0C, "Estimate");
            codePages[6].AddToken(0x0D, "Response");
            codePages[6].AddToken(0x0E, "Status");
            #endregion

            // Code Page 7: FolderHierarchy
            #region FolderHierarchy Code Page
            codePages[7] = new ASWBXMLCodePage();
            codePages[7].Namespace = "FolderHierarchy";
            codePages[7].Xmlns = "folderhierarchy";

            codePages[7].AddToken(0x07, "DisplayName");
            codePages[7].AddToken(0x08, "ServerId");
            codePages[7].AddToken(0x09, "ParentId");
            codePages[7].AddToken(0x0A, "Type");
            codePages[7].AddToken(0x0C, "Status");
            codePages[7].AddToken(0x0E, "Changes");
            codePages[7].AddToken(0x0F, "Add");
            codePages[7].AddToken(0x10, "Delete");
            codePages[7].AddToken(0x11, "Update");
            codePages[7].AddToken(0x12, "SyncKey");
            codePages[7].AddToken(0x13, "FolderCreate");
            codePages[7].AddToken(0x14, "FolderDelete");
            codePages[7].AddToken(0x15, "FolderUpdate");
            codePages[7].AddToken(0x16, "FolderSync");
            codePages[7].AddToken(0x17, "Count");
            #endregion

            // Code Page 8: MeetingResponse
            #region MeetingResponse Code Page
            codePages[8] = new ASWBXMLCodePage();
            codePages[8].Namespace = "MeetingResponse";
            codePages[8].Xmlns = "meetingresponse";

            codePages[8].AddToken(0x05, "CalendarId");
            codePages[8].AddToken(0x06, "CollectionId");
            codePages[8].AddToken(0x07, "MeetingResponse");
            codePages[8].AddToken(0x08, "RequestId");
            codePages[8].AddToken(0x09, "Request");
            codePages[8].AddToken(0x0A, "Result");
            codePages[8].AddToken(0x0B, "Status");
            codePages[8].AddToken(0x0C, "UserResponse");
            codePages[8].AddToken(0x0E, "InstanceId");
            #endregion

            // Code Page 9: Tasks
            #region Tasks Code Page
            codePages[9] = new ASWBXMLCodePage();
            codePages[9].Namespace = "Tasks";
            codePages[9].Xmlns = "tasks";

            codePages[9].AddToken(0x08, "Categories");
            codePages[9].AddToken(0x09, "Category");
            codePages[9].AddToken(0x0A, "Complete");
            codePages[9].AddToken(0x0B, "DateCompleted");
            codePages[9].AddToken(0x0C, "DueDate");
            codePages[9].AddToken(0x0D, "UtcDueDate");
            codePages[9].AddToken(0x0E, "Importance");
            codePages[9].AddToken(0x0F, "Recurrence");
            codePages[9].AddToken(0x10, "Type");
            codePages[9].AddToken(0x11, "Start");
            codePages[9].AddToken(0x12, "Until");
            codePages[9].AddToken(0x13, "Occurrences");
            codePages[9].AddToken(0x14, "Interval");
            codePages[9].AddToken(0x15, "DayOfMonth");
            codePages[9].AddToken(0x16, "DayOfWeek");
            codePages[9].AddToken(0x17, "WeekOfMonth");
            codePages[9].AddToken(0x18, "MonthOfYear");
            codePages[9].AddToken(0x19, "Regenerate");
            codePages[9].AddToken(0x1A, "DeadOccur");
            codePages[9].AddToken(0x1B, "ReminderSet");
            codePages[9].AddToken(0x1C, "ReminderTime");
            codePages[9].AddToken(0x1D, "Sensitivity");
            codePages[9].AddToken(0x1E, "StartDate");
            codePages[9].AddToken(0x1F, "UtcStartDate");
            codePages[9].AddToken(0x20, "Subject");
            codePages[9].AddToken(0x22, "OrdinalDate");
            codePages[9].AddToken(0x23, "SubOrdinalDate");
            codePages[9].AddToken(0x24, "CalendarType");
            codePages[9].AddToken(0x25, "IsLeapMonth");
            codePages[9].AddToken(0x26, "FirstDayOfWeek");
            #endregion

            // Code Page 10: ResolveRecipients
            #region ResolveRecipients Code Page
            codePages[10] = new ASWBXMLCodePage();
            codePages[10].Namespace = "ResolveRecipients";
            codePages[10].Xmlns = "resolverecipients";

            codePages[10].AddToken(0x05, "ResolveRecipients");
            codePages[10].AddToken(0x06, "Response");
            codePages[10].AddToken(0x07, "Status");
            codePages[10].AddToken(0x08, "Type");
            codePages[10].AddToken(0x09, "Recipient");
            codePages[10].AddToken(0x0A, "DisplayName");
            codePages[10].AddToken(0x0B, "EmailAddress");
            codePages[10].AddToken(0x0C, "Certificates");
            codePages[10].AddToken(0x0D, "Certificate");
            codePages[10].AddToken(0x0E, "MiniCertificate");
            codePages[10].AddToken(0x0F, "Options");
            codePages[10].AddToken(0x10, "To");
            codePages[10].AddToken(0x11, "CertificateRetrieval");
            codePages[10].AddToken(0x12, "RecipientCount");
            codePages[10].AddToken(0x13, "MaxCertificates");
            codePages[10].AddToken(0x14, "MaxAmbiguousRecipients");
            codePages[10].AddToken(0x15, "CertificateCount");
            codePages[10].AddToken(0x16, "Availability");
            codePages[10].AddToken(0x17, "StartTime");
            codePages[10].AddToken(0x18, "EndTime");
            codePages[10].AddToken(0x19, "MergedFreeBusy");
            codePages[10].AddToken(0x1A, "Picture");
            codePages[10].AddToken(0x1B, "MaxSize");
            codePages[10].AddToken(0x1C, "Data");
            codePages[10].AddToken(0x1D, "MaxPictures");
            #endregion

            // Code Page 11: ValidateCert
            #region ValidateCert Code Page
            codePages[11] = new ASWBXMLCodePage();
            codePages[11].Namespace = "ValidateCert";
            codePages[11].Xmlns = "validatecert";

            codePages[11].AddToken(0x05, "ValidateCert");
            codePages[11].AddToken(0x06, "Certificates");
            codePages[11].AddToken(0x07, "Certificate");
            codePages[11].AddToken(0x08, "CertificateChain");
            codePages[11].AddToken(0x09, "CheckCRL");
            codePages[11].AddToken(0x0A, "Status");
            #endregion

            // Code Page 12: Contacts2
            #region Contacts2 Code Page
            codePages[12] = new ASWBXMLCodePage();
            codePages[12].Namespace = "Contacts2";
            codePages[12].Xmlns = "contacts2";

            codePages[12].AddToken(0x05, "CustomerId");
            codePages[12].AddToken(0x06, "GovernmentId");
            codePages[12].AddToken(0x07, "IMAddress");
            codePages[12].AddToken(0x08, "IMAddress2");
            codePages[12].AddToken(0x09, "IMAddress3");
            codePages[12].AddToken(0x0A, "ManagerName");
            codePages[12].AddToken(0x0B, "CompanyMainPhone");
            codePages[12].AddToken(0x0C, "AccountName");
            codePages[12].AddToken(0x0D, "NickName");
            codePages[12].AddToken(0x0E, "MMS");
            #endregion

            // Code Page 13: Ping
            #region Ping Code Page
            codePages[13] = new ASWBXMLCodePage();
            codePages[13].Namespace = "Ping";
            codePages[13].Xmlns = "ping";

            codePages[13].AddToken(0x05, "Ping");
            codePages[13].AddToken(0x06, "AutdState");  // Per MS-ASWBXML, this tag is not used by protocol
            codePages[13].AddToken(0x07, "Status");
            codePages[13].AddToken(0x08, "HeartbeatInterval");
            codePages[13].AddToken(0x09, "Folders");
            codePages[13].AddToken(0x0A, "Folder");
            codePages[13].AddToken(0x0B, "Id");
            codePages[13].AddToken(0x0C, "Class");
            codePages[13].AddToken(0x0D, "MaxFolders");
            #endregion

            // Code Page 14: Provision
            #region Provision Code Page
            codePages[14] = new ASWBXMLCodePage();
            codePages[14].Namespace = "Provision";
            codePages[14].Xmlns = "provision";

            codePages[14].AddToken(0x05, "Provision");
            codePages[14].AddToken(0x06, "Policies");
            codePages[14].AddToken(0x07, "Policy");
            codePages[14].AddToken(0x08, "PolicyType");
            codePages[14].AddToken(0x09, "PolicyKey");
            codePages[14].AddToken(0x0A, "Data");
            codePages[14].AddToken(0x0B, "Status");
            codePages[14].AddToken(0x0C, "RemoteWipe");
            codePages[14].AddToken(0x0D, "EASProvisionDoc");
            codePages[14].AddToken(0x0E, "DevicePasswordEnabled");
            codePages[14].AddToken(0x0F, "AlphanumericDevicePasswordRequired");
            //codePages[14].AddToken(0x10, "DeviceEncryptionEnabled");
            codePages[14].AddToken(0x10, "RequireStorageCardEncryption");
            codePages[14].AddToken(0x11, "PasswordRecoveryEnabled");
            codePages[14].AddToken(0x13, "AttachmentsEnabled");
            codePages[14].AddToken(0x14, "MinDevicePasswordLength");
            codePages[14].AddToken(0x15, "MaxInactivityTimeDeviceLock");
            codePages[14].AddToken(0x16, "MaxDevicePasswordFailedAttempts");
            codePages[14].AddToken(0x17, "MaxAttachmentSize");
            codePages[14].AddToken(0x18, "AllowSimpleDevicePassword");
            codePages[14].AddToken(0x19, "DevicePasswordExpiration");
            codePages[14].AddToken(0x1A, "DevicePasswordHistory");
            codePages[14].AddToken(0x1B, "AllowStorageCard");
            codePages[14].AddToken(0x1C, "AllowCamera");
            codePages[14].AddToken(0x1D, "RequireDeviceEncryption");
            codePages[14].AddToken(0x1E, "AllowUnsignedApplications");
            codePages[14].AddToken(0x1F, "AllowUnsignedInstallationPackages");
            codePages[14].AddToken(0x20, "MinDevicePasswordComplexCharacters");
            codePages[14].AddToken(0x21, "AllowWiFi");
            codePages[14].AddToken(0x22, "AllowTextMessaging");
            codePages[14].AddToken(0x23, "AllowPOPIMAPEmail");
            codePages[14].AddToken(0x24, "AllowBluetooth");
            codePages[14].AddToken(0x25, "AllowIrDA");
            codePages[14].AddToken(0x26, "RequireManualSyncWhenRoaming");
            codePages[14].AddToken(0x27, "AllowDesktopSync");
            codePages[14].AddToken(0x28, "MaxCalendarAgeFilter");
            codePages[14].AddToken(0x29, "AllowHTMLEmail");
            codePages[14].AddToken(0x2A, "MaxEmailAgeFilter");
            codePages[14].AddToken(0x2B, "MaxEmailBodyTruncationSize");
            codePages[14].AddToken(0x2C, "MaxEmailHTMLBodyTruncationSize");
            codePages[14].AddToken(0x2D, "RequireSignedSMIMEMessages");
            codePages[14].AddToken(0x2E, "RequireEncryptedSMIMEMessages");
            codePages[14].AddToken(0x2F, "RequireSignedSMIMEAlgorithm");
            codePages[14].AddToken(0x30, "RequireEncryptionSMIMEAlgorithm");
            codePages[14].AddToken(0x31, "AllowSMIMEEncryptionAlgorithmNegotiation");
            codePages[14].AddToken(0x32, "AllowSMIMESoftCerts");
            codePages[14].AddToken(0x33, "AllowBrowser");
            codePages[14].AddToken(0x34, "AllowConsumerEmail");
            codePages[14].AddToken(0x35, "AllowRemoteDesktop");
            codePages[14].AddToken(0x36, "AllowInternetSharing");
            codePages[14].AddToken(0x37, "UnapprovedInROMApplicationList");
            codePages[14].AddToken(0x38, "ApplicationName");
            codePages[14].AddToken(0x39, "ApprovedApplicationList");
            codePages[14].AddToken(0x3A, "Hash");
            #endregion

            // Code Page 15: Search
            #region Search Code Page
            codePages[15] = new ASWBXMLCodePage();
            codePages[15].Namespace = "Search";
            codePages[15].Xmlns = "search";

            codePages[15].AddToken(0x05, "Search");
            codePages[15].AddToken(0x07, "Store");
            codePages[15].AddToken(0x08, "Name");
            codePages[15].AddToken(0x09, "Query");
            codePages[15].AddToken(0x0A, "Options");
            codePages[15].AddToken(0x0B, "Range");
            codePages[15].AddToken(0x0C, "Status");
            codePages[15].AddToken(0x0D, "Response");
            codePages[15].AddToken(0x0E, "Result");
            codePages[15].AddToken(0x0F, "Properties");
            codePages[15].AddToken(0x10, "Total");
            codePages[15].AddToken(0x11, "EqualTo");
            codePages[15].AddToken(0x12, "Value");
            codePages[15].AddToken(0x13, "And");
            codePages[15].AddToken(0x14, "Or");
            codePages[15].AddToken(0x15, "FreeText");
            codePages[15].AddToken(0x17, "DeepTraversal");
            codePages[15].AddToken(0x18, "LongId");
            codePages[15].AddToken(0x19, "RebuildResults");
            codePages[15].AddToken(0x1A, "LessThan");
            codePages[15].AddToken(0x1B, "GreaterThan");
            codePages[15].AddToken(0x1E, "UserName");
            codePages[15].AddToken(0x1F, "Password");
            codePages[15].AddToken(0x20, "ConversationId");
            codePages[15].AddToken(0x21, "Picture");
            codePages[15].AddToken(0x22, "MaxSize");
            codePages[15].AddToken(0x23, "MaxPictures");
            #endregion

            // Code Page 16: GAL
            #region GAL Code Page
            codePages[16] = new ASWBXMLCodePage();
            codePages[16].Namespace = "GAL";
            codePages[16].Xmlns = "gal";

            codePages[16].AddToken(0x05, "DisplayName");
            codePages[16].AddToken(0x06, "Phone");
            codePages[16].AddToken(0x07, "Office");
            codePages[16].AddToken(0x08, "Title");
            codePages[16].AddToken(0x09, "Company");
            codePages[16].AddToken(0x0A, "Alias");
            codePages[16].AddToken(0x0B, "FirstName");
            codePages[16].AddToken(0x0C, "LastName");
            codePages[16].AddToken(0x0D, "HomePhone");
            codePages[16].AddToken(0x0E, "MobilePhone");
            codePages[16].AddToken(0x0F, "EmailAddress");
            codePages[16].AddToken(0x10, "Picture");
            codePages[16].AddToken(0x11, "Status");
            codePages[16].AddToken(0x12, "Data");
            #endregion

            // Code Page 17: AirSyncBase
            #region AirSyncBase Code Page
            codePages[17] = new ASWBXMLCodePage();
            codePages[17].Namespace = "AirSyncBase";
            codePages[17].Xmlns = "airsyncbase";

            codePages[17].AddToken(0x05, "BodyPreference");
            codePages[17].AddToken(0x06, "Type");
            codePages[17].AddToken(0x07, "TruncationSize");
            codePages[17].AddToken(0x08, "AllOrNone");
            codePages[17].AddToken(0x0A, "Body");
            codePages[17].AddToken(0x0B, "Data");
            codePages[17].AddToken(0x0C, "EstimatedDataSize");
            codePages[17].AddToken(0x0D, "Truncated");
            codePages[17].AddToken(0x0E, "Attachments");
            codePages[17].AddToken(0x0F, "Attachment");
            codePages[17].AddToken(0x10, "DisplayName");
            codePages[17].AddToken(0x11, "FileReference");
            codePages[17].AddToken(0x12, "Method");
            codePages[17].AddToken(0x13, "ContentId");
            codePages[17].AddToken(0x14, "ContentLocation");
            codePages[17].AddToken(0x15, "IsInline");
            codePages[17].AddToken(0x16, "NativeBodyType");
            codePages[17].AddToken(0x17, "ContentType");
            codePages[17].AddToken(0x18, "Preview");
            codePages[17].AddToken(0x19, "BodyPartPreference");
            codePages[17].AddToken(0x1A, "BodyPart");
            codePages[17].AddToken(0x1B, "Status");
            #endregion

            // Code Page 18: Settings
            #region Settings Code Page
            codePages[18] = new ASWBXMLCodePage();
            codePages[18].Namespace = "Settings";
            codePages[18].Xmlns = "settings";

            codePages[18].AddToken(0x05, "Settings");
            codePages[18].AddToken(0x06, "Status");
            codePages[18].AddToken(0x07, "Get");
            codePages[18].AddToken(0x08, "Set");
            codePages[18].AddToken(0x09, "Oof");
            codePages[18].AddToken(0x0A, "OofState");
            codePages[18].AddToken(0x0B, "StartTime");
            codePages[18].AddToken(0x0C, "EndTime");
            codePages[18].AddToken(0x0D, "OofMessage");
            codePages[18].AddToken(0x0E, "AppliesToInternal");
            codePages[18].AddToken(0x0F, "AppliesToExternalKnown");
            codePages[18].AddToken(0x10, "AppliesToExternalUnknown");
            codePages[18].AddToken(0x11, "Enabled");
            codePages[18].AddToken(0x12, "ReplyMessage");
            codePages[18].AddToken(0x13, "BodyType");
            codePages[18].AddToken(0x14, "DevicePassword");
            codePages[18].AddToken(0x15, "Password");
            codePages[18].AddToken(0x16, "DeviceInformation");
            codePages[18].AddToken(0x17, "Model");
            codePages[18].AddToken(0x18, "IMEI");
            codePages[18].AddToken(0x19, "FriendlyName");
            codePages[18].AddToken(0x1A, "OS");
            codePages[18].AddToken(0x1B, "OSLanguage");
            codePages[18].AddToken(0x1C, "PhoneNumber");
            codePages[18].AddToken(0x1D, "UserInformation");
            codePages[18].AddToken(0x1E, "EmailAddresses");
            codePages[18].AddToken(0x1F, "SMTPAddress");
            codePages[18].AddToken(0x20, "UserAgent");
            codePages[18].AddToken(0x21, "EnableOutboundSMS");
            codePages[18].AddToken(0x22, "MobileOperator");
            codePages[18].AddToken(0x23, "PrimarySmtpAddress");
            codePages[18].AddToken(0x24, "Accounts");
            codePages[18].AddToken(0x25, "Account");
            codePages[18].AddToken(0x26, "AccountId");
            codePages[18].AddToken(0x27, "AccountName");
            codePages[18].AddToken(0x28, "UserDisplayName");
            codePages[18].AddToken(0x29, "SendDisabled");
            codePages[18].AddToken(0x2B, "RightsManagementInformation");
            #endregion

            // Code Page 19: DocumentLibrary
            #region DocumentLibrary Code Page
            codePages[19] = new ASWBXMLCodePage();
            codePages[19].Namespace = "DocumentLibrary";
            codePages[19].Xmlns = "documentlibrary";

            codePages[19].AddToken(0x05, "LinkId");
            codePages[19].AddToken(0x06, "DisplayName");
            codePages[19].AddToken(0x07, "IsFolder");
            codePages[19].AddToken(0x08, "CreationDate");
            codePages[19].AddToken(0x09, "LastModifiedDate");
            codePages[19].AddToken(0x0A, "IsHidden");
            codePages[19].AddToken(0x0B, "ContentLength");
            codePages[19].AddToken(0x0C, "ContentType");
            #endregion

            // Code Page 20: ItemOperations
            #region ItemOperations Code Page
            codePages[20] = new ASWBXMLCodePage();
            codePages[20].Namespace = "ItemOperations";
            codePages[20].Xmlns = "itemoperations";

            codePages[20].AddToken(0x05, "ItemOperations");
            codePages[20].AddToken(0x06, "Fetch");
            codePages[20].AddToken(0x07, "Store");
            codePages[20].AddToken(0x08, "Options");
            codePages[20].AddToken(0x09, "Range");
            codePages[20].AddToken(0x0A, "Total");
            codePages[20].AddToken(0x0B, "Properties");
            codePages[20].AddToken(0x0C, "Data");
            codePages[20].AddToken(0x0D, "Status");
            codePages[20].AddToken(0x0E, "Response");
            codePages[20].AddToken(0x0F, "Version");
            codePages[20].AddToken(0x10, "Schema");
            codePages[20].AddToken(0x11, "Part");
            codePages[20].AddToken(0x12, "EmptyFolderContents");
            codePages[20].AddToken(0x13, "DeleteSubFolders");
            codePages[20].AddToken(0x14, "UserName");
            codePages[20].AddToken(0x15, "Password");
            codePages[20].AddToken(0x16, "Move");
            codePages[20].AddToken(0x17, "DstFldId");
            codePages[20].AddToken(0x18, "ConversationId");
            codePages[20].AddToken(0x19, "MoveAlways");
            #endregion

            // Code Page 21: ComposeMail
            #region ComposeMail Code Page
            codePages[21] = new ASWBXMLCodePage();
            codePages[21].Namespace = "ComposeMail";
            codePages[21].Xmlns = "composemail";

            codePages[21].AddToken(0x05, "SendMail");
            codePages[21].AddToken(0x06, "SmartForward");
            codePages[21].AddToken(0x07, "SmartReply");
            codePages[21].AddToken(0x08, "SaveInSentItems");
            codePages[21].AddToken(0x09, "ReplaceMime");
            codePages[21].AddToken(0x0B, "Source");
            codePages[21].AddToken(0x0C, "FolderId");
            codePages[21].AddToken(0x0D, "ItemId");
            codePages[21].AddToken(0x0E, "LongId");
            codePages[21].AddToken(0x0F, "InstanceId");
            codePages[21].AddToken(0x10, "Mime");
            codePages[21].AddToken(0x11, "ClientId");
            codePages[21].AddToken(0x12, "Status");
            codePages[21].AddToken(0x13, "AccountId");
            #endregion

            // Code Page 22: Email2
            #region Email2 Code Page
            codePages[22] = new ASWBXMLCodePage();
            codePages[22].Namespace = "Email2";
            codePages[22].Xmlns = "email2";

            codePages[22].AddToken(0x05, "UmCallerID");
            codePages[22].AddToken(0x06, "UmUserNotes");
            codePages[22].AddToken(0x07, "UmAttDuration");
            codePages[22].AddToken(0x08, "UmAttOrder");
            codePages[22].AddToken(0x09, "ConversationId");
            codePages[22].AddToken(0x0A, "ConversationIndex");
            codePages[22].AddToken(0x0B, "LastVerbExecuted");
            codePages[22].AddToken(0x0C, "LastVerbExecutionTime");
            codePages[22].AddToken(0x0D, "ReceivedAsBcc");
            codePages[22].AddToken(0x0E, "Sender");
            codePages[22].AddToken(0x0F, "CalendarType");
            codePages[22].AddToken(0x10, "IsLeapMonth");
            codePages[22].AddToken(0x11, "AccountId");
            codePages[22].AddToken(0x12, "FirstDayOfWeek");
            codePages[22].AddToken(0x13, "MeetingMessageType");
            #endregion

            // Code Page 23: Notes
            #region Notes Code Page
            codePages[23] = new ASWBXMLCodePage();
            codePages[23].Namespace = "Notes";
            codePages[23].Xmlns = "notes";

            codePages[23].AddToken(0x05, "Subject");
            codePages[23].AddToken(0x06, "MessageClass");
            codePages[23].AddToken(0x07, "LastModifiedDate");
            codePages[23].AddToken(0x08, "Categories");
            codePages[23].AddToken(0x09, "Category");
            #endregion

            // Code Page 24: RightsManagement
            #region RightsManagement Code Page
            codePages[24] = new ASWBXMLCodePage();
            codePages[24].Namespace = "RightsManagement";
            codePages[24].Xmlns = "rightsmanagement";

            codePages[24].AddToken(0x05, "RightsManagementSupport");
            codePages[24].AddToken(0x06, "RightsManagementTemplates");
            codePages[24].AddToken(0x07, "RightsManagementTemplate");
            codePages[24].AddToken(0x08, "RightsManagementLicense");
            codePages[24].AddToken(0x09, "EditAllowed");
            codePages[24].AddToken(0x0A, "ReplyAllowed");
            codePages[24].AddToken(0x0B, "ReplyAllAllowed");
            codePages[24].AddToken(0x0C, "ForwardAllowed");
            codePages[24].AddToken(0x0D, "ModifyRecipientsAllowed");
            codePages[24].AddToken(0x0E, "ExtractAllowed");
            codePages[24].AddToken(0x0F, "PrintAllowed");
            codePages[24].AddToken(0x10, "ExportAllowed");
            codePages[24].AddToken(0x11, "ProgrammaticAccessAllowed");
            codePages[24].AddToken(0x12, "Owner");
            codePages[24].AddToken(0x13, "ContentExpiryDate");
            codePages[24].AddToken(0x14, "TemplateID");
            codePages[24].AddToken(0x15, "TemplateName");
            codePages[24].AddToken(0x16, "TemplateDescription");
            codePages[24].AddToken(0x17, "ContentOwner");
            codePages[24].AddToken(0x18, "RemoveRightsManagementDistribution");
            #endregion
            #endregion
        }   

        // This function takes an XML formatted string and loads
        // it into the parser.
        public void LoadXml(string xmlString)
        {
            XmlReader xmlReader = XmlReader.Create(new StringReader(xmlString));
            xmlDoc.Load(xmlReader);
        }

        // This function retrieves the current XML from the parser
        public string GetXml()
        {
            StringWriter stringWriter = new StringWriter();
            XmlTextWriter xmlWriter = new XmlTextWriter(stringWriter);
            xmlWriter.Formatting = Formatting.Indented;
            xmlDoc.WriteTo(xmlWriter);
            xmlWriter.Flush();

            return stringWriter.ToString();
        }

        // This function takes a WBXML byte array and loads it into the parser.
        // This overwrites any current XML loaded in the parser with the results
        // of parsing the WBXML stream.
        public void LoadBytes(byte[] byteWBXML)
        {
            xmlDoc = new XmlDocument();

            ASWBXMLByteQueue bytes = new ASWBXMLByteQueue(byteWBXML);

            // Version is ignored
            byte version = bytes.Dequeue();

            // Public Identifier is ignored
            int publicIdentifier = bytes.DequeueMultibyteInt();

            // Character set
            // Currently only UTF-8 is supported, throw if something else
            int charset = bytes.DequeueMultibyteInt();
            if (charset != 0x6A)
                throw new InvalidDataException("EX2010_activesyncfolder_cs only supports UTF-8 encoded XML.");

            // String table length
            // This should be 0, MS-ASWBXML does not use string tables
            int stringTableLength = bytes.DequeueMultibyteInt();
            if (stringTableLength != 0)
                throw new InvalidDataException("WBXML data contains a string table.");

            // Now we should be at the body of the data.
            // Add the declaration
            XmlDeclaration xmlDeclaration = xmlDoc.CreateXmlDeclaration("1.0", "utf-8", null);
            xmlDoc.InsertBefore(xmlDeclaration, null);

            XmlNode currentNode = xmlDoc;

            while (bytes.Count > 0)
            {
                byte currentByte = bytes.Dequeue();

                switch ((GlobalTokens)currentByte)
                {
                    // Check for a global token that is implemented
                    case GlobalTokens.SWITCH_PAGE:
                        // The next byte indicates which code page
                        // to switch to.
                        int newCodePage = (int)bytes.Dequeue();
                        if (newCodePage >= 0 && newCodePage < 25)
                        {
                            currentCodePage = newCodePage;
                        }
                        else
                        {
                            throw new InvalidDataException(string.Format(
                                "Unknown code page ID 0x{0:X} encountered in WBXML", currentByte));
                        }
                        break;
                    case GlobalTokens.END:
                        // This is the end of the current node, so
                        // move back up to the parent.
                        if (currentNode.ParentNode != null)
                        {
                            currentNode = currentNode.ParentNode;
                        }
                        else
                        {
                            throw new InvalidDataException(
                                "END global token encountered out of sequence");
                        }
                        break;
                    case GlobalTokens.OPAQUE:
                        int opaqueLength = bytes.DequeueMultibyteInt();

                        // In this implementation, CDATA is used to wrap
                        // values of elements that use the data type Byte 
                        // Array (MS-ASDTYPE section 2.6.1)
                        // The data in these are interpreted based on what 
                        // element they are in, so for convenience, special 
                        // case here and display the data in a user-friendly way
                        // This is purely to avoid doing it at the higher level for now.

                        string opaqueString = "";

                        switch (currentNode.Name)
                        {
                            // The Mime element contains a MIME stream which is human
                            // readable, so read it as a string.
                            case "Mime":
                                opaqueString = bytes.DequeueString(opaqueLength);
                                break;
                            // ConversationId and ConversationIndex are binary and
                            // not human readable in pure form. So for convenience, we will
                            // store these in XML as hexadecimal representation of the binary
                            case "ConversationId":
                            case "ConversationIndex":
                            default:
                                opaqueString = Utilities.PrintHex(bytes.DequeueBinary(opaqueLength));
                                break;
                        }

                        XmlCDataSection newOpaqueNode = xmlDoc.CreateCDataSection(opaqueString);
                        currentNode.AppendChild(newOpaqueNode);
                        break;
                    case GlobalTokens.STR_I:
                        XmlNode newTextNode = xmlDoc.CreateTextNode(bytes.DequeueString());
                        currentNode.AppendChild(newTextNode);
                        break;
                    // According to MS-ASWBXML, these features aren't used
                    case GlobalTokens.ENTITY:
                    case GlobalTokens.EXT_0:
                    case GlobalTokens.EXT_1:
                    case GlobalTokens.EXT_2:
                    case GlobalTokens.EXT_I_0:
                    case GlobalTokens.EXT_I_1:
                    case GlobalTokens.EXT_I_2:
                    case GlobalTokens.EXT_T_0:
                    case GlobalTokens.EXT_T_1:
                    case GlobalTokens.EXT_T_2:
                    case GlobalTokens.LITERAL:
                    case GlobalTokens.LITERAL_A:
                    case GlobalTokens.LITERAL_AC:
                    case GlobalTokens.LITERAL_C:
                    case GlobalTokens.PI:
                    case GlobalTokens.STR_T:
                        throw new InvalidDataException(string.Format("Encountered unknown global token 0x{0:X}.", currentByte));

                    // If it's not a global token, it should be a tag
                    default:
                        bool hasAttributes = false;
                        bool hasContent = false;

                        // Check the attributes and contents flags
                        hasAttributes = (currentByte & 0x80) > 0;
                        hasContent = (currentByte & 0x40) > 0;

                        // Determine the token
                        byte token = (byte)(currentByte & 0x3F);

                        if (hasAttributes)
                            // Per MS-ASWBXML, attributes are not implemented
                            throw new InvalidDataException(string.Format("Token 0x{0:X} has attributes.", token));

                        string tagName = codePages[currentCodePage].GetTag(token);
                        if (tagName == null)
                        {
                            // This shouldn't happen, but if it does, log
                            // the hex token for investigation.
                            tagName = string.Format("UNKNOWN_TAG_{0,2:X}", token);
                        }

                        XmlNode newNode = xmlDoc.CreateElement(tagName, codePages[currentCodePage].Namespace);
                        
                        if (xmlDoc.DocumentElement == null)
                        {
                            // First element, use this as the default namespace
                            defaultCodePage = currentCodePage;
                            currentNode.AppendChild(newNode);
                            xmlDoc.DocumentElement.SetAttribute("xmlns", codePages[currentCodePage].Namespace);
                        }
                        else
                        {
                            if (currentCodePage != defaultCodePage)
                            {
                                xmlDoc.DocumentElement.SetAttribute("xmlns:" + codePages[currentCodePage].Xmlns, codePages[currentCodePage].Namespace);
                                newNode.Prefix = codePages[currentCodePage].Xmlns;
                            }
                            currentNode.AppendChild(newNode);
                        }

                        // Only move our current node if this node actually has content.
                        // Otherwise, it's an empty node, so we won't be adding anything
                        // "under" it.
                        if (hasContent)
                        {
                            currentNode = newNode;
                        }
                        break;
                }
            }
        }

        // This function returns the current WBXML byte stream from the parser.
        public byte[] GetBytes()
        {
            List<byte> byteList = new List<byte>();

            // Add the WBXML header
            byteList.Add(versionByte);
            byteList.Add(publicIdentifierByte);
            byteList.Add(characterSetByte);
            byteList.Add(stringTableLengthByte);

            // Encode the XML nodes into the WBXML stream.
            foreach (XmlNode node in xmlDoc.ChildNodes)
            {
                byteList.AddRange(EncodeNode(node));
            }

            return byteList.ToArray();
        }

        // This function generates a byte array that represents
        // an XML node.
        private byte[] EncodeNode(XmlNode node)
        {
            List<byte> byteList = new List<byte>();

            switch (node.NodeType)
            {
                case XmlNodeType.Element:
                    if (node.Attributes.Count > 0)
                    {
                        // Check for any xmlns attributes
                        ParseXmlnsAttributes(node);
                    }

                    // Set the code page by the namespace
                    if (SetCodePageByXmlns(node.Prefix))
                    {
                        // SetCodePageByXmlns returns true if the code
                        // page switches.
                        byteList.Add((byte)GlobalTokens.SWITCH_PAGE);
                        byteList.Add((byte)currentCodePage);
                    }

                    byte token = codePages[currentCodePage].GetToken(node.LocalName);

                    if (node.HasChildNodes)
                    {
                        // Set the content flag
                        token |= 0x40;
                    }

                    byteList.Add(token);

                    if (node.HasChildNodes)
                    {
                        // Add child nodes to the WBXML stream
                        foreach (XmlNode child in node.ChildNodes)
                        {
                            byteList.AddRange(EncodeNode(child));
                        }

                        // Finally, write an END token
                        byteList.Add((byte)GlobalTokens.END);
                    }
                    break;
                case XmlNodeType.Text:
                    byteList.Add((byte)GlobalTokens.STR_I);
                    byteList.AddRange(EncodeString(node.Value));
                    break;
                case XmlNodeType.CDATA:
                    // In this implementation, CDATA is used to wrap
                    // values of elements that use the data type Byte 
                    // Array (MS-ASDTYPE section 2.6.1)
                    // The data in these are interpreted based on what 
                    // element they are in, so special case here and 
                    // handle the data based on how it is displayed.

                    byteList.Add((byte)GlobalTokens.OPAQUE);
                    switch (node.ParentNode.Name)
                    {
                        case "Mime":
                            // The Mime element contains a MIME stream which is human
                            // readable.
                            byteList.AddRange(EncodeStringAsOpaqueData(node.Value));
                            break;
                            // ConversationId and ConversationIndex are binary and
                            // not human readable in pure form. So for convenience, these
                            // are stored in XML as hexadecimal representation of the binary
                        case "ConversationId":
                        case "ConversationIndex":
                        default:
                            byteList.AddRange(EncodeHexStringAsOpaqueData(node.Value));
                            break;
                    }
                    break;
                default:
                    break;
            }

            return byteList.ToArray();
        }

        // This function looks up a code page that is assigned
        // to a particular xmlns attribute value.
        private int GetCodePageByXmlns(string xmlns)
        {
            for (int i = 0; i < codePages.Length; i++)
            {
                if (codePages[i].Xmlns.ToUpper() == xmlns.ToUpper())
                {
                    return i;
                }
            }

            return -1;
        }

        // This function looks up a code page that is assigned
        // to a particular namespace.
        private int GetCodePageByNamespace(string nameSpace)
        {
            for (int i = 0; i < codePages.Length; i++)
            {
                if (codePages[i].Namespace.ToUpper() == nameSpace.ToUpper())
                {
                    return i;
                }
            }

            return -1;
        }

        // This function sets the current code page by
        // doing a lookup by xmlns attribute value.
        private bool SetCodePageByXmlns(string xmlns)
        {
            if (xmlns == null || xmlns == "")
            {
                // Try default namespace
                if (currentCodePage != defaultCodePage)
                {
                    currentCodePage = defaultCodePage;
                    return true;
                }

                return false;
            }

            // Try current first
            if (codePages[currentCodePage].Xmlns.ToUpper() == xmlns.ToUpper())
            {
                return false;
            }

            for (int i = 0; i < codePages.Length; i++)
            {
                if (codePages[i].Xmlns.ToUpper() == xmlns.ToUpper())
                {
                    currentCodePage = i;
                    return true;
                }
            }

            throw new InvalidDataException(string.Format("Unknown Xmlns: {0}.", xmlns));
        }
        
        // This function parses attributes on an XML node
        // for any xmlns attributes. 
        private void ParseXmlnsAttributes(XmlNode node)
        {
            foreach (XmlAttribute attribute in node.Attributes)
            {
                char[] colon = { ':' };
                string attributeValue = attribute.Value.Trim(colon);
                
                // We can encounter one of two forms for the xmlns
                // attribute.
                // The first is used to set a default namespace, and looks
                // like: xmlns="AirSync"
                // The second is used to assign a prefix that serves as a 
                // placeholder for a namespace, and looks like:
                // xmlns:airsync="AirSync"
                if (attribute.Name.ToUpper() == "XMLNS")
                {
                    // If the Name property of the attribute is XMLNS, then
                    // this is the first form. Look up the code page
                    // and set it as the default.
                    int codePage = GetCodePageByNamespace(attributeValue);
                    defaultCodePage = codePage;
                }
                else if (attribute.Prefix.ToUpper() == "XMLNS")
                {
                    // If the Prefix property of the attribute is XMLNS,
                    // then this is the second form. Look up the code page
                    // and save the placeholder in the codepage's Xmlns
                    // property.
                    int codePage = GetCodePageByNamespace(attributeValue);
                    codePages[codePage].Xmlns = attribute.LocalName;
                }
            }
        }

        // This function encodes a string to WBXML
        private byte[] EncodeString(string value)
        {
            List<byte> byteList = new List<byte>();

            char[] charArray = value.ToCharArray();

            for (int i = 0; i < charArray.Length; i++)
            {
                byteList.Add((byte)charArray[i]);
            }

            byteList.Add(0x00);

            return byteList.ToArray();
        }

        // This function encodes a string to
        // WBXML opaque data.
        private byte[] EncodeStringAsOpaqueData(string value)
        {
            List<byte> byteList = new List<byte>();

            char[] charArray = value.ToCharArray();

            byteList.AddRange(EncodeMultiByteInteger(charArray.Length));

            for (int i = 0; i < charArray.Length; i++)
            {
                byteList.Add((byte)charArray[i]);
            }

            return byteList.ToArray();
        }

        // This function encodes a hexadecimal string representation
        // of binary data to WBXML opaque data.
        private byte[] EncodeHexStringAsOpaqueData(string value)
        {
            List<byte> byteList = new List<byte>();

            byteList.AddRange(EncodeMultiByteInteger(value.Length/2));

            byteList.AddRange(Utilities.ConvertHexToBytes(value));

            return byteList.ToArray();
        }

        // This function encodes an integer as a WBXML 
        // multi-byte integer.
        private byte[] EncodeMultiByteInteger(int value)
        {
            List<byte> byteList = new List<byte>();

            int shiftedValue = value;

            while (value > 0)
            {
                byte addByte = (byte)(value & 0x7F);

                if (byteList.Count > 0)
                {
                    addByte |= 0x80;
                }

                byteList.Insert(0, addByte);

                value >>= 7;
            }

            return byteList.ToArray();
        }
    }
}
