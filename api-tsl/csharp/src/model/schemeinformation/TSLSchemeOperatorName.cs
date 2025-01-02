using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLSchemeOperatorName : BaseElement
    {
        private IList<TSLName> operatorNames = new List<TSLName>(); 

        public TSLSchemeOperatorName(XmlDocument document, IList<TSLName> iOperatorNames) : base(document)
        {
            addLineBreak();
            operatorNames = iOperatorNames;

            for(int i=0;i<operatorNames.Count;i++)
            {
                mElement.AppendChild(operatorNames[i].Element);
                addLineBreak();
            }
        }

        public TSLSchemeOperatorName(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSLSchemeOperatorName(XmlElement aElement) : base(aElement)
        {
            if(mElement.HasChildNodes)
            {
                XmlNodeList childList = aElement.ChildNodes;
                foreach (XmlNode child in childList)
                {
                    operatorNames.Add(new TSLName((XmlElement)child));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SCHEMEOPERATORNAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllOperatorNames()
        {
            mElement.RemoveAll();
            operatorNames.Clear();
            addLineBreak();
        }

        public void addOperatorName(TSLName tslName)
        {
            operatorNames.Add(tslName);
            mElement.AppendChild(tslName.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SCHEMEOPERATORNAME; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLName> OperatorNames
        {
            get { return operatorNames; }
            set
            {
                mElement.RemoveAll();
                addLineBreak();
                operatorNames = value;

                for (int i = 0; i < operatorNames.Count; i++)
                {
                    mElement.AppendChild(operatorNames[i].Element);
                    addLineBreak();
                }
            }
        }
    }
}
