package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.util.*;


/**
 * Utility class for Human readable OR address defined in RFC2156 and X402 Annex-F.
 * 
 * @author dogukan.dogan
 *
 */
public final class OrAddressUtility {
	
	/** Empty String. */
	private static final String EMPTY_STRING = "";

	/** Slash Delimiter. */
	private static final String DELIMITER_SLASH = "/";
	
	/** Semicolon delimiter. */
	private static final String DELIMITER_SEMICOLON = ";";
	
	/** Default delimiter. */
	private static final String DEFAULT_DELIMITER = DELIMITER_SEMICOLON;
	
	/** Given name definition. */
	private static final String GIVEN_NAME = "G";
	
	/** Initials definition. */
	private static final String INITIALS = "I";
	
	/** Surname definition. */
	private static final String SURNAME = "S";
	
	/** Generation qualifier definition. */
	private static final String GENERATION_QUALIFIER = "Q";
	
	/** Organization definition. */
	private static final String ORGANIZATION = "O";
	
	/** Organizational unit definition. */
	private static final String ORGANIZATIONAL_UNIT = "OU";
	
	/** Private domain name definition. */
	private static final String PRIVATE_DOMAIN_NAME = "PRMD";
	
	/** Private domain name alternative definition. */
	private static final String PRIVATE_DOMAIN_NAME_ALT = "P";
	
	/** Administrative domain name definition. */
	private static final String ADMINISTRATIVE_DOMAIN_NAME = "ADMD";
	
	/** Administrative domain name alternative definition.*/
	private static final String ADMINISTRATIVE_DOMAIN_NAME_ALT = "A";
	
	/** Country definition. */
	private static final String COUNTRY = "C";

	/** Common name definition. */
	private static final String COMMON_NAME = "CN";
	
	/** Key value splitter. */
	private static final String KEY_VALUE_SPLITTER = "=";

	/** CN attribute ID. */
	private static final long CN_ATTRIBUTE_ID = 1;
	
	
	/**
	 * Creates OR Address from human readable <code>String</code> form. Conversion is defined in RFC2156 Section 4.1 
	 * and X402 Annex-F.
	 * 
	 * @param orAddressString OR Address String
	 * @return OR Address
	 */
	public static ORAddress createOrAddressFromString(String orAddressString) {
		ORAddress orAddress = new ORAddress();
		Map<String, String> orAddressComponents = parseORAddressComponents(orAddressString);
		
		orAddress.built_in_standard_attributes = readBuiltInStandardAttributes(orAddressComponents);
		orAddress.built_in_domain_defined_attributes = readBuiltInDomainDefinedAttributes(orAddressComponents);
		orAddress.extension_attributes = readExtensionAttributes(orAddressComponents);
		
		validateOrAddress(orAddress);
		return orAddress;
	}
	
	
	/**
	 * Validates given OR Address.
	 * 
	 * @param orAddress OR address
	 */
	private static void validateOrAddress(ORAddress orAddress) {
		//Encode and decode given OR Address..
		try {
			// FIXME Encode method can be called from a utility.. 
			Asn1BerEncodeBuffer encodeBuff = new Asn1BerEncodeBuffer();
			orAddress.encode(encodeBuff, true);
			Asn1OpenType openType = new Asn1OpenType(encodeBuff.getMsgCopy());
			
			// FIXME Decode method can be called from a utility.. 
			Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(openType.value);
			ORAddress decodedOrAddress = new ORAddress();
			decodedOrAddress.decode(buffer);
		} catch (Exception e) {
			throw new IllegalArgumentException("Syntax problem.", e);
		}
	}


