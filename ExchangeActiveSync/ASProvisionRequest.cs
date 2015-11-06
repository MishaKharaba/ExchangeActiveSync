using System;
using System.IO;
using System.Net;
using System.Xml;

namespace ExchangeActiveSync
{
    // This class represents a Provision command request
    // as specified in MS-ASPROV section 2.2.
    class ASProvisionRequest : ASCommandRequest
    {
        // This enumeration covers the acceptable values
        // of the Status element in a Provision request
        // when acknowledging a policy, as specified
        // in MS-ASPROV section 3.1.5.1.2.1.
        public enum PolicyAcknowledgement
        {
            Success = 1,
            PartialSuccess = 2,
            PolicyIgnored = 3,
            ExternalManagement = 4
        }

        private static string policyType = "MS-EAS-Provisioning-WBXML";

        private bool isAcknowledgement = false;
        private bool isRemoteWipe = false;
        private Int32 status = 0;

        private Device provisionDevice = null;

        #region Property Accessors

        public bool IsAcknowledgement
        {
            get
            {
                return isAcknowledgement;
            }
            set
            {
                isAcknowledgement = value;
            }
        }

        public bool IsRemoteWipe
        {
            get 
            {
                return isRemoteWipe;
            }
            set
            {
                isRemoteWipe = value;
            }
        }

        public Device ProvisionDevice
        {
            get
            {
                return provisionDevice;
            }
            set
            {
                provisionDevice = value;
            }
        }

        public Int32 Status
        {
            get
            {
                return status;
            }
            set
            {
                status = value;
            }
        }

        #endregion

        public ASProvisionRequest()
        {
            Command = "Provision";
        }

        // This function generates an ASProvisionResponse from an
        // HTTP response.
        protected override ASCommandResponse WrapHttpResponse(HttpWebResponse httpResp)
        {
            return new ASProvisionResponse(httpResp);
        }

        // This function generates the XML request body
        // for the Provision request.
        protected override void GenerateXMLPayload()
        {
            // If WBXML was explicitly set, use that
            if (WbxmlBytes != null)
                return;

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

            // The other two possibilities here are
            // an initial request or an acknowledgment
            // of a policy received in a previous Provision
            // response.
            else
            {
                if (!isAcknowledgement)
                {
                    // A DeviceInformation node is only included in the initial
                    // request.
                    XmlNode deviceNode = provisionXML.ImportNode(provisionDevice.GetDeviceInformationNode(), true);
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
                    policyKeyNode.InnerText = PolicyKey.ToString();
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

            XmlString = stringWriter.ToString();
        }
    }
}
