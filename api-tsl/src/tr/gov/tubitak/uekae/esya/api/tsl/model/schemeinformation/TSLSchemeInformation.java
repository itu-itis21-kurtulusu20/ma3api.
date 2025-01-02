package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.math.BigInteger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLSchemeInformation extends BaseElement {

	private BigInteger versionId, sequenceNumber, historicalInformationPeriod;
	private String tslType, statusDeterminationApproach;

	private Element versionIdElement, sequenceNumberElement; // 1-2

	private TSLType tslTypeElement; // 3

	private TSLSchemeOperatorName schemeOperatorName; // 4
	private TSLSchemeOperatorAddress schemeOperatorAddress; // 5
	private TSLSchemeName schemeName; // 6
	private TSLSchemeInformationURI schemeInformationURI; // 7

	private Element statusDeterminationApproachElement; // 8

	private TSLSchemeTypeCommunityRules schemeTypeCommunityRules; // 9

	private TSLSchemeTerritory schemeTerritory; // 10

	private TSLPolicyOrLegalNotice policyOrLegalNotice; // 11

	private Element historicalInformationPeriodElement; // 12

	private PointersToOtherTSL pointersToOtherTSL; // 13

	private TSLListIssueDateTime issueDateTime; // 14

	private TSLNextUpdate nextUpdate; // 15

	private TSLDistributionPoints distributionPoints; // 16

	public TSLSchemeInformation(Document document) {
		super(document);
		addLineBreak();
	}

	public TSLSchemeInformation(Element aElement)
			throws XPathExpressionException, TSLException, ESYAException {
		super(aElement);
		NodeList nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_TSLVERSIONIDENTIFIER);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSLVERSIONIDENTIFIER		+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSLVERSIONIDENTIFIER+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		versionIdElement = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSLSEQUENCENUMBER);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSLSEQUENCENUMBER	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSLSEQUENCENUMBER+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		sequenceNumberElement = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_TSLTYPE);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSLTYPE+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSLTYPE	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		tslTypeElement = new TSLType((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_SCHEMEOPERATORNAME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SCHEMEOPERATORNAME		+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SCHEMEOPERATORNAME		+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		schemeOperatorName = new TSLSchemeOperatorName(
				(Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SCHEMEOPERATORADDRESS);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SCHEMEOPERATORADDRESS	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SCHEMEOPERATORADDRESS	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		schemeOperatorAddress = new TSLSchemeOperatorAddress((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SCHEMENAME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SCHEMENAME	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SCHEMENAME	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		schemeName = new TSLSchemeName((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SCHEMEINFORMATIONURI);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SCHEMEINFORMATIONURI	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SCHEMEINFORMATIONURI	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		schemeInformationURI = new TSLSchemeInformationURI(
				(Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSLSTATUSDETERMINATIONAPPROACH);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSLSTATUSDETERMINATIONAPPROACH	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSLSTATUSDETERMINATIONAPPROACH	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		statusDeterminationApproachElement = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SCHEMETYPECOMMUNITYRULES);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SCHEMETYPECOMMUNITYRULES	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SCHEMETYPECOMMUNITYRULES	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		schemeTypeCommunityRules = new TSLSchemeTypeCommunityRules((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SCHEMETERRITORY);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SCHEMETERRITORY	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SCHEMETERRITORY	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		schemeTerritory = new TSLSchemeTerritory((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_POLICYORLEGALNOTICE);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_POLICYORLEGALNOTICE		+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_POLICYORLEGALNOTICE		+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		policyOrLegalNotice = new TSLPolicyOrLegalNotice(	(Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_HISTORICALINFORMATIONPERIOD);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_HISTORICALINFORMATIONPERIOD	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_HISTORICALINFORMATIONPERIOD	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		historicalInformationPeriodElement = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_POINTERSTOOTHERTSL);

		if (nodeList != null && nodeList.getLength() != 0) {
			if (nodeList.getLength() > 1) {
				throw new TSLException(Constants.TAG_POINTERSTOOTHERTSL	+ TSL_DIL.NODE_MORE_THAN_ONE);
			}
			pointersToOtherTSL = new PointersToOtherTSL(	(Element) nodeList.item(0));
		}
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_LISTISSUEDATETIME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_LISTISSUEDATETIME	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_LISTISSUEDATETIME	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		issueDateTime = new TSLListIssueDateTime((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_NEXTUPDATE);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_NEXTUPDATE	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_NEXTUPDATE	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		nextUpdate = new TSLNextUpdate((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_DISTRIBUTIONPOINTS);

		if (nodeList != null && nodeList.getLength() != 0) {
			if (nodeList.getLength() > 1) {
				throw new TSLException(Constants.TAG_DISTRIBUTIONPOINTS	+ TSL_DIL.NODE_MORE_THAN_ONE);
			}
			distributionPoints = new TSLDistributionPoints(	(Element) nodeList.item(0));
		}

		versionId = new BigInteger(1, XmlUtil.getText(versionIdElement).getBytes());
		sequenceNumber = new BigInteger(1, XmlUtil.getText(	sequenceNumberElement).getBytes());
		tslType = XmlUtil.getText(tslTypeElement.getElement());
		statusDeterminationApproach = XmlUtil	.getText(statusDeterminationApproachElement);
		historicalInformationPeriod = new BigInteger(1, XmlUtil.getText(	historicalInformationPeriodElement).getBytes());
	}

	public Element getVersionID() // 1
	{
		return versionIdElement;
	}

	public void setVersionID(Element value) {
		versionIdElement = value;
		versionId = new BigInteger(1, versionIdElement.getTextContent()
				.getBytes());
		mElement.appendChild(versionIdElement);
		addLineBreak();
	}

	public Element getSequenceNumber() // 2
	{
		return sequenceNumberElement;
	}

	public void setSequenceNumber(Element value) {
		sequenceNumberElement = value;
		sequenceNumber = new BigInteger(1, sequenceNumberElement
				.getTextContent().getBytes());
		mElement.appendChild(sequenceNumberElement);
		addLineBreak();
	}

	public TSLType getTSLType() // 3
	{
		return tslTypeElement;
	}

	public void setTSLType(TSLType value) {
		tslTypeElement = value;
		mElement.appendChild(tslTypeElement.getElement());
		tslType = tslTypeElement.getTslType();
		addLineBreak();
	}

	public TSLSchemeOperatorName getSchemeOperatorName() // 4
	{
		return schemeOperatorName;
	}

	public void setSchemeOperatorName(TSLSchemeOperatorName value) {
		schemeOperatorName = value;
		mElement.appendChild(schemeOperatorName.getElement());
		addLineBreak();
	}

	public TSLSchemeOperatorAddress getSchemeOperatorAddress() // 5
	{
		return schemeOperatorAddress;
	}

	public void setSchemeOperatorAddress(TSLSchemeOperatorAddress value) {
		schemeOperatorAddress = value;
		mElement.appendChild(schemeOperatorAddress.getElement());
		addLineBreak();
	}

	public TSLSchemeName getSchemeName() // 6
	{
		return schemeName;
	}

	public void setSchemeName(TSLSchemeName value) {
		schemeName = value;
		mElement.appendChild(schemeName.getElement());
		addLineBreak();
	}

	public TSLSchemeInformationURI getSchemeInformationUri() // 7
	{
		return schemeInformationURI;
	}

	public void setSchemeInformationUri(TSLSchemeInformationURI value) {
		schemeInformationURI = value;
		mElement.appendChild(schemeInformationURI.getElement());
		addLineBreak();
	}

	public Element getStatusDeterminationApproach() // 8
	{
		return statusDeterminationApproachElement;
	}

	public void setStatusDeterminationApproach(Element value) {
		statusDeterminationApproachElement = value;
		mElement.appendChild(statusDeterminationApproachElement);
		statusDeterminationApproach = statusDeterminationApproachElement
				.getTextContent();
		addLineBreak();
	}

	public TSLSchemeTypeCommunityRules getSchemeTypeCommunityRules() // 9
	{
		return schemeTypeCommunityRules;
	}

	public void setSchemeTypeCommunityRules(TSLSchemeTypeCommunityRules value) {
		schemeTypeCommunityRules = value;
		mElement.appendChild(schemeTypeCommunityRules.getElement());
		addLineBreak();
	}

	public TSLSchemeTerritory getSchemeTerritory() // 10
	{
		return schemeTerritory;
	}

	public void setSchemeTerritory(TSLSchemeTerritory value) {
		schemeTerritory = value;
		mElement.appendChild(schemeTerritory.getElement());
		addLineBreak();
	}

	public TSLPolicyOrLegalNotice getPolicyOrLegalNotice() // 11
	{
		return policyOrLegalNotice;
	}

	public void setPolicyOrLegalNotice(TSLPolicyOrLegalNotice value) {
		policyOrLegalNotice = value;
		mElement.appendChild(policyOrLegalNotice.getElement());
		addLineBreak();
	}

	public Element getHistoricalInformationPeriod() // 12
	{
		return historicalInformationPeriodElement;
	}

	public void setHistoricalInformationPeriod(Element value) {
		historicalInformationPeriodElement = value;
		historicalInformationPeriod = new BigInteger(1,
				historicalInformationPeriodElement.getTextContent().getBytes());
		mElement.appendChild(historicalInformationPeriodElement);
		addLineBreak();
	}

	public PointersToOtherTSL getPointersToOtherTSL() // 13
	{
		return pointersToOtherTSL;
	}

	public void setPointersToOtherTSL(PointersToOtherTSL value) {
		pointersToOtherTSL = value;
		mElement.appendChild(pointersToOtherTSL.getElement());
		addLineBreak();
	}

	public TSLListIssueDateTime getListIssueDateTime() // 14
	{
		return issueDateTime;
	}

	public void setListIssueDateTime(TSLListIssueDateTime value) {
		issueDateTime = value;
		mElement.appendChild(issueDateTime.getElement());
		addLineBreak();
	}

	public TSLNextUpdate getNextUpdate() // 15
	{
		return nextUpdate;
	}

	public void setNextUpdate(TSLNextUpdate value) {
		nextUpdate = value;
		mElement.appendChild(nextUpdate.getElement());
		addLineBreak();
	}

	public TSLDistributionPoints getDistributionPoints() // 16
	{
		return distributionPoints;
	}

	public void setDistributionPoints(TSLDistributionPoints value) {
		distributionPoints = value;
		mElement.appendChild(distributionPoints.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMEINFORMATION;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;
	}

	public BigInteger getVersionIDBigInt() {
		return versionId;
	}

	public BigInteger getSequenceNumberBigInt() {
		return sequenceNumber;
	}

	public BigInteger getHistoricalInformationPeriodBigInt() {
		return historicalInformationPeriod;
	}

	public String getTSLTypeStr() {
		return tslType;
	}

	public String getStatusDeterminationApproachStr() {
		return statusDeterminationApproach;
	}
}
