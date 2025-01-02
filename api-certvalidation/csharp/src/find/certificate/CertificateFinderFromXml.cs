using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.xml;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate
{
    public class CertificateFinderFromXml : CertificateFinder//*, ICloneable
    {
        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private  List<ECertificate> _certificates;
        private DateTime _readTime = DateTime.UtcNow;

        /*public Object Clone()
        {
            return base.MemberwiseClone();
        }*/

        /**
         * Find issuer certificate from file
         */
        protected override List<ECertificate> _findCertificate()
        {
            return _findCertificate(null);
        }

        protected override List<ECertificate> _findCertificate(ECertificate aSertifika)
        {
            EName name = null;

            if (aSertifika != null)
            {
                name = aSertifika.getIssuer();
            }
            return searchCertificates(name);
        }

        protected List<ECertificate> searchCertificates(EName issuer)
        {
            DateTime now = DateTime.UtcNow;

		    DateTime oneDayLaterAfterReading = _readTime.AddDays(1)/*.Clone()*/;

		    if(now > oneDayLaterAfterReading)
		    {
			    _certificates = null;
		    }


		    if(_certificates == null)
		    {
			    String storePath = mParameters.getParameterAsString(PARAM_STOREPATH);
			    try 
			    {
				    XMLStore store = null;
				    if(storePath != null)
				    {
					    Stream stream = BaglantiUtil.urldenStreamOku(storePath);
					    store = new XMLStore(stream);
				    }
				    else
				    {
                        Object storeStreamParam = mParameters.getParameter(PARAM_STORE_STREAM);
                        if((storeStreamParam != null) && (storeStreamParam is Stream)){
                            store = new XMLStore((Stream)storeStreamParam);
                        }
                        else
                        {
					        store = new XMLStore();
                        }
				    }
				    _certificates = store.getCertificates();
			    } 
			    catch (ESYAException e) 
			    {
				    Logger.Error("Xml sertifikası okunurken hata oluştu",e);
			    }

			    _readTime = DateTime.UtcNow;
		    } 

            if (_certificates == null)
            {
                return new List<ECertificate>();
            }

		    List<ECertificate> properCerts = new List<ECertificate>(_certificates);
		    foreach (ECertificate cert in _certificates)
		    {
			    byte [] subject = issuer.getEncoded();
			    if(subject != null)
			    {
				    if(cert.getSubject().getEncoded().SequenceEqual(subject))
					    properCerts.Remove(cert);
			    }
		    }
    		
		    return properCerts;
	    }
    }
}