	/**
	 * Parses O/R address attributes from the supplied string representation of
	 * the O/R Address.
	 * 
	 * @param orAddressAsString
	 *            String representation of the O/R Address<br/>
	 *            Example of valid strings that can be converted to an O/R
	 *            Address:<br/> {@code "cn=test-user; ou=test-ou; o=test-o; c=tr"}
	 *            {@code "cn=test-user; ou1=test-ou1; ou2=test-ou2; p=test-p; a=test-a}
	 * @return map that contains the attribute type value list pairs
	 * 
	 * @author cevher.bozkur
	 */
	private static Map<String, String> parseORAddressComponents(String orAddressAsString) {
		String delimiter = findDelimiter(orAddressAsString);
		Map<String, String> result = new LinkedHashMap<String, String>();
		if (orAddressAsString.startsWith(delimiter)) {
			orAddressAsString = orAddressAsString.substring(1);
		}
		
		String[] firstLevelKeys = orAddressAsString.split(delimiter);
		
		for (String firstLevelKey : firstLevelKeys) {
			String[] keyValue = firstLevelKey.split(KEY_VALUE_SPLITTER);
			if (keyValue.length != 2) {
				throw new IllegalArgumentException("Invalid key value pair in OR Address string: "
						+ keyValue);
			}
			String canonicKey = keyValue[0].trim().toUpperCase(Locale.ENGLISH);
			
			/*
			if (!attributeTypeKeysList.contains(canonicKey)) {
				throw new ORAddressParseException(
						"Invalid attribute type in the OR Address string : " + canonicKey);
			}
			*/
			
			if (result.containsKey(canonicKey)) {
				throw new IllegalArgumentException("Attribute : " + canonicKey
						+ " occurs twice in the supplied string.");
			}
			result.put(canonicKey, processValue(keyValue[1]));
		}
		return result;
	}
	
	
	/**
	 * Process string value.
	 * 
	 * @param value canonical value
	 * @return processed value
	 */
	private static String processValue(String value) {
		String processedValue = value.trim();
		if (processedValue.isEmpty()) { return " "; }
		
		//TODO execute other process operations.
		return value;
	}


	/**
	 * Finds proper delimiter for parsing Or Address String.
	 * 
	 * @param orAddressAsString OR Address String value
	 * @return delimiter used in given value
	 */
	private static String findDelimiter(String orAddressAsString) {
		if (orAddressAsString.contains(DELIMITER_SEMICOLON)) {
			return DELIMITER_SEMICOLON;
		} else if (orAddressAsString.contains(DELIMITER_SLASH)) {
			return DELIMITER_SLASH;
		} else {
			throw new IllegalArgumentException("Unknown delimiter.");
		}
	}
	

	/**
	 * Reads built in standard attributes.
	 * 
	 * @param orAddressComponents OR address components.
	 * @return Built-in-standard attributes
	 */
	private static BuiltInStandardAttributes readBuiltInStandardAttributes(Map<String, String> orAddressComponents) {
		BuiltInStandardAttributes standartAttributes = new BuiltInStandardAttributes();
		standartAttributes.country_name = (readCountryName(orAddressComponents));
		standartAttributes.administration_domain_name  = (readAdministrationDomainName(orAddressComponents));
		
		standartAttributes.private_domain_name = (readPrivateDomainName(orAddressComponents));
		
		standartAttributes.organization_name = (readOrganizationName(orAddressComponents));
		standartAttributes.organizational_unit_names = (readOrganizationUnitNames(orAddressComponents));
		
		standartAttributes.personal_name = (readPersonalName(orAddressComponents));
		
		return standartAttributes;
	}

	
	/**
	 * Reads personal name.
	 * 
	 * @param orAddressComponents OR address components
	 * @return personal name
	 */
	private static PersonalName readPersonalName(Map<String, String> orAddressComponents) {
		String surname = orAddressComponents.get(SURNAME);
		if (surname == null) { return null; }
		
		PersonalName personalName = new PersonalName();
		personalName.surname = new Asn1PrintableString(surname);
		personalName.given_name = (readGivenName(orAddressComponents));
		personalName.initials = (readInitials(orAddressComponents));
		personalName.generation_qualifier = (readGenerationQualifier(orAddressComponents));
		return personalName;
	}


	/**
	 * Reads generation qualifier.
	 * 
	 * @param orAddressComponents OR address components
	 * @return Generation qualifier value
	 */
	private static Asn1PrintableString readGenerationQualifier(Map<String, String> orAddressComponents) {
		String generationQualifier = orAddressComponents.get(GENERATION_QUALIFIER);
		if (generationQualifier == null) { return null; }
		return new Asn1PrintableString(generationQualifier);
	}


	/**
	 * Reads initials.
	 * 
	 * @param orAddressComponents OR Address components
	 * @return Initials value
	 */
	private static Asn1PrintableString readInitials(Map<String, String> orAddressComponents) {
		String initial = orAddressComponents.get(INITIALS);
		if (initial == null) { return null; }
		return new Asn1PrintableString(initial);
	}


