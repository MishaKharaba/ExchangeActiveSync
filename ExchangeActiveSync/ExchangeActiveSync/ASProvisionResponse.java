//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASCommandResponse;
import ExchangeActiveSync.ASPolicy;

// This class represents the Provision command
// response specified in MS-ASPROV section 2.2.
public class ASProvisionResponse  extends ASCommandResponse 
{
    public enum ProvisionStatus
    {
        // This enumeration covers the Provision-
        // specific status values that can come from
        // the server.
        __dummyEnum__0,
        Success,
        SyntaxError,
        ServerError,
        __dummyEnum__1,
        __dummyEnum__2,
        __dummyEnum__3,
        __dummyEnum__4,
        __dummyEnum__5,
        __dummyEnum__6,
        __dummyEnum__7,
        __dummyEnum__8,
        __dummyEnum__9,
        __dummyEnum__10,
        __dummyEnum__11,
        __dummyEnum__12,
        __dummyEnum__13,
        __dummyEnum__14,
        __dummyEnum__15,
        __dummyEnum__16,
        __dummyEnum__17,
        __dummyEnum__18,
        __dummyEnum__19,
        __dummyEnum__20,
        __dummyEnum__21,
        __dummyEnum__22,
        __dummyEnum__23,
        __dummyEnum__24,
        __dummyEnum__25,
        __dummyEnum__26,
        __dummyEnum__27,
        __dummyEnum__28,
        __dummyEnum__29,
        __dummyEnum__30,
        __dummyEnum__31,
        __dummyEnum__32,
        __dummyEnum__33,
        __dummyEnum__34,
        __dummyEnum__35,
        __dummyEnum__36,
        __dummyEnum__37,
        __dummyEnum__38,
        __dummyEnum__39,
        __dummyEnum__40,
        __dummyEnum__41,
        __dummyEnum__42,
        __dummyEnum__43,
        __dummyEnum__44,
        __dummyEnum__45,
        __dummyEnum__46,
        __dummyEnum__47,
        __dummyEnum__48,
        __dummyEnum__49,
        __dummyEnum__50,
        __dummyEnum__51,
        __dummyEnum__52,
        __dummyEnum__53,
        __dummyEnum__54,
        __dummyEnum__55,
        __dummyEnum__56,
        __dummyEnum__57,
        __dummyEnum__58,
        __dummyEnum__59,
        __dummyEnum__60,
        __dummyEnum__61,
        __dummyEnum__62,
        __dummyEnum__63,
        __dummyEnum__64,
        __dummyEnum__65,
        __dummyEnum__66,
        __dummyEnum__67,
        __dummyEnum__68,
        __dummyEnum__69,
        __dummyEnum__70,
        __dummyEnum__71,
        __dummyEnum__72,
        __dummyEnum__73,
        __dummyEnum__74,
        __dummyEnum__75,
        __dummyEnum__76,
        __dummyEnum__77,
        __dummyEnum__78,
        __dummyEnum__79,
        __dummyEnum__80,
        __dummyEnum__81,
        __dummyEnum__82,
        __dummyEnum__83,
        __dummyEnum__84,
        __dummyEnum__85,
        __dummyEnum__86,
        __dummyEnum__87,
        __dummyEnum__88,
        __dummyEnum__89,
        __dummyEnum__90,
        __dummyEnum__91,
        __dummyEnum__92,
        __dummyEnum__93,
        __dummyEnum__94,
        __dummyEnum__95,
        __dummyEnum__96,
        __dummyEnum__97,
        __dummyEnum__98,
        __dummyEnum__99,
        __dummyEnum__100,
        __dummyEnum__101,
        __dummyEnum__102,
        __dummyEnum__103,
        __dummyEnum__104,
        __dummyEnum__105,
        __dummyEnum__106,
        __dummyEnum__107,
        __dummyEnum__108,
        __dummyEnum__109,
        __dummyEnum__110,
        __dummyEnum__111,
        __dummyEnum__112,
        __dummyEnum__113,
        __dummyEnum__114,
        __dummyEnum__115,
        __dummyEnum__116,
        __dummyEnum__117,
        __dummyEnum__118,
        __dummyEnum__119,
        __dummyEnum__120,
        __dummyEnum__121,
        __dummyEnum__122,
        __dummyEnum__123,
        __dummyEnum__124,
        __dummyEnum__125,
        __dummyEnum__126,
        __dummyEnum__127,
        __dummyEnum__128,
        __dummyEnum__129,
        __dummyEnum__130,
        __dummyEnum__131,
        __dummyEnum__132,
        __dummyEnum__133,
        __dummyEnum__134,
        __dummyEnum__135,
        DeviceNotFullyProvisionable,
        __dummyEnum__136,
        LegacyDeviceOnStrictPolicy,
        __dummyEnum__137,
        __dummyEnum__138,
        __dummyEnum__139,
        ExternallyManagedDevicesNotAllowed
    }
    private boolean isPolicyLoaded = false;
    private ASPolicy policy = null;
    private Int32 status = 0;
    public ASProvisionResponse(HttpWebResponse httpResponse) throws Exception {
        super(httpResponse);
        policy = new ASPolicy();
        isPolicyLoaded = policy.loadXML(getXmlString());
        setStatus();
    }

    public boolean getIsPolicyLoaded() throws Exception {
        return isPolicyLoaded;
    }

    public ASPolicy getPolicy() throws Exception {
        return policy;
    }

    public Int32 getStatus() throws Exception {
        return status;
    }

    // This function parses the response XML for
    // the Status element under the Provision element
    // and sets the status property according to the
    // value.
    private void setStatus() throws Exception {
        XmlDocument responseXml = new XmlDocument();
        responseXml.LoadXml(getXmlString());
        XmlNamespaceManager xmlNsMgr = new XmlNamespaceManager(responseXml.NameTable);
        xmlNsMgr.AddNamespace("provision", "Provision");
        XmlNode provisionNode = responseXml.SelectSingleNode(".//provision:Provision", xmlNsMgr);
        XmlNode statusNode = null;
        if (provisionNode != null)
            statusNode = provisionNode.SelectSingleNode(".//provision:Status", xmlNsMgr);
         
        if (statusNode != null)
            status = XmlConvert.ToInt32(statusNode.InnerText);
         
    }

}


