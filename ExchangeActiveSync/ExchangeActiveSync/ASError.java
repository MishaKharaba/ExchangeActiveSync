//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;


public class ASError   
{
    // This method simply displayes the exception to the user.
    public static void reportException(Exception ex) throws Exception {
        Console.WriteLine(ex.Message);
        Console.WriteLine(ex.TargetSite.ToString());
    }

}


