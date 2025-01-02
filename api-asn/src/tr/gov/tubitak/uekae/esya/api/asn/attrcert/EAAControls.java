package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1Boolean;
import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AAControls;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttrSpec;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 4/26/11 - 2:40 PM <p>
 <b>Description</b>: <br>
 */
public class EAAControls extends BaseASNWrapper<AAControls> {

    public EAAControls(AAControls aObject) {
        super(aObject);
    }

    public EAAControls(byte[] aBytes) throws ESYAException {
        super(aBytes, new AAControls());
    }

    public EAAControls() {
        super(new AAControls());
    }

    public void setPathLenConstraint(long pathLenConstraint) {
        mObject.pathLenConstraint = new Asn1Integer(pathLenConstraint);
    }

    public void setPermitUnSpecified(boolean permitUnSpecified) {
        mObject.permitUnSpecified = new Asn1Boolean(permitUnSpecified);
    }

    public void setPermittedAttrs(Asn1ObjectIdentifier[] permittedAttrs) {
        mObject.permittedAttrs = new AttrSpec(permittedAttrs);
    }

    public void setExcludedAttrs(Asn1ObjectIdentifier[] excludedAttrs) {
        mObject.excludedAttrs = new AttrSpec(excludedAttrs);
    }

    public long getPathLenConstraint() {
        return mObject.pathLenConstraint.value;
    }

    public Asn1ObjectIdentifier[] getPermittedAttrs() {
        return mObject.permittedAttrs.elements;
    }

    public Asn1ObjectIdentifier[] getExcludedAttrs() {
        return mObject.excludedAttrs.elements;
    }

    public boolean isPermitUnSpecified() {
        return mObject.permitUnSpecified.value;
    }
}
