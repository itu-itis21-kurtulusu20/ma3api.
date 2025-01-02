using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.core
{

	using Logger = log4net.ILog;
	//using BaseASNWrapper = tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
	using EOCSPResponse = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
	using ECRL = tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using ValidationSystem = tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
	using CertificateCriteriaMatcher = tr.gov.tubitak.uekae.esya.api.signature.certval.match.CertificateCriteriaMatcher;
	using CertificateSearchCriteria = tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
    using CRLCriteriaMatcher = tr.gov.tubitak.uekae.esya.api.signature.certval.match.CRLCriteriaMatcher;
    using OCSPResponseCriteriaMatcher = tr.gov.tubitak.uekae.esya.api.signature.certval.match.OCSPResponseCriteriaMatcher;
	using CertValidationData = tr.gov.tubitak.uekae.esya.api.xmlsignature.CertValidationData;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using ValidationResultType = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using KeyInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo;
	using KeyInfoElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyInfoElement;
	using RetrievalMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.RetrievalMethod;
	using X509Data = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data;
	using CertID = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
	using Resolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using AsnIO = tr.gov.tubitak.uekae.esya.asn.util.AsnIO;


	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class ValidationDataCollector
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(ValidationDataCollector));

		// todo make false every time before commit
		private readonly bool debugWriteRevocationData = false;
		private string baseDir;
	    private ValidationInfoResolver referenceResolver;

		public virtual ValidationResult collect(XMLSignature aSignature, bool resolveReferencesFromExternalSources)
		{
            if(resolveReferencesFromExternalSources)
                referenceResolver = new ValidationInfoResolverFromCertStore();
            else
                referenceResolver = new ValidationInfoResolver();

            /*bool useExternalResources = true;
            SignatureType st = aSignature.SignatureType;
            if (st == SignatureType.XAdES_X_L || st == SignatureType.XAdES_A)
            {
                useExternalResources = !aSignature.Context.Config.ValidationConfig.ForceStrictReferences;
            }
		    ValidationSystem vs = aSignature.Context.getCertValidationSystem(useExternalResources);*/
			
            baseDir = aSignature.Context.BaseURIStr;

			ValidationResult r1 = fillValidationDataFromKeyInfo(aSignature/*, vs*/);
			if ((r1 != null) && (r1.getType() != ValidationResultType.VALID))
			{
				return r1;
			}
			ValidationResult r2 = fillValidationDataFromSignatureProperties(aSignature/*, vs*/);
			if ((r2 != null) && (r2.getType() != ValidationResultType.VALID))
			{
				return r2;
			}

			// collect validation data from counter signatures
			foreach (XMLSignature counterSignature in aSignature.AllCounterSignatures)
			{
				ValidationResult r3 = collect(counterSignature,resolveReferencesFromExternalSources);
				if ((r3 != null) && (r3.getType() != ValidationResultType.VALID))
				{
					return r3;
				}
			}

			return null;

		}

		public virtual ValidationResult fillValidationDataFromKeyInfo(XMLSignature aSignature/*, ValidationSystem vs*/)
		{
			CertValidationData vdata = aSignature.Context.getValidationData(aSignature);
			try
			{
				KeyInfo ki = aSignature.KeyInfo;
				for (int i = 0; i < ki.ElementCount; i++)
				{
					KeyInfoElement o = ki.get(i);

					if (o is X509Data)
					{
						X509Data data = (X509Data) o;
						bool criteriaSet = false;
						ECertificate certificate = null;
						CertificateSearchCriteria criteria = new CertificateSearchCriteria();
						for (int j = 0; j < data.ElementCount; j++)
						{
							X509DataElement xde = data.get(j);
							if (xde is X509Certificate)
							{
								X509Certificate cert = (X509Certificate) xde;
								certificate = new ECertificate(cert.CertificateBytes);
								vdata.addCertificate(certificate);
                                //TODO Debug validation data
								//writeResourceForDebug(certificate, "keyInfo" + i);
							}
							else if (xde is X509CRL)
							{
								X509CRL xcrl = (X509CRL) xde;
								vdata.addCRL(new ECRL(xcrl.CRLBytes));
							}
							else if (xde is X509IssuerSerial)
							{
								X509IssuerSerial xis = (X509IssuerSerial) xde;
							    criteria.setIssuer(xis.IssuerName);
                                criteria.setSerial(new BigInteger(xis.SerialNumber.ToByteArray(),1));
								criteriaSet = true;
							}
							else if (xde is X509SKI)
							{
								X509SKI xski = (X509SKI) xde;
							    criteria.setSubjectKeyIdentifier(xski.SKIBytes);
								criteriaSet = true;
							}
							else if (xde is X509SubjectName)
							{
								X509SubjectName xsn = (X509SubjectName) xde;
							    criteria.setSubject(xsn.SubjectName);
								criteriaSet = true;
							}
						}

						/*
					   Any X509IssuerSerial, X509SKI, and X509SubjectName elements that appear MUST
					   refer to the certificate or certificates containing the validation key. All
					   such elements that refer to a particular individual certificate MUST be grouped
					   inside a single X509Data element and if the certificate to which they refer
					   appears, it MUST also be in that X509Data element.
						*/
						if (criteriaSet)
						{
							if (certificate == null)
							{
								//IList<ECertificate> certs = vs.getFindSystem().searchCertificates(criteria);
							    IList<ECertificate> certs = referenceResolver.resolve(criteria);
								logger.Error("Cant find certificate referenced in KeyInfo");
								vdata.addCertificate(certs[0]);
                                //TOFO Write debug for
								//writeResourceForDebug(certs[0], "keyInfo_found_" + i);
							}
							else
							{
								// todo !
								/*
								CertificateCriteriaMatcher matcher = new CertificateCriteriaMatcher();
								if (!matcher.match(criteria, certificate)){
								    logger.error("Certificate referenced in KeyInfo does not match "+criteria);
								    throw new XMLSignatureException("Certificate referenced in KeyInfo does not metch "+criteria);
								} */
							}
						}

					}
					else if (o is RetrievalMethod)
					{
						// retrieval metod raxX509 a map eder
						ECertificate cert = new ECertificate(((RetrievalMethod) o).RawX509);
						vdata.addCertificate(cert);
                        //TODO write debug
						//writeResourceForDebug(cert, "keyInfo_retrieved_" + i);
					}

				}
			}
			catch (Exception x)
			{
				logger.Warn(x);
				return new ValidationResult(ValidationResultType.INCOMPLETE,
                                            I18n.translate("validation.check.keyInfo"),
                                            I18n.translate("core.invalidKeyInfo"),
                                            x.Message, GetType());
			}
			return null;
		}

		public virtual ValidationResult fillValidationDataFromSignatureProperties(XMLSignature aSignature/*, ValidationSystem vs*/)
		{

		    CertValidationData vdata = aSignature.Context.getValidationData(aSignature);

			QualifyingProperties qp = aSignature.QualifyingProperties;
			if (qp != null)
			{
				UnsignedSignatureProperties usp = qp.UnsignedSignatureProperties;
				if (usp != null)
				{

					// timestamp validation datas
					//aSignature.Context.ValidationData.TSValidationData = usp.AllTimeStampValidationData;
                    ICollection<XAdESTimeStamp> keys = usp.AllTimeStampValidationData.Keys;
                    foreach (XAdESTimeStamp key in keys)
                    {
                        aSignature.Context.getValidationData(aSignature).TSValidationData[key] = usp.AllTimeStampValidationData[key];
                    }

					// cert values
					CertificateValues certificateValues = usp.CertificateValues;
					if (certificateValues != null)
					{
						for (int j = 0; j < certificateValues.CertificateCount; j++)
						{
							try
							{
                                //TODO write debug
								//writeResourceForDebug(certificateValues.getCertificate(j), "cert_value_" + j);
								vdata.addCertificate(certificateValues.getCertificate(j));
							}
							catch (Exception x)
							{
								logger.Warn(x);
								return new ValidationResult(ValidationResultType.INCOMPLETE,
                                                            I18n.translate("validation.check.keyInfo"),
                                                            I18n.translate("validation.data.cantConstruct", j, "certificate", "CertificateValues"),
                                                            null, typeof(ValidationDataCollector));
							}
						}
					}

					// cert refs
					CompleteCertificateRefs certRefs = usp.CompleteCertificateRefs;
					if (certRefs != null)
					{

						CertificateCriteriaMatcher matcher = new CertificateCriteriaMatcher();

						for (int i = 0; i < certRefs.CertificateReferenceCount; i++)
						{
							CertID certID = certRefs.getCertificateReference(i);

							bool found = false;
							CertificateSearchCriteria csc = certID.toSearchCriteria();
							for (int k = 0; k < vdata.Certificates.Count;k++)
							{
								if (matcher.match(csc, vdata.Certificates[k]))
								{
									found = true;
									break;
								}
							}
							if (found)
							{
								continue;
							}

							string uri = certID.URI;
							if (uri != null && uri.Length > 0)
							{
								try
								{
									Document doc = Resolver.resolve(uri, aSignature.Context);
									ECertificate cert = new ECertificate(doc.Bytes);

									if (!matcher.match(certID.toSearchCriteria(), cert))
									{
                                        //TODO Debug
										//writeResourceForDebug(cert, "ref_mismatch_uri_" + uri);
										return new ValidationResult(ValidationResultType.INVALID,
                                                                    I18n.translate("validation.check.keyInfo"),
                                                                    I18n.translate("validation.data.resolutionMismatch", "certificate", uri, "CertID", certID),
                                                                    null, typeof(ValidationDataCollector));
									}
									else
									{
                                        //TODO Debug
										//writeResourceForDebug(cert, "ref_uri_" + uri);
									}
									vdata.addCertificate(cert);
								}
								catch (Exception x)
								{
									// todo Encapsulated Cert cozumu problemli...
									logger.Warn("Cant resolve certificate / issuer: " + certID.X509IssuerName + "; serial=" + certID.X509SerialNumber + " from URI: '" + uri + "'");
									logger.Warn(x);
									//return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve certificate / issuer: " + certID.getX509IssuerName() + "; serial=" + certID.getX509SerialNumber() + " from URI: '" + uri + "'");
								}
								continue;
							}
							try
							{

								//IList<ECertificate> foundCerts = vs.getFindSystem().searchCertificates(csc);
							    IList<ECertificate> foundCerts = referenceResolver.resolve(certID.toSearchCriteria());
								if (foundCerts != null && foundCerts.Count == 1)
								{
                                    //TODO Debug
									//writeResourceForDebug(foundCerts[0], "ref_found_" + i);
									vdata.addCertificate(foundCerts[0]);
									continue;
								}
							}
							catch (Exception x)
							{
								logger.Warn("Error occurred searching cert ", x);
							}
							// todo timestamp sertifikalarına bakmadan burada hata dönülemez...
							logger.Warn("Cant resolve certificate / issuer: " + certID.X509IssuerName + "; serial=" + certID.X509SerialNumber);
							//return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve certificate / issuer: " + certID.getX509IssuerName() + "; serial=" + certID.getX509SerialNumber());
						}
					}


					RevocationValues revocationValues = usp.RevocationValues;
					if (revocationValues != null)
					{
						for (int r = 0; r < revocationValues.CRLValueCount; r++)
						{
							try
							{
                                //TODO Debug
								//writeResourceForDebug(revocationValues.getCRL(r), "rev_value_" + r);
								vdata.addCRL(revocationValues.getCRL(r));
							}
							catch (Exception x)
							{
								logger.Warn(x);
								return new ValidationResult(ValidationResultType.INCOMPLETE,
                                                            I18n.translate("validation.check.keyInfo"),
                                                            I18n.translate("validation.data.cantConstruct", r, "CRL", "RevocationValues"),
                                                            null, typeof(ValidationDataCollector));
							}
						}
						for (int s = 0; s < revocationValues.OCSPValueCount; s++)
						{
							try
							{
                                //TODO Debug
								//writeResourceForDebug(revocationValues.getOCSPResponse(s), "rev_value_" + s);
								vdata.addOCSPResponse(revocationValues.getOCSPResponse(s));
							}
							catch (Exception x)
							{
								logger.Warn(x);
								return new ValidationResult(ValidationResultType.INCOMPLETE,
                                                            I18n.translate("validation.check.keyInfo"),
                                                            I18n.translate("validation.data.cantConstruct", s, "OCSP", "RevocationValues"),
                                                            null, typeof(ValidationDataCollector));
							}
						}
					}

					// revocation refs
					CompleteRevocationRefs revocationRefs = usp.CompleteRevocationRefs;

						if (revocationRefs != null)
						{
							OCSPResponseCriteriaMatcher ocspMatcher = new OCSPResponseCriteriaMatcher();
							CRLCriteriaMatcher crlMatcher = new CRLCriteriaMatcher();
						// ocsp refs
						for (int k = 0; k < revocationRefs.OCSPReferenceCount; k++)
						{
							OCSPReference ocspRef = revocationRefs.getOCSPReference(k);
							string uri = ocspRef.OCSPIdentifier.URI;
							if (uri != null && uri.Length > 0)
							{
								try
								{
									Document doc = Resolver.resolve(uri, aSignature.Context);
									EOCSPResponse ocsp = new EOCSPResponse(doc.Bytes);
									if (!ocspMatcher.match(ocspRef.toSearchCriteria(), ocsp))
									{
                                        //TODO Debug
										//writeResourceForDebug(ocsp, "ref_mismatch_uri_" + uri);
										return new ValidationResult(ValidationResultType.INVALID,
                                                                    I18n.translate("validation.check.keyInfo"),
                                                                    I18n.translate("validation.data.resolutionMismatch", "OCSP", uri, "OCSPRef", ocspRef),
                                                                    null, typeof(ValidationDataCollector));
									}
									else
									{
                                        //TODO Debug
										//writeResourceForDebug(ocsp, "ref_uri_" + uri);
									}

									vdata.addOCSPResponse(ocsp);
								}
								catch (Exception x)
								{
									// todo
									logger.Warn("Cant resolve ocsp response " + ocspRef + " from URI: '" + uri + "'");
									logger.Warn(x);
									//return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve ocsp response " + ocspRef + " from URI: '" + uri + "'");
								}
								continue;
							}
							try
							{
								// todo bulunan resp reference ile uyumlu mu, resp bulma
								//IList<EOCSPResponse> found = vs.getFindSystem().searchOCSPResponses(ocspRef.toSearchCriteria());
							    IList<EOCSPResponse> found = referenceResolver.resolve(ocspRef.toSearchCriteria());
								if (found != null && found.Count == 1)
								{
                                    //TODO Debug
									//writeResourceForDebug(found[0], "ref_found_" + k);
									vdata.addOCSPResponse(found[0]);
									continue;
								}
							}
							catch (Exception x)
							{
                                logger.Warn("Error occurred searching ocsp response ", x);
							}
							// todo 
							logger.Warn("Cant resolve ocsp response " + ocspRef);
							//return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve ocsp response " + ocspRef);
						}

						// crl refs
						for (int m = 0; m < revocationRefs.CRLReferenceCount; m++)
						{
							CRLReference crlReference = revocationRefs.getCRLReference(m);
							string uri = crlReference.CRLIdentifier.URI;
							if (uri != null && uri.Length > 0)
							{
								try
								{
									Document doc = Resolver.resolve(uri, aSignature.Context);
									ECRL crl = new ECRL(doc.Bytes);
									vdata.addCRL(crl);
									if (!crlMatcher.match(crlReference.toSearchCriteria(), crl))
									{
                                        //TODO Debug
										//logger.Error("Resolved CRL (uri='" + uri + "') mismatch CRLRef: " + crlReference);
										///writeResourceForDebug(crl, "ref_mismatch_uri_" + uri);
										return new ValidationResult(ValidationResultType.INVALID,
                                                                    I18n.translate("validation.check.keyInfo"),
                                                                    I18n.translate("validation.data.resolutionMismatch", "CRL", uri, "CRLRef", crlReference),
                                                                    null, typeof(ValidationDataCollector));
									}
									else
									{
                                        //TODO Debug
										//writeResourceForDebug(crl, "ref_uri_" + uri);
									}
								}
								catch (Exception x)
								{
									logger.Warn("Cant resolve CRL / " + crlReference + " from URI: '" + uri + "'");
									logger.Warn(x);
									//return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve CRL / " + crlReference + " from URI: '" + uri + "'");
								}
								continue;
							}
							try
							{
								// todo bulunan crl reference ile uyumlu mu, crl bulma
								//IList<ECRL> found = vs.getFindSystem().searchCRLs(crlReference.toSearchCriteria());
							    IList<ECRL> found = referenceResolver.resolve(crlReference.toSearchCriteria());
								if (found != null && found.Count == 1)
								{
                                    //TODO Debug
									//writeResourceForDebug(found[0], "ref_found_" + m);
									vdata.addCRL(found[0]);
									continue;
								}
							}
							catch (Exception x)
							{
								logger.Warn("Error occurred searching crl ", x);
							}
							// todo crlin elde olmaması mantıklı olabilir?
							logger.Warn("Cant resolve crl / " + crlReference);
							//return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve crl / " + crlReference);
						}
						}


				}
			}
			return null;
		}

        // 3 tane kullanilmayan fonksiyonu cevirmedim, onlar zaten ValidationInfoResolver icinde var sayilir


		private void writeResourceForDebug(/*BaseASNWrapper<tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate> resource, string aFileName*/)
		{
            //TODO revocation data debug yaz
            /*
			if (debugWriteRevocationData)
			{
				if (aFileName == null)
				{
					aFileName = System.currentTimeMillis() + "";
				}
				try
				{
					AsnIO.dosyayaz(resource.Encoded, baseDir + "\\debug_" + aFileName + "." + resource.GetType().SimpleName);
				}
				catch (Exception x)
				{
					Console.WriteLine(x.ToString());
					Console.Write(x.StackTrace);
				}
			}*/
		}
	}

}