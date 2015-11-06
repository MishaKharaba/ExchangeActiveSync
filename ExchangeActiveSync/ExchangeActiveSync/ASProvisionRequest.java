//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;

import ExchangeActiveSync.ASCommandRequest;
import ExchangeActiveSync.ASCommandResponse;
import ExchangeActiveSync.ASProvisionResponse;
import ExchangeActiveSync.Device;
import ExchangeActiveSync.Namespaces;
import ExchangeActiveSync.Xmlns;

// This class represents a Provision command request
// as specified in MS-ASPROV section 2.2.
public class ASProvisionRequest  extends ASCommandRequest 
{
    public enum PolicyAcknowledgement
    {
        // This enumeration covers the acceptable values
        // of the Status element in a Provision request
        // when acknowledging a policy, as specified
        // in MS-ASPROV section 3.1.5.1.2.1.
        __dummyEnum__0,
        Success,
        PartialSuccess,
        PolicyIgnored,
        ExternalManagement
    }
    private static String policyType = "MS-EAS-Provisioning-WBXML";
    private boolean isAcknowledgement = false;
    private boolean isRemoteWipe = false;
    private Int32 status = 0;
    private Device provisionDevice = null;
    public boolean getIsAcknowledgement() throws Exception {
        return isAcknowledgement;
    }

    public void setIsAcknowledgement(boolean value) throws Exception {
        isAcknowledgement = value;
    }

    public boolean getIsRemoteWipe() throws Exception {
        return isRemoteWipe;
    }

    public void setIsRemoteWipe(boolean value) throws Exception {
        isRemoteWipe = value;
    }

    public Device getProvisionDevice() throws Exception {
        return provisionDevice;
    }

    public void setProvisionDevice(Device value) throws Exception {
        provisionDevice = value;
    }

    public Int32 getStatus() throws Exception {
        return status;
    }

    public void setStatus(Int32 value) throws Exception {
        status = value;
    }

    public ASProvisionRequest() throws Exception {
        setCommand("Provision");
    }

    // This function generates an ASProvisionResponse from an
    // HTTP response.
    protected ASCommandResponse wrapHttpResponse(HttpWebResponse httpResp) throws Exception {
        return new ASProvisionResponse(httpResp);
    }

    // This function generates the XML request body
    // for the Provision request.
    protected void generateXMLPayload() throws Exception {
        // If WBXML was explicitly set, use that
        if (getWbxmlBytes() != null)
            return ;
         
        // Otherwise, use the properties to build the XML and then WBXML encode it
        XmlDocument provisionXML = new XmlDocument();
        XmlDeclaration xmlDeclaration = provisionXML.CreateXmlDeclaration("1.0", "utf-8", null);
        provisionXML.InsertBefore(xmlDeclaration, null);
        XmlNode provisionNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "Provision", Namespaces.provisionNamespace);
        provisionNode.Prefix = Xmlns.provisionXmlns;
        provisionXML.AppendChild(provisionNode);
        // If this is a remote wipe acknowledgment, use
        // the remote wipe acknowledgment format
        // specified in MS-ASPROV section 3.1.5.1.2.2.
        if (isRemoteWipe)
        {
            // Build response to RemoteWipe request
            XmlNode remoteWipeNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "RemoteWipe", Namespaces.provisionNamespace);
            remoteWipeNode.Prefix = Xmlns.provisionXmlns;
            provisionNode.AppendChild(remoteWipeNode);
            // Always return success for remote wipe
            XmlNode statusNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "Status", Namespaces.provisionNamespace);
            statusNode.Prefix = Xmlns.provisionXmlns;
            statusNode.InnerText = "1";
            remoteWipeNode.AppendChild(statusNode);
        }
        else
        {
            // The other two possibilities here are
            // an initial request or an acknowledgment
            // of a policy received in a previous Provision
            // response.
            if (!isAcknowledgement)
            {
                // A DeviceInformation node is only included in the initial
                // request.
                XmlNode deviceNode = provisionXML.ImportNode(provisionDevice.getDeviceInformationNode(), true);
                provisionNode.AppendChild(deviceNode);
            }
             
            // These nodes are included in both scenarios.
            XmlNode policiesNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "Policies", Namespaces.provisionNamespace);
            policiesNode.Prefix = Xmlns.provisionXmlns;
            provisionNode.AppendChild(policiesNode);
            XmlNode policyNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "Policy", Namespaces.provisionNamespace);
            policyNode.Prefix = Xmlns.provisionXmlns;
            policiesNode.AppendChild(policyNode);
            XmlNode policyTypeNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "PolicyType", Namespaces.provisionNamespace);
            policyTypeNode.Prefix = Xmlns.provisionXmlns;
            policyTypeNode.InnerText = policyType;
            policyNode.AppendChild(policyTypeNode);
            if (isAcknowledgement)
            {
                // Need to also include policy key and status
                // when acknowledging
                XmlNode policyKeyNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "PolicyKey", Namespaces.provisionNamespace);
                policyKeyNode.Prefix = Xmlns.provisionXmlns;
                policyKeyNode.InnerText = getPolicyKey().ToString();
                policyNode.AppendChild(policyKeyNode);
                XmlNode statusNode = provisionXML.CreateElement(Xmlns.provisionXmlns, "Status", Namespaces.provisionNamespace);
                statusNode.Prefix = Xmlns.provisionXmlns;
                statusNode.InnerText = status.ToString();
                policyNode.AppendChild(statusNode);
            }
             
        } 
        StringWriter stringWriter = new StringWriter();
        XmlTextWriter xmlWriter = new XmlTextWriter(stringWriter);
        xmlWriter.Formatting = Formatting.Indented;
        provisionXML.WriteTo(xmlWriter);
        xmlWriter.Flush();
        setXmlString(stringWriter.ToString());
    }

}


