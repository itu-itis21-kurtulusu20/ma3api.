package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLServiceDigitalIdentities extends BaseElement {
	List<TSLServiceDigitalIdentity> serviceDigitalIdentityList = new ArrayList<TSLServiceDigitalIdentity>();

	public TSLServiceDigitalIdentities(Document document,
			List<TSLServiceDigitalIdentity> iServiceDigitalIdentityList) {
		super(document);
		serviceDigitalIdentityList = iServiceDigitalIdentityList;

		addLineBreak();
		for (TSLServiceDigitalIdentity tslServiceDigitalIdentity : serviceDigitalIdentityList) {
			mElement.appendChild(tslServiceDigitalIdentity.getElement());
			addLineBreak();
		}
	}

	public TSLServiceDigitalIdentities(Document document) {
		super(document);
		addLineBreak();
	}

	public TSLServiceDigitalIdentities(Element aElement) throws TSLException, ESYAException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				serviceDigitalIdentityList.add(new TSLServiceDigitalIdentity(
						element));
			}
		} else {
			throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITIES
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void addServiceDigitalIdentity(
			TSLServiceDigitalIdentity iServiceDigitalIdentity) {
		serviceDigitalIdentityList.add(iServiceDigitalIdentity);
		mElement.appendChild(iServiceDigitalIdentity.getElement());
		addLineBreak();
	}

	public void addServiceDigitalIdentityCert(ECertificate iCertificate) {
		TSLServiceDigitalIdentity serviceDigitalIdentity = new TSLServiceDigitalIdentity(
				mElement.getOwnerDocument(), iCertificate);
		serviceDigitalIdentityList.add(serviceDigitalIdentity);
		mElement.appendChild(serviceDigitalIdentity.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SERVICEDIGITALIDENTITIES;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public List<TSLServiceDigitalIdentity> getServiceDigitalIdentityList() {
		return serviceDigitalIdentityList;
	}

	public TSLServiceDigitalIdentity ServiceDigitalIdentityAt(int pos) {
		if (pos >= serviceDigitalIdentityList.size()) {
			return null;
		} else {
			return serviceDigitalIdentityList.get(pos);
		}
	}

	public ECertificate ServiceDigitalIdentityCertificateAt(int pos) {
		if (pos >= serviceDigitalIdentityList.size()) {
			return null;
		} else {
			return serviceDigitalIdentityList.get(pos).getX509Certificate();
		}
	}
}
