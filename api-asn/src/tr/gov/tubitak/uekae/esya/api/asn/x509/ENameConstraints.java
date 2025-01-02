package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralSubtree;
import tr.gov.tubitak.uekae.esya.asn.x509.NameConstraints;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ahmety
 * date: Feb 1, 2010
 */
public class ENameConstraints extends BaseASNWrapper<NameConstraints>
{
    public ENameConstraints(NameConstraints aObject)
    {
        super(aObject);
    }

    public ENameConstraints(byte[] nameConstraintsBytes) throws ESYAException {
        super(nameConstraintsBytes,new NameConstraints());
    }

    public ENameConstraints(EGeneralSubtrees aPermitted, EGeneralSubtrees aExcluded)
    {
        super(new NameConstraints(aPermitted==null?null:aPermitted.getObject(), aExcluded==null?null:aExcluded.getObject()));
    }

    public List<EGeneralSubtree> getPermittedSubtrees(){
        int length = (mObject.permittedSubtrees != null) ? mObject.permittedSubtrees.elements.length : 0;
        List<EGeneralSubtree> permittedList = new ArrayList<EGeneralSubtree>(length);
        if (length>0){
            for (GeneralSubtree permitted :  mObject.permittedSubtrees.elements) {
                permittedList.add(new EGeneralSubtree(permitted));
            }
        }
        return permittedList;
    }

    public List<EGeneralSubtree> getExcludedSubtrees(){
        int length = (mObject.excludedSubtrees != null) ? mObject.excludedSubtrees.elements.length : 0;
        List<EGeneralSubtree> excludedList = new ArrayList<EGeneralSubtree>(length);
        if (length>0){
            for (GeneralSubtree excluded : mObject.excludedSubtrees.elements) {
                excludedList.add(new EGeneralSubtree(excluded));
            }
        }
        return excludedList;
    }

    public String stringValue(){
        return new BigInteger(getEncoded()).toString(16);
    }
    
    @Override
    public String toString() {
    	StringBuilder result = new StringBuilder();
    	result.append("Permitted\n");
    	List<EGeneralSubtree> subtrees = getPermittedSubtrees();
    	writeSubtrees(subtrees, result);
    	
    	result.append("Excluded\n");
    	subtrees = getExcludedSubtrees();
    	writeSubtrees(subtrees, result);
    	
        return result.toString();
    }
    
    private void writeSubtrees(List<EGeneralSubtree> subtrees,StringBuilder result){
    	int i =1;
    	for(EGeneralSubtree subtree : subtrees){
    		String max = (String) ((subtree.getMaximum() != null) ? subtree.getMaximum().toString() : "Max");
    		result.append("     [" +i+ "]Subtrees ("+ subtree.getMinimum()+"..."+ max+")\n");
    		result.append("          ").append(subtree.getBase().getObject().getElemName()+"="+subtree.getBase()+"\n");
    		i++;
    	}
    }
    public EExtension toExtension(boolean critic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_nameConstraints, critic, this);
    }

}
