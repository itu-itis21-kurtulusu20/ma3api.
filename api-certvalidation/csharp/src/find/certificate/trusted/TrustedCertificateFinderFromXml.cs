using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.infra.cache;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.xml;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted
{
    public class TrustedCertificateFinderFromXml : TrustedCertificateFinder
    {
        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);


        private static Object syncObject = new Object();

        private static FixedSizedCache<String, List<ECertificate>> cache = new FixedSizedCache<String, List<ECertificate>>(20, TimeSpan.FromHours(6));



        protected override List<ECertificate> _findTrustedCertificate()
        {
            string finderConfig = mParameters.ToString();
            lock (syncObject)
            {
                List<ECertificate> trustedCertificates = cache.Get(finderConfig);

                if (trustedCertificates == null || trustedCertificates.Count == 0)
                {
                    trustedCertificates = new List<ECertificate>();
                    try
                    {
                        String storePath = mParameters.getParameterAsString(PARAM_STOREPATH);
                        XMLStore store = null;
                        if (storePath != null)
                        {
                            Logger.Debug("store path is: " + storePath);
                            if (storePath.Substring(0, 4).Equals("http"))
                            {
                                Logger.Debug("store path is http");
                                Stream stream = BaglantiUtil.urldenStreamOku(storePath);
                                store = new XMLStore(stream);
                            }
                            else
                            {
                                Logger.Debug("store path is not http, probably local file");
                                store = new XMLStore(storePath);
                            }
                        }
                        else
                        {
                            Object storeStreamParam = mParameters.getParameter(PARAM_STORE_STREAM);
                            if ((storeStreamParam != null) && (storeStreamParam is Stream))
                            {
                                Logger.Debug("store stream is: " + storeStreamParam);
                                store = new XMLStore((Stream) storeStreamParam);
                            }
                            else
                            {
                                Logger.Debug(
                                    "no path or stream is found, xmlstore is being build from scratch at user directory");
                                store = new XMLStore();
                            }
                        }

                        List<DepoKokSertifika> depoKokSertifikaList = store.getTrustedCertificates();

                        List<SecurityLevel> guvenlikSeviyeleri = getSecurityLevel();

                        foreach (DepoKokSertifika depoKokSertifika in depoKokSertifikaList)
                        {
                            SecurityLevel guvenSeviyesi = depoKokSertifika.getKokGuvenSeviyesi();
                            if (guvenlikSeviyeleri.Contains(guvenSeviyesi))
                            {
                                bool sonuc = CertStoreUtil.verifyDepoKokSertifika(depoKokSertifika);
                                if (sonuc == true)
                                    trustedCertificates.Add(new ECertificate(depoKokSertifika.getValue()));
                            }
                        }

                        cache.Add(finderConfig, trustedCertificates);
                    }
                    catch (ESYAException e)
                    {
                        Logger.Error("Xml sertifikası okunurken hata oluştu", e);
                    }
                    catch (CertStoreException e)
                    {
                        Logger.Error("Kök sertifika doğrulanırken hata oluştu", e);
                    }
                }

                return trustedCertificates;
            }
        }
    }
}
