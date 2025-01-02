package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import org.omg.CORBA.PUBLIC_MEMBER;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/21/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 * The structure is as follows:
 * <ul>
 * <li>username</li>
 * <li>password</li>
 * <li>viewname</li>
 * <li>ocspsigncheck</li>
 * <li>hmackey</li>
 * <li>dbproperties</li>
 * </ul>
 */
public class DbProviderConfigElement extends AbstractConfigElement implements IStatusProviderConfigElement {
	private boolean wrapUserNamePassword;
	public static int NEXTUPDATEMIN = 10;

	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.USERNAME, OcspConfigTags.PASSWORD, OcspConfigTags.VIEWNAME, OcspConfigTags.OCSPSIGNCHECK, OcspConfigTags.HMAC, OcspConfigTags.DBPROPERTIES, OcspConfigTags.ISESYA20, OcspConfigTags.OCSPLOG, OcspConfigTags.NEXTUPDATE, OcspConfigTags.NEXTUPDATEMINUTE, OcspConfigTags.SAVERESPONSE};

	public DbProviderConfigElement(String username, String password, String viewName, boolean ocspSignCheck, String hmacKey, String dbProperties, boolean isEsya20, boolean wrapUserNamePassword, boolean ocspLog, boolean nextUpdate, int nextUpdateMin, boolean saveResponse) {
		map.put(OcspConfigTags.USERNAME, username);
		map.put(OcspConfigTags.PASSWORD, password);
		map.put(OcspConfigTags.VIEWNAME, viewName);
		map.put(OcspConfigTags.OCSPSIGNCHECK, Boolean.toString(ocspSignCheck));
		map.put(OcspConfigTags.HMAC, hmacKey);
		map.put(OcspConfigTags.DBPROPERTIES, dbProperties);
		map.put(OcspConfigTags.ISESYA20, Boolean.toString(isEsya20));
		map.put(OcspConfigTags.OCSPLOG, Boolean.toString(ocspLog));
		map.put(OcspConfigTags.NEXTUPDATE, Boolean.toString(nextUpdate));
		map.put(OcspConfigTags.NEXTUPDATEMINUTE, Integer.toString(nextUpdateMin));
		map.put(OcspConfigTags.SAVERESPONSE, Boolean.toString(saveResponse));
		this.wrapUserNamePassword = wrapUserNamePassword;
	}

	public DbProviderConfigElement() {

	}

	public void wrapSensitiveData(BufferedCipher cipher) throws Exception {
		String username = map.get(OcspConfigTags.USERNAME);
		String password = map.get(OcspConfigTags.PASSWORD);
		String hmacKey = map.get(OcspConfigTags.HMAC);
		if (wrapUserNamePassword){
			if (username!=null && !username.trim().equals("")){
				cipher.reset();
				username = Base64.encode(cipher.doFinal(username.getBytes("UTF-8")));
				map.put(OcspConfigTags.USERNAME, username);
			}

			if (password!=null && !password.trim().equals("")){
				cipher.reset();
				password = Base64.encode(cipher.doFinal(password.getBytes("UTF-8")));
				map.put(OcspConfigTags.PASSWORD, password);
			}
		}

		if (hmacKey!=null && !hmacKey.trim().equals("")){
			cipher.reset();
			hmacKey =  Base64.encode(cipher.doFinal(hmacKey.getBytes("UTF-8")));
			map.put(OcspConfigTags.HMAC, hmacKey);
		}
	}


	public boolean isWrapUserNamePassword() {
		return wrapUserNamePassword;
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.DBPROVIDER;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}

	public boolean isMultiple() {
		return true;
	}

	public String getUserName(){
		return  map.get(OcspConfigTags.USERNAME);
	}

	public String getPassword(){
		return map.get(OcspConfigTags.PASSWORD);
	}

	public String getViewName(){
		return map.get(OcspConfigTags.VIEWNAME);
	}

	public boolean ocspSignCheck(){
		return Boolean.parseBoolean(map.get(OcspConfigTags.OCSPSIGNCHECK));
	}

	public String getHmacKey(){
		return map.get(OcspConfigTags.HMAC);
	}

	public boolean isEsya20(){
		return Boolean.parseBoolean(map.get(OcspConfigTags.ISESYA20));
	}

	public String getDbProperties(){
		return map.get(OcspConfigTags.DBPROPERTIES);
	}

	public boolean isOcspLog(){
		if (map.get(OcspConfigTags.OCSPLOG) != null){
			return Boolean.parseBoolean(map.get(OcspConfigTags.OCSPLOG));
		}
		return false;
	}

	public boolean isNextUpdate() {
		if (map.get(OcspConfigTags.NEXTUPDATE) != null){
			return Boolean.parseBoolean(map.get(OcspConfigTags.NEXTUPDATE));
		}
		return false;
	}

	public int getNextUpdateMinute() {
		if (map.get(OcspConfigTags.NEXTUPDATEMINUTE) != null){
			return Integer.parseInt(map.get(OcspConfigTags.NEXTUPDATEMINUTE));
		}
		return NEXTUPDATEMIN;
	}

	public boolean isSaveResponse() {
		if (map.get(OcspConfigTags.SAVERESPONSE) != null){
			return Boolean.parseBoolean(map.get(OcspConfigTags.SAVERESPONSE));
		}
		return false;
	}
}
