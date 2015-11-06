//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.BodyType;

// This class represents body or body part
// preferences included in a <BodyPreference> or
// <BodyPartPreference> element.
public class BodyPreferences   
{
    public BodyType Type = BodyType.NoType;
    public UInt32 TruncationSize = 0;
    public boolean AllOrNone = false;
    public Int32 Preview = -1;
}


