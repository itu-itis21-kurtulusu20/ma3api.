package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.Calendar;
import java.util.List;

public class TrustedCertificateFinderFromTSL extends TrustedCertificateFinder {

	private static final Logger logger = LoggerFactory.getLogger(TrustedCertificateFinderFromTSL.class);
	private static TrustedCertificateFinder trustCertfinder;
	private static List<ECertificate> trustedCertificates;
	private static Calendar readTime = Calendar.getInstance();

	@Override
	protected List<ECertificate> _findTrustedCertificate() {
		Calendar now = Calendar.getInstance();

		Calendar oneDayLaterAfterReading = (Calendar) readTime.clone();
		oneDayLaterAfterReading.add(Calendar.DATE, 1);

		// if one day has passed since we read trusted certificates, read again
		if (now.after(oneDayLaterAfterReading)) {
			trustedCertificates = null;
		}
		if (trustedCertificates == null) {

            try {
                trustCertfinder = (TrustedCertificateFinder) Class.forName(
                                "tr.gov.tubitak.uekae.esya.api.tsl.CertificateFinder.TrustedCertificateFinderFromTSL").newInstance();
            } catch (Exception e) {
                logger.error("Dynamic Binding of TrustedCertificateFinderFromTSL is not working" + e.getMessage(), e);
            }
            trustedCertificates= trustCertfinder.findTrustedCertificate();
            readTime = Calendar.getInstance();
        }
		return trustedCertificates;
	}
}