	/**
	 * Reads given name.
	 * 
	 * @param orAddressComponents OR Address components
	 * @return Given name value
	 */
	private static Asn1PrintableString readGivenName(Map<String, String> orAddressComponents) {
		String givenName = orAddressComponents.get(GIVEN_NAME);
		if (givenName == null) { return null; }
		return new Asn1PrintableString(givenName);
	}


	/**
	 * Reads organizational unit names.
	 * 
	 * @param orAddressComponents OR address components
	 * @return Organizational unit names.
	 */
	private static OrganizationalUnitNames readOrganizationUnitNames(Map<String, String> orAddressComponents) {
		List<Asn1PrintableString> organizationUnitNamesList = new ArrayList<Asn1PrintableString>();
		for (String key : orAddressComponents.keySet()) {
			if (key.startsWith(ORGANIZATIONAL_UNIT)) {
				String nameValue = orAddressComponents.get(key);
				if (nameValue != null) {
					organizationUnitNamesList.add(new Asn1PrintableString(nameValue));
				}
			}
		}
		if (organizationUnitNamesList.isEmpty()) { return null; }

        Asn1PrintableString[] unitNamesArray = new Asn1PrintableString[organizationUnitNamesList.size()];
		organizationUnitNamesList.toArray(unitNamesArray);
		return new OrganizationalUnitNames(unitNamesArray);
	}

	
	/**
	 * Reads organization name.
	 * 
	 * @param orAddressComponents Or address components.
	 * @return Organization name
	 */
	private static Asn1PrintableString readOrganizationName(Map<String, String> orAddressComponents) {
		String organization = orAddressComponents.get(ORGANIZATION);
		if (organization == null) { return null; }
		return new Asn1PrintableString(organization);
	}
	
	
	/**
	 * Reads private domain name.
	 * 
	 * @param orAddressComponents OR address components
	 * @return Private domain name
	 */
	private static PrivateDomainName readPrivateDomainName(Map<String, String> orAddressComponents) {
		String privateDomainName = orAddressComponents.get(PRIVATE_DOMAIN_NAME);
		if (privateDomainName == null) {
			privateDomainName = orAddressComponents.get(PRIVATE_DOMAIN_NAME_ALT);
			if (privateDomainName == null) { return null; }
		}
		if (isNumericString(privateDomainName)) {
			return new PrivateDomainName(PrivateDomainName._NUMERIC, new Asn1NumericString(privateDomainName));
		}
		return new PrivateDomainName(PrivateDomainName._PRINTABLE, new Asn1PrintableString(privateDomainName));
	}


	/**
	 * Reads administration domain name.
	 * 
	 * @param orAddressComponents OR address components
	 * @return Administration domain name
	 */
	private static AdministrationDomainName readAdministrationDomainName(Map<String, String> orAddressComponents) {
		String admdName = orAddressComponents.get(ADMINISTRATIVE_DOMAIN_NAME);
		if (admdName == null) {
			admdName = orAddressComponents.get(ADMINISTRATIVE_DOMAIN_NAME_ALT);
			if (admdName == null) { return null; }
		}
		if (isNumericString(admdName)) {
			return new AdministrationDomainName(AdministrationDomainName._NUMERIC, new Asn1NumericString(admdName));
		}
		return new AdministrationDomainName(AdministrationDomainName._PRINTABLE, new Asn1PrintableString(admdName));
	}


	/**
	 * Reads country name.
	 * 
	 * @param orAddressComponents OR address components
	 * @return Country name
	 */
	private static CountryName readCountryName(Map<String, String> orAddressComponents) {
		String countryString = orAddressComponents.get(COUNTRY);
		if (countryString == null) { return null; }
		
		if (isNumericString(countryString)) {
			return new CountryName(CountryName._X121_DCC_CODE, new Asn1NumericString(countryString));
		}
		return new CountryName(CountryName._ISO_3166_ALPHA2_CODE, new Asn1PrintableString(countryString));
	}


