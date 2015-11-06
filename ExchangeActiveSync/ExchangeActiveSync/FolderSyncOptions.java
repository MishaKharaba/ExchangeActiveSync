//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.BodyPreferences;
import ExchangeActiveSync.ConflictResolution;
import ExchangeActiveSync.MimeSupport;
import ExchangeActiveSync.MimeTruncationType;
import ExchangeActiveSync.SyncFilterType;

// This class represents the sync options
// that are included under the <Options> element
// in a Sync command request.
public class FolderSyncOptions   
{
    public SyncFilterType FilterType = SyncFilterType.NoFilter;
    public ConflictResolution ConflictHandling = ConflictResolution.LetServerDecide;
    public MimeTruncationType MimeTruncation = MimeTruncationType.NoTruncate;
    public String Class = null;
    public Int32 MaxItems = -1;
    public BodyPreferences[] BodyPreference = null;
    public BodyPreferences BodyPartPreference = null;
    public boolean IsRightsManagementSupported = false;
    public MimeSupport MimeSupportLevel = MimeSupport.NeverSendMime;
}


