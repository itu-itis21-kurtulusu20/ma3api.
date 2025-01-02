package test.esya.api.cmssignature.crypto;


import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import java.io.*;
import java.security.PublicKey;

import static junit.framework.TestCase.assertNotNull;

public class PfxParserTest {

	@Test
	public void testPfx() throws CryptoException, IOException {
		byte []  pfx = Base64.decode(mustafaAksoyUG2048_042145);
		PfxParser parser = new PfxParser(new ByteArrayInputStream(pfx), "042145".toCharArray());
		System.out.println(parser.getCertificatesAndKeys().size());

		byte []  pfx2 = Base64.decode(GOK_UG_852175);
		PfxParser parser2 = new PfxParser(new ByteArrayInputStream(pfx2), "852175".toCharArray());
		System.out.println(parser2.getCertificatesAndKeys().size());
	}


	@Test
	public void testListPfxsInTheFolder() throws Exception{
		File folder = new File("T:\\api-parent\\resources\\ug\\pfx\\test-suite\\");

		FilenameFilter pfxFileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".p12") || name.toLowerCase().endsWith(".pfx");
			}
		};

		File[] files = folder.listFiles(pfxFileFilter);

		for (File aPfxFile:files) {
			PfxParser pfxParser = new PfxParser(new FileInputStream(aPfxFile), "123456");

            byte[] bytes = pfxParser.getFirstCertificate().getEncoded();
            ECertificate certificate = new ECertificate(bytes);
            assertNotNull(certificate);

            PublicKey pubKey = KeyUtil.decodePublicKey(pfxParser.getFirstCertificate().getSubjectPublicKeyInfo());
            assertNotNull(pubKey);
			System.out.println(aPfxFile.getName() + " : " + StringUtil.toHexString(pubKey.getEncoded()));
		}
	}

	@Test
	public void testPfxSignatureCheck()throws Exception {
		PfxParser signerPfx = new PfxParser(new FileInputStream("T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_1.p12"), "123456");
		PfxParser verifierPfx = new PfxParser(new FileInputStream("T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_2.p12"), "123456");

		byte [] data = RandomUtil.generateRandom(300);

		byte[] signature = SignUtil.sign(SignatureAlg.RSA_SHA256, data, signerPfx.getFirstPrivateKey());

		boolean verified = SignUtil.verify(SignatureAlg.RSA_SHA256, signature, data, verifierPfx.getFirstCertificate());

		System.out.println(verified);
	}

	public static String GOK_UG_PASSWORD = 	"852175";
	public static String GOK_UG_852175 = 	"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" +
											"gKbVqG0CAgfQ";

	public static String mustafaAksoyUG2048_042145 =
	"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" +
	"ezi4IIJq/PMPHN2QAgIH0A==" ;

	public  static String YETGIN_UG_PASSWORD = 	"051268";
	private static String YETKIN_UG_051268   = 	"MIIQ+QIBAzCCELUGCSqGSIb3DQEHAaCCEKYEghCiMIIQnjCCBfMGCSqGSIb3DQEHAaCCBeQEggXg" +
												"MIIF3DCCAuoGCyqGSIb3DQEMCgECoIICwjCCAr4wKAYKKoZIhvcNAQwBAzAaBBROabjhVPUGHE7j" +
												"FNu1P0TnahcD7wICB9AEggKQcyuU99b814Yf8ZkQ0VoumAPRj+tF6/kg68/x62B/VIPQn5SdxCFs" +
												"SbmqCWRWYjwlrs6UhVzCLZrse/zznDpMZZ+zsG4hjWvZhvxnsIkvkKV3UywZmngBYgQ7loI2KK3S" +
												"/qY7y1kRrNif6SOaojD6O8cmCw5lu7RsFerEpccsK8fMwmaxEeRf+Nh0Y74YxLZVXqlZsheakl1M" +
												"rGB93SoAPIO8za5n99eOdhVVgssveDG8LtrFrz4XP7XcjUaEZtjvofKu1FgT6dRxajOvviuIJUhb" +
												"QkAyG+McNtLJ+n2glwHIlVy01KykGl4mvDzljLKQchxCvmxIagudbYGrjCnjx5F3DeHoR3AlYqL1" +
												"lsIQB9+ZZV4gMoCHn5DFrT3+QY0CuI5Pdmofb3y8o4T9KA10A2BlkuOdoEGyfg0WvEqho/fGT+N4" +
												"7h5lnY/46z90E6bNBcxIBCUSDIy1Lv5K58GoliirDSLBdCdXwXdqccNYr9DIbDQN32SGHXr1Ni7w" +
												"7ox4rZSfMah4RpItsAKfPmFmPrhn6GlPOeLTvokG89AjMyNOvsRhHQy67/Ij+lGE3MqQPfa1F+aL" +
												"2DkqP/wMe3UJHKx2jJSTbryBI9i14ZChnEuVtZ4Rz1KM/DvrVHWaHLdalQewPDLogyT914H0fILS" +
												"9sDmnbs0zkTr8FWnIw/hIpF5U4o0+OF1iarr2oSP54IAbqLHV7xK0YlqN02dWmnzLZpDW0r9jBb8" +
												"kGOBWDTc0nxCiGWlmUTzYcT6yU2AavVRC0GcgMMMvxoTigHFytysVEQ+nKtY1tfU+FUMaRDXd0dZ" +
												"TuNRPs+n/S43HSL6C2rROdFpiACfa30csDwwJ7XzzMAqWnoYlWwRbwQW5v99f1oxFTATBgkqhkiG" +
												"9w0BCRUxBgQEAQAAADCCAuoGCyqGSIb3DQEMCgECoIICwjCCAr4wKAYKKoZIhvcNAQwBAzAaBBRO" +
												"abjhVPUGHE7jFNu1P0TnahcD7wICB9AEggKQpO+tIpKrT2Ub00FZUlKshZumjD90OexCdhOSvXQR" +
												"NPfdhjqUUIy6ukiGOxKur+bq8LzCVR52wRs0tKy88Ud/37i83C10Q79/Rb12V4aafOPO/pqI8PxT" +
												"gal3erfjzwxaKer4oM/MnIn5U77VCX2xTtbDQLtfsX/55fkX73ukK88w9P2FSQwHhDbTBClO1M4V" +
												"A6GTfH1axH5voiREsHNUDV1/3nsCRG43/srFtvDAaXIAbHAsD8Tw7PpPq9OdIEWx3fJlXY2ZUEak" +
												"xLMwlhvstw+5YoW/lnGiNHE12DPijA0XKze/33SG3aTFtn/TKCbm+vrCSlOM2aUAPmARPcAtMB2U" +
												"SKMNCc3BWUIZmtSB/umQWiODMZ7slmWovzL8ouKudBBcv1RN/fDLP+YyI+dQGajny/8juaeV4puF" +
												"OIHLxBWYF1JyTCNCSjjvwsey6eK7vDcFi/Xl/8N3YfUOg82yoopFAtTLu0d+Ad9NDCouQrGi66Av" +
												"SkRCt2fTGBOeOTwozt/BvtwmL0bIDRO/uR5UavHLh6ubdw0GE5o2jOBTFiQKZoZse5O/AaHAIOEx" +
												"0XI+JPZfeLdeW13L9CsIyeWjr1dCQ4NgBKkamMK6Cox+O2IkZCN3pwT5UrOHvLS/dZWWnquX03qv" +
												"m7/gZZ3/XbbGuLBN2YvFBgDcrp0KyXJia2+WH0B++O58c2sF6DyHwJf+D5OvyHcJYV5jMaNmb1k8" +
												"8no18qrplVyk9UY576Ov1dtW8yd4fseyeQ8Aa43vgNhZ0OW8ULci7WZkjTzJOPJN3cD5uDicB9r5" +
												"eRse3/H4nik1zNzG2YkYRArMoXB6qvmboOL3pszxwGEqqyePHBTthJBGovsycT7WHLzpslQ9E+0x" +
												"FTATBgkqhkiG9w0BCRUxBgQEAgAAADCCCqMGCSqGSIb3DQEHBqCCCpQwggqQAgEAMIIKiQYJKoZI" +
												"hvcNAQcBMCgGCiqGSIb3DQEMAQMwGgQUTmm44VT1BhxO4xTbtT9E52oXA+8CAgfQgIIKUInNmr2p" +
												"/hpFEzFjxIDIjMS48qgNoLMa76Y2xiIEHzpm33ypZAmgZWJCS4axITOKoM5Z8jgHRHbNZy/LLEPE" +
												"liNixmKbi3zAHUJi43+nNH1ogi6v0pL2Ac/tKGOO6YYhDzHZeqiaO9GBS2+bFqB8uVKg3yi88pB0" +
												"AUwJJbUVcfVLXG6hgrW7C+DX1snPml2EROiLuTc/1vfyKPuR+H2O16+ymXCDG0P9o+KTIZeLdinB" +
												"HGT6a5UA37NOA/QgAU/1WUZMOSKfNMYQPOdIFZvttNp3LKNsg1udHrm6kyCPpm3YvTpV39hnkDoP" +
												"ZYX4aKOUrX1mGzyLBOLIt/UTevbKiMuJq6TZs5FH4WCBrDEebq+QXYmjIbPUJovtSUvO4mpQm9ux" +
												"bf7jGQLzriGmpDbU6/kNO0ZczmMsUTJltVbds7SvfidXMgjUej0UBNbW8yDlNspWAlxRuoHn6+iU" +
												"VN0Kua6V0Nl2zfGb2UYRmIElajtgoPNq5P8sMEBpQ+oJMxbeB7iUfuOPCmt95SI2pTgPZmS/6Wj6" +
												"bIKf5n/gXrq6hay/lf8K7iJPutwN5BBC14Pfr+NV8kRpR7iQqjYJsA9epsCxPQJ5CJEH7R5gEM86" +
												"0a+CLv4ParqfLW8n1Ht/SftMzahHxx8uRRhxHYUtTq8/IwwvCMan034RmswO4pi3u+RDk40I0Tl+" +
												"yK6sVh7G8r/HwTtxWt0AVVjuSQ6QEsGFsyNrfpUVcms/UVOkHEUPwiajasfcw/cMW+vWsiCtRwb/" +
												"Pzbo7obO1jA12D78Ct67wwev1Yi2QaTW4y/8DixCRXW8oHRnmuvCNjqvty3s2GbinUBaU29FlE7u" +
												"mw50ZIx/sLimx1nmNtoA4xDPaehXDqfaPQ+rmVEI8Y4iRJHCWZPowFYd4r64S2l3eFIDAo114LU2" +
												"KncytwPIE5XQ4auEcEZFuO+ZvcwrDbjoeoLTj/sv5K7Vt123/sF0w6t2BgxoywSTAgjuEdclCvhc" +
												"EBWxYghhVUmEnsjkSSyye2WoJn1/nu49YMV6+HvCW+6BwGaOHl8XlCoqc02Na0SG0xn3nCN3EWcn" +
												"gNwk7SlMzT6TqMTPS1nYvNTILUDxJ0QQcFRnZEkcDYV/C3PQs1z/4Ni2kC7Ev60jTfZSBO+YL89Q" +
												"URTxLDN9xjasZDfv5PdGNJ1Hyr7BVBXmIVkSSeBpIy4CCisO0bw5ONWe+G2pveKF0TOAh1GQnJ3X" +
												"8mq77cJqEtPnXdCuFCb+elaB854Dihl40/pnJwBT5uX7ToF8R8moKpD2WTPQQm4lT42VPAi63OsW" +
												"c/KzAwoleH3GVJ0uGp7WZ/Ptc42AZgSDVRadjgUdaA2G9/us4RSJc88Xrh+61MXg+iKgmqLhVBye" +
												"ozy7qshjNqJpM6C+I0Bag5XWViX/b04kayPyiCdoL6e3u8FS50sJISgWBTgeFu9O8cIpuI7P33k3" +
												"Ovxk+biNgKDULMq4ZcNqv0Np26vFqDMaGEz6Ek4/fGNBJ3QGJT6IgKIIuMsMXcoHNy5GYcFOUUZl" +
												"Lde/WDzpudEC8Bzah8ymFgbk+L1saAqYB01sAtyjhAbDXEO+rzfDk0WPfgIqm+iBtfDV/M6UTSIt" +
												"FcHz/MyOLStQsMXgGO8mWLXFXTyqj0CkagyGtqRsQkyk335ABR9rvesBVybptEefk33a4wo/oO5T" +
												"m9WcOkSAnD4x6ipWgh5kROpexI4UpCoq7rTvPal0DnnsDciUvV/Muw5bF5Q9k1TNMFyx4F7UsxrZ" +
												"SdEAFie2syLoiXgLxrxicmZRrjkIF7YeDIO5mNOm5F0FG60M4++azrVzVS5tlzZVkRd+8xX7Hsb6" +
												"oTJk4YfOcBWFitVaMvjX4noR/lBf1fkxHynxhMqCp5uL0SVOVdd5FQEawlfJBP/icl9o51TE7EOG" +
												"h94yIsLV6Lom0RgQdkDmVPeIu+A6azynRTk7/2DF2r2KlXrlK/uc2kxu/EdGIKeUtTSNneSSHNOv" +
												"PRZ9rUyCKrkXoWJlRSXT+DFL7qFXVTyk3zjSpLDyarF1wQFrD8qY5fDiu0QpeZVRnJgv6iY8qo4B" +
												"h3dRH8uFYE6ytcToKRfgJg/Xr2EcfC/JW+nbhWV9heU44xxFWyRDXz6wIvi7Ao8Tr3W4NkacR9pM" +
												"zJmvkinnqfaRBHyuSeCu5W/ooFOztg1+y7fDWua0DQNcEJD6xTF7vYNBesdyAe3F8jlZnYhtFXjy" +
												"G2lBOepiX2DrFDnt7CUG8bB33WRIBArMJPvKsIjPkP6G0U3MsnsS5n+RJ4cSTd9BuQXrO+S0iPVw" +
												"1yk079unRj/UTNgNT1JcuWrosyUa2wCOYiATdSbS2TJRzriaJQ0ySEMdMKagpbtmodYD2ZJNAOaX" +
												"Y5lWmi63vJUrWWCikby3hCENVbd0uf9yJikExTVeVRQ+PHMiZgz0CkrOBdTMIOsTbPNl5pOb/xDp" +
												"vTVjnv75h1+SFiwWXXxPhlUboGTshP1XzAkLd1UR72nAUhM0vE5ehUCY0XN2bWd2fn6kbv+lfnrP" +
												"CvwMQ7c56GrvO/FkQNxvWbNrhg+F7oGzxly7bSZm6zts7FRcv2B+7wrdBEbCGnXVoIjd5zXiO60+" +
												"Q9d/BfpJCc03qUow609XpfGYRFoV6UrCLNKh3jLNpAnbxN62NLGVoMyLLqKeg4ye8yQptTNQkZGm" +
												"h4H3PKzfT2BkiobEClrW4hbhxV3PSC/cPPNi5TQwfd8DjNesEoc2eRDNkYt+aRB7sidTS7fSnoZ1" +
												"7dAgVeaGGADrEi872q7IG+qDD4iJxZUUqDedB8yi430gbHUfi0CGQ50MqYCfEGyBUqOki+woHV8A" +
												"gDtObJIWV+ruVz/3h4Z3QnOdTZxyX1jwEe6+dbr0B42gusM97PhrYI/p5YWcB6aOsgQQGSIfsjIx" +
												"tt71jnxcpjnLie/3OOGXjUfXvBiv+VPjPV3lgtDYAcIAWcNgzKXFfte1XFIreBTIh9N21OfXbXab" +
												"8T5X9Ou7hY/FploGTcsmB+LwNqvDT32itXZ16Qjbnk/vS1Oggd7CjHO+XeYXwaR4LsvoWgdA9Mx7" +
												"KNsINxVBfDXifDcfIPTT7Sc4SrAeMS3zmVaIDJKN/rQaa8pSiwDk1Ft3O18aZ65PkhMBzUS9bSUr" +
												"5z20Vlbhc7+a6P5frezhXX+vDMaT8hBZ6RV68Y2eIbtQdV9zOJ9WefGcB4NGucI0ovaAZPD6cZiK" +
												"Tr1eCynW+rkaWfdaOhJFs+wMzDe8tSv+yWzUcc1mjMWQFoLqXU1pHDnzizLkwA8RD9ODD+ZJGECi" +
												"kgVBTSPmxtxHVXecg28zhx3pj/8IqkhfMnGVvZ/L3t39MCP3NLeg3EeTJMPNBFPlOl35ObSbFWY3" +
												"r7DWw3KfL+vX0GxG+gLlPQKAqvguvpcwKN0GbXyU7s1q16JvP7RrIpDKUlUEwKNjNan4706OMFix" +
												"pNY3ys7tKKXAqjTc/T6vSaOmGbPzGYijVukvCnq+8RNnBcA5o0KGaZqM/01Eac7H1KxhwfH0bZE5" +
												"3pTsjVScj2uxA8EbRjA7MB8wBwYFKw4DAhoEFKA3Ca893ZVrFmPbIR7JviziBig6BBROabjhVPUG" +
												"HE7jFNu1P0TnahcD7wICB9A=";
}
