package dev.esya.api.cmssignature.plugtest.validation;

import test.esya.api.cmssignature.CMSSignatureTest;

public class CMSValidationTest
{
	public static String INPUT_DIRECTORY_PATH;
	
	static
	{
		CMSSignatureTest test = new CMSSignatureTest();
		INPUT_DIRECTORY_PATH = test.getDirectory() + "validation\\";
	}
}
