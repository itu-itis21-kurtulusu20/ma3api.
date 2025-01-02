package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerDecodeContext;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1MissingRequiredException;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1SeqOrderException;
import com.objsys.asn1j.runtime.Asn1Tag;
import com.objsys.asn1j.runtime.Asn1Type;
import com.objsys.asn1j.runtime.IntHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.ATSHashIndex;
import tr.gov.tubitak.uekae.esya.asn.cms.ATSHashIndex_certificatesHashIndex;
import tr.gov.tubitak.uekae.esya.asn.cms.ATSHashIndex_crlsHashIndex;
import tr.gov.tubitak.uekae.esya.asn.cms.ATSHashIndex_unsignedAttrsHashIndex;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

public class EATSHashIndex extends BaseASNWrapper<EATSHashIndex.MyATSHashIndex> {

	protected static Logger logger = LoggerFactory.getLogger(EATSHashIndex.class);

	public EATSHashIndex(MyATSHashIndex aObject) {
		super(aObject);
	}
	
	public EATSHashIndex(byte[] aBytes) throws ESYAException {
		super(aBytes, new MyATSHashIndex(aBytes));

	}
	
	public EAlgorithmIdentifier gethashIndAlgorithm() {
		if (mObject.hashIndAlgorithm != null && mObject.hashIndAlgorithm.algorithm != null)
			return new EAlgorithmIdentifier(mObject.hashIndAlgorithm);
		else
			return new EAlgorithmIdentifier(new AlgorithmIdentifier(
					_algorithmsValues.id_sha256));
	}

	public void sethashIndAlgorithm(EAlgorithmIdentifier digestAlg) {
		mObject.hashIndAlgorithm = digestAlg.getObject();
	}

	public Asn1OctetString[] getCertificatesHashIndex() {
		return mObject.certificatesHashIndex.elements;
	}

	public Asn1OctetString[] getCrlsHashIndex() {
		return mObject.crlsHashIndex.elements;
	}

	public Asn1OctetString[] getUnsignedAttrsHashIndex() {
		return mObject.unsignedAttrsHashIndex.elements;
	}

	public static class MyATSHashIndex extends ATSHashIndex {
		private byte[] bytes = null;

		public MyATSHashIndex() {
			super();
		}

		public MyATSHashIndex(byte[] aBytes) {
			super();
			bytes = aBytes;
		}

		public void decode(Asn1BerDecodeBuffer buffer, boolean explicit, int implicitLength) throws Asn1Exception, java.io.IOException {
			try {
				super.decode(buffer, explicit, implicitLength);
			} catch (Exception ex) {

				logger.debug("Warning in EATSHashIndex", ex);

				buffer = new Asn1BerDecodeBuffer(bytes);
				int llen = (explicit) ? Asn1Type.matchTag(buffer,
						Asn1Tag.SEQUENCE) : implicitLength;

				// decode SEQUENCE

				Asn1BerDecodeContext _context = new Asn1BerDecodeContext(
						buffer, llen);

				IntHolder elemLen = new IntHolder();

				super.hashIndAlgorithm = null;
				// decode certificatesHashIndex

				if (_context.matchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16,
						elemLen, false)) {
					super.certificatesHashIndex = new ATSHashIndex_certificatesHashIndex();
					super.certificatesHashIndex.decode(buffer, true,
							elemLen.value);
				} else
					throw new Asn1MissingRequiredException(buffer,
							"certificatesHashIndex");

				// decode crlsHashIndex

				if (_context.matchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16,
						elemLen, false)) {
					super.crlsHashIndex = new ATSHashIndex_crlsHashIndex();
					super.crlsHashIndex.decode(buffer, true, elemLen.value);
				} else
					throw new Asn1MissingRequiredException(buffer,
							"crlsHashIndex");

				// decode unsignedAttrsHashIndex

				if (_context.matchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16,
						elemLen, false)) {
					super.unsignedAttrsHashIndex = new ATSHashIndex_unsignedAttrsHashIndex();
					super.unsignedAttrsHashIndex.decode(buffer, true,
							elemLen.value);
				} else
					throw new Asn1MissingRequiredException(buffer,
							"unsignedAttrsHashIndex");

				if (!_context.expired()) {
					Asn1Tag _tag = buffer.peekTag();
					if (_tag.equals(Asn1Tag.UNIV, Asn1Tag.CONS, 16))
						throw new Asn1SeqOrderException();
				}
			}
		}
	}
}