	/**
	 * Reads Extension attributes.
	 * 
	 * @param orAddressComponents OR address components
	 * @return Extension attributes
	 */
	private static ExtensionAttributes readExtensionAttributes(Map<String, String> orAddressComponents) {
		String cnStringValue = orAddressComponents.get(COMMON_NAME);
		if (cnStringValue != null) {
			ExtensionAttribute cnAttribute = new ExtensionAttribute(CN_ATTRIBUTE_ID, 
					createCommonNameOpenTypeValue(new Asn1PrintableString(cnStringValue)));
			
			ExtensionAttribute[] attributes = { cnAttribute };
			return new ExtensionAttributes(attributes);
		}
		return null;
	}

	
	/**
	 * This method can be called from a utility method such as asn1Type2Asn1OpenType.
	 * 
	 * @param cnValue Common name value as printable string
	 * @return Encoded open type value of common name
	 */
	private static Asn1OpenType createCommonNameOpenTypeValue(Asn1PrintableString cnValue) {
		Asn1BerEncodeBuffer encodeBuff = new Asn1BerEncodeBuffer();
		try {
			cnValue.encode(encodeBuff, true);
		} catch (Asn1Exception e) {
			throw new IllegalArgumentException("Wrong formatted Common Name value: " + cnValue.value, e);
		}
		return new Asn1OpenType(encodeBuff.getMsgCopy());
	}
	

