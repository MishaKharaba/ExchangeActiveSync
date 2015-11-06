using System;

namespace ExchangeActiveSync
{
    // This enumeration is derived from the list of common
    // statuscodes in [MS-ASCMD] section 2.2.4.
    enum ASStatusCodes
    {
        InvalidContent = 101,
        InvalidWBXML = 102,
        InvalidXML = 103,
        InvalidDateTime = 104,
        InvalidCombinationOfIDs = 105,
        InvalidIDs = 106,
        InvalidMIME = 107,
        DeviceIdMissingOrInvalid = 108,
        DeviceTypeMissingOrInvalid = 109,
        ServerError = 110,
        ServerErrorRetryLater = 111,
        ActiveDirectoryAccessDenied = 112,
        MailboxQuotaExceeded = 113,
        MailboxServerOffline = 114,
        SendQuotaExceeded = 115,
        MessageRecipientUnresolved = 116,
        MessageReplyNotAllowed = 117,
        MessagePreviouslySent = 118,
        MessageHasNoRecipient = 119,
        MailSubmissionFailed = 120,
        MessageReplyFailed = 121,
        AttachmentIsTooLarge = 122,
        UserHasNoMailbox = 123,
        UserCannotBeAnonymous = 124,
        UserPrincipalCouldNotBeFound = 125,
        UserDisabledForSync = 126,
        UserOnNewMailboxCannotSync = 127,
        UserOnLegacyMailboxCannotSync = 128,
        DeviceIsBlockedForThisUser = 129,
        AccessDenied = 130,
        AccountDisabled = 131,
        SyncStateNotFound = 132,
        SyncStateLocked = 133,
        SyncStateCorrupt = 134,
        SyncStateAlreadyExists = 135,
        SyncStateVersionInvalid = 136,
        CommandNotSupported = 137,
        VersionNotSupported = 138,
        DeviceNotFullyProvisionable = 139,
        RemoteWipeRequested = 140,
        LegacyDeviceOnStrictPolicy = 141,
        DeviceNotProvisioned = 142,
        PolicyRefresh = 143,
        InvalidPolicyKey = 144,
        ExternallyManagedDevicesNotAllowed = 145,
        NoRecurrenceInCalendar = 146,
        UnexpectedItemClass = 147,
        RemoteServerHasNoSSL = 148,
        InvalidStoredRequest = 149,
        ItemNotFound = 150,
        TooManyFolders = 151,
        NoFoldersFound = 152,
        ItemsLostAfterMove = 153,
        FailureInMoveOperation = 154,
        MoveCommandDisallowedForNonPersistentMoveAction = 155,
        MoveCommandInvalidDestinationFolder = 156,
        AvailabilityTooManyRecipients = 160,
        AvailabilityDLLimitReached = 161,
        AvailabilityTransientFailure = 162,
        AvailabilityFailure = 163,
        BodyPartPreferenceTypeNotSupported = 164,
        DeviceInformationRequired = 165,
        InvalidAccountId = 166,
        AccountSendDisabled = 167,
        IRM_FeatureDisabled = 168,
        IRM_TransientError = 169,
        IRM_PermanentError = 170,
        IRM_InvalidTemplateID = 171,
        IRM_OperationNotPermitted = 172,
        NoPicture = 173,
        PictureTooLarge = 174,
        PictureLimitReached = 175,
        BodyPart_ConversationTooLarge = 176,
        MaximumDevicesReached = 177
    }

    class ASError
    {
        // This method simply displayes the exception to the user.
        public static void ReportException(Exception ex)
        {
            Console.WriteLine(ex.Message);
            Console.WriteLine(ex.TargetSite.ToString());
        }
    }
}
