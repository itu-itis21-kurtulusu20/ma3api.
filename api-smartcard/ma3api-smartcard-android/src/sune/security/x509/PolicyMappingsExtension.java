/*
 * Copyright 1997-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package sune.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import sune.security.util.DerOutputStream;

/**
 * Represent the Policy Mappings Extension.
 *
 * This extension, if present, identifies the certificate policies considered
 * identical between the issuing and the subject CA.
 * <p>Extensions are addiitonal attributes which can be inserted in a X509
 * v3 certificate. For example a "Driving License Certificate" could have
 * the driving license number as a extension.
 *
 * <p>Extensions are represented as a sequence of the extension identifier
 * (Object Identifier), a boolean flag stating whether the extension is to
 * be treated as being critical and the extension value itself (this is again
 * a DER encoding of the extension value).
 *
 * @author Amit Kapoor
 * @author Hemma Prafullchandra
 * @see sune.security.x509.Extension
 * @see sune.security.x509.CertAttrSet
 */
public class PolicyMappingsExtension extends Extension
implements CertAttrSet<String> {
    /**
     * Identifier for this attribute, to be used with the
     * get, set, delete methods of Certificate, x509 type.
     */
    public static final String IDENT = "x509.info.extensions.PolicyMappings";
    /**
     * Attribute names.
     */
    public static final String NAME = "PolicyMappings";
    public static final String MAP = "map";

    // Private data members
    private List<sune.security.x509.CertificatePolicyMap> maps;

    // Encode this extension value
    private void encodeThis() throws IOException {
        if (maps == null || maps.isEmpty()) {
            this.extensionValue = null;
            return;
        }
        sune.security.util.DerOutputStream os = new DerOutputStream();
        sune.security.util.DerOutputStream tmp = new sune.security.util.DerOutputStream();

        for (sune.security.x509.CertificatePolicyMap map : maps) {
            map.encode(tmp);
        }

        os.write(sune.security.util.DerValue.tag_Sequence, tmp);
        this.extensionValue = os.toByteArray();
    }

    /**
     * Create a PolicyMappings with the List of CertificatePolicyMap.
     *
     * @param map the List of CertificatePolicyMap.
     */
    public PolicyMappingsExtension(List<sune.security.x509.CertificatePolicyMap> map)
            throws IOException {
        this.maps = map;
        this.extensionId = sune.security.x509.PKIXExtensions.PolicyMappings_Id;
        this.critical = false;
        encodeThis();
    }

    /**
     * Create a default PolicyMappingsExtension.
     */
    public PolicyMappingsExtension() {
        extensionId = sune.security.x509.PKIXExtensions.KeyUsage_Id;
        critical = false;
        maps = new ArrayList<sune.security.x509.CertificatePolicyMap>();
    }

    /**
     * Create the extension from the passed DER encoded value.
     *
     * @param critical true if the extension is to be treated as critical.
     * @param value an array of DER encoded bytes of the actual value.
     * @exception ClassCastException if value is not an array of bytes
     * @exception IOException on error.
     */
    public PolicyMappingsExtension(Boolean critical, Object value)
    throws IOException {
        this.extensionId = sune.security.x509.PKIXExtensions.PolicyMappings_Id;
        this.critical = critical.booleanValue();

        this.extensionValue = (byte[]) value;
        sune.security.util.DerValue val = new sune.security.util.DerValue(this.extensionValue);
        if (val.tag != sune.security.util.DerValue.tag_Sequence) {
            throw new IOException("Invalid encoding for " +
                                  "PolicyMappingsExtension.");
        }
        maps = new ArrayList<sune.security.x509.CertificatePolicyMap>();
        while (val.data.available() != 0) {
            sune.security.util.DerValue seq = val.data.getDerValue();
            sune.security.x509.CertificatePolicyMap map = new sune.security.x509.CertificatePolicyMap(seq);
            maps.add(map);
        }
    }

    /**
     * Returns a printable representation of the policy map.
     */
    public String toString() {
        if (maps == null) return "";
        String s = super.toString() + "PolicyMappings [\n"
                 + maps.toString() + "]\n";

        return (s);
    }

    /**
     * Write the extension to the OutputStream.
     *
     * @param out the OutputStream to write the extension to.
     * @exception IOException on encoding errors.
     */
    public void encode(OutputStream out) throws IOException {
        sune.security.util.DerOutputStream tmp = new sune.security.util.DerOutputStream();
        if (extensionValue == null) {
            extensionId = PKIXExtensions.PolicyMappings_Id;
            critical = false;
            encodeThis();
        }
        super.encode(tmp);
        out.write(tmp.toByteArray());
    }

    /**
     * Set the attribute value.
     */
    public void set(String name, Object obj) throws IOException {
        if (name.equalsIgnoreCase(MAP)) {
            if (!(obj instanceof List)) {
              throw new IOException("Attribute value should be of" +
                                    " type List.");
            }
            maps = (List<CertificatePolicyMap>)obj;
        } else {
          throw new IOException("Attribute name not recognized by " +
                        "CertAttrSet:PolicyMappingsExtension.");
        }
        encodeThis();
    }

    /**
     * Get the attribute value.
     */
    public Object get(String name) throws IOException {
        if (name.equalsIgnoreCase(MAP)) {
            return (maps);
        } else {
          throw new IOException("Attribute name not recognized by " +
                        "CertAttrSet:PolicyMappingsExtension.");
        }
    }

    /**
     * Delete the attribute value.
     */
    public void delete(String name) throws IOException {
        if (name.equalsIgnoreCase(MAP)) {
            maps = null;
        } else {
          throw new IOException("Attribute name not recognized by " +
                        "CertAttrSet:PolicyMappingsExtension.");
        }
        encodeThis();
    }

    /**
     * Return an enumeration of names of attributes existing within this
     * attribute.
     */
    public Enumeration<String> getElements () {
        sune.security.x509.AttributeNameEnumeration elements = new AttributeNameEnumeration();
        elements.addElement(MAP);

        return elements.elements();
    }

    /**
     * Return the name of this attribute.
     */
    public String getName () {
        return (NAME);
    }
}