	/**
	 * Reads built in domain defined attributes.
	 * 
	 * @param orAddressComponents
	 *            OR address components
	 * @return Built in domain defined attributes
	 */
	private static BuiltInDomainDefinedAttributes readBuiltInDomainDefinedAttributes(
			Map<String, String> orAddressComponents) {
		
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * Checks that given value is a numeric <code>String</code>.
	 * 
	 * @param value String value
	 * @return true if numeric <code>String</code>, otherwise false
	 */
	private static boolean isNumericString(String value) {
		for (int index = 0; index < value.length(); index++) {
			if (!Character.isDigit(value.charAt(index))) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Creates human readable OR Address <code>String</code> form. Conversion is defined in RFC2156 Section 4.1 
	 * and X402 Annex-F.
	 * 
	 * @param orAddress OR Address value
	 * @return Human readable OR Address <code>String</code>
	 */
	public static String createOrAddressString(ORAddress orAddress) {
		BuiltInStandardAttributes standartAttributes = orAddress.built_in_standard_attributes;
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(createPersonalNameString(standartAttributes.personal_name));
		
		buffer.append(createCommonNameString(orAddress.extension_attributes));

		buffer.append(createOrganizationalUnitNameString(standartAttributes.organizational_unit_names));
		buffer.append(createOrganizationNameString(standartAttributes.organization_name));
		
		buffer.append(createPrivateDomainNameStringValue(standartAttributes.private_domain_name));
		buffer.append(createAdministrativeDomainNameString(standartAttributes.administration_domain_name));
		
		buffer.append(createCountryNameString(standartAttributes.country_name));
		return processResultString(buffer.toString());
	}
	
	

	/**
	 * Creates common name string value.
	 * 
	 * @param extensionAttributes extension attributes.
	 * @return common name string value
	 */
	private static String createCommonNameString(ExtensionAttributes extensionAttributes) {
		if (extensionAttributes == null) { return EMPTY_STRING; }
		for (ExtensionAttribute attribute : extensionAttributes.elements) {
			if (CN_ATTRIBUTE_ID == attribute.extension_attribute_type.value) {
				return createAttributeTypeAndValueString(COMMON_NAME, 
						readCommonNameStringValue(attribute.extension_attribute_value));
			}
		}
		return EMPTY_STRING;
	}


	/**
	 * Reads common name string value.
	 * 
	 * @param openTypeValue Common name open type value
	 * @return Common name string value
	 */
	private static String readCommonNameStringValue(Asn1OpenType openTypeValue) {
		// FIXME value can be printable string, telex string, universal string or combination of these..
		
		// return ((Asn1PrintableString) asn1OpenType2Asn1Type(Asn1PrintableString.class, openTypeValue)).value;
		return readCommonName(openTypeValue).value;
	}


	/**
	 * This method can be called from a utility method such as asn1OpenType2Asn1Type method.
	 * 
	 * @param openTypeValue common name open type value
	 * @return Common name value as Asn1 Printable String
	 */
	private static Asn1PrintableString readCommonName(Asn1OpenType openTypeValue) {
		Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(openTypeValue.value);
		Asn1PrintableString commonName = new Asn1PrintableString();
		try {
			commonName.decode(buffer);
		} catch (Exception e) {
			throw new IllegalArgumentException("Wrong formatted value.", e);
		}
		return commonName;
	}


	/**
	 * Removes last delimiters.
	 * 
	 * When the delimiter is "/" the first label is prefixed by "/". X402 Annex-F 3.2.1
	 * 
	 * @param result result string
	 * @return processed result string
	 */
	private static String processResultString(String result) {
		if (result.endsWith(DEFAULT_DELIMITER)) {
			result = result.substring(0, result.length() - 1);
		}
		
		if (DELIMITER_SLASH.equals(DEFAULT_DELIMITER)) {
			return DELIMITER_SLASH + result;
		}
		return result;
	}
	

	/**
	 * Creates organization name <code>String</code> value.
	 * 
	 * @param organizationName Organization name
	 * @return Organization name string
	 */
	private static String createOrganizationNameString(Asn1PrintableString organizationName) {
		if (organizationName == null) { return EMPTY_STRING; }
		return createAttributeTypeAndValueString(ORGANIZATION, organizationName.value);
	}


	/**
	 * Creates private domain name <code>String</code> value.
	 * 
	 * @param privateDomainName Private Domain name
	 * @return Private domain name string
	 */
	private static String createPrivateDomainNameStringValue(PrivateDomainName privateDomainName) {
		if (privateDomainName == null) { return EMPTY_STRING; }
		return createAttributeTypeAndValueString(PRIVATE_DOMAIN_NAME, privateDomainName.getElement().toString());
	}


	/**
	 * Creates administration domain name <code>String</code> value.
	 * 
	 * @param administrationDomainName Administration Domain name
	 * @return Administration domain name string
	 */
	private static String createAdministrativeDomainNameString(AdministrationDomainName administrationDomainName) {
		if (administrationDomainName == null) { return EMPTY_STRING; }
		return createAttributeTypeAndValueString(ADMINISTRATIVE_DOMAIN_NAME, 
				administrationDomainName.getElement().toString());
	}


	/**
	 * Creates country name <code>String</code> value.
	 * 
	 * @param countryName Country name
	 * @return Country name string
	 */
	private static String createCountryNameString(CountryName countryName) {
		if (countryName == null) { return EMPTY_STRING; }
		return createAttributeTypeAndValueString(COUNTRY, countryName.getElement().toString());
	}


	/**
	 * Creates organizational unit name string.
	 * 
	 * @param organizationalUnitNames organizational unit names
	 * @return organizational unit names string
	 */
	private static String createOrganizationalUnitNameString(OrganizationalUnitNames organizationalUnitNames) {
		if (organizationalUnitNames == null) { return EMPTY_STRING; }
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i <= organizationalUnitNames.elements.length; i++) {
			buffer.append(createAttributeTypeAndValueString(ORGANIZATIONAL_UNIT.concat(String.valueOf(i)), 
					organizationalUnitNames.elements[i - 1].toString()));
		}
		return buffer.toString();
	}
	

	/**
	 * Creates person name string.
	 * 
	 * @param personalName personal name information
	 * @return String representation of person name
	 */
	private static String createPersonalNameString(PersonalName personalName) {
		if (personalName == null) { return EMPTY_STRING; }
		
		StringBuffer buffer = new StringBuffer();
		if (personalName.given_name != null) {
			buffer.append(createAttributeTypeAndValueString(GIVEN_NAME, 
					personalName.given_name.toString()));
		}
		
		if (personalName.initials != null) {
			buffer.append(createAttributeTypeAndValueString(INITIALS, personalName.initials.toString()));
		}
		buffer.append(createAttributeTypeAndValueString(SURNAME, personalName.surname.toString()));
		
		if (personalName.generation_qualifier != null) {
			buffer.append(createAttributeTypeAndValueString(GENERATION_QUALIFIER, 
					personalName.generation_qualifier.toString()));
		}
		return buffer.toString();
	} 
	
	
	/**
	 * Creates string with given type and value.
	 * 
	 * @param type attribute type string
	 * @param value attribute value as string
	 * @return String representation of attribute type and value definition
	 */
	private static String createAttributeTypeAndValueString(String type, String value) {
		return type + KEY_VALUE_SPLITTER + value + DEFAULT_DELIMITER;
	}

	
	
	/**
	 * Private constructor.
	 */
	private OrAddressUtility() {
	}
}
