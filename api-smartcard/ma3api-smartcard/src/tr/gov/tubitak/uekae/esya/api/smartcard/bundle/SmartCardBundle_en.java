package tr.gov.tubitak.uekae.esya.api.smartcard.bundle;

import static tr.gov.tubitak.uekae.esya.api.smartcard.bundle.E_KEYS.*;

import java.util.ListResourceBundle;

public class SmartCardBundle_en extends ListResourceBundle
{
	private Object[][] mContents = 
		{
				{INCORRECT_PIN.name(), "Incorrect PIN."},
				{INCORRECT_PIN_FINAL_TRY.name(), "Incorrect PIN. PIN will be locked next incorrect attempt."},
				{PIN_LOCKED.name(), "PIN is locked."},
				{OK.name(), "OK"},
				{EXCEEDING_DATA_LENGTH.name(), "Data length exceeds expected maximum length."}
		};

		@Override
		protected Object[][] getContents() 
		{
			return mContents;
		}
}
