using System;
using System.Net;
using System.Xml;

namespace ExchangeActiveSync
{
    // This class represents the Provision command
    // response specified in MS-ASPROV section 2.2.
    class ASProvisionResponse : ASCommandResponse 
    {
        // This enumeration covers the Provision-
        // specific status values that can come from
        // the server.
        public enum ProvisionStatus
        {
            Success = 1,
            SyntaxError = 2,
            ServerError = 3,
            DeviceNotFullyProvisionable = 139,
            LegacyDeviceOnStrictPolicy = 141,
            ExternallyManagedDevicesNotAllowed = 145
        }

        private bool isPolicyLoaded = false;
        private ASPolicy policy = null;
        private Int32 status = 0;

        public ASProvisionResponse(HttpWebResponse httpResponse) : base (httpResponse)
        {
            policy = new ASPolicy();
            isPolicyLoaded = policy.LoadXML(XmlString);
            SetStatus();
        }

        #region Property Accessors

        public bool IsPolicyLoaded
        {
            get
            {
                return isPolicyLoaded;
            }
        }

        public ASPolicy Policy
        {
            get
            {
                return policy;
            }
        }

        public Int32 Status
        {
            get
            {
                return status;
            }
        }

        #endregion

        // This function parses the response XML for
        // the Status element under the Provision element
        // and sets the status property according to the
        // value.
        private void SetStatus()
        {
            XmlDocument responseXml = new XmlDocument();
            responseXml.LoadXml(XmlString);

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
}
