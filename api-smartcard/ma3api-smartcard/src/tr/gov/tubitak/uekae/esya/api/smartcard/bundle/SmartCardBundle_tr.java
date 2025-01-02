package tr.gov.tubitak.uekae.esya.api.smartcard.bundle;

import java.util.ListResourceBundle;
import static tr.gov.tubitak.uekae.esya.api.smartcard.bundle.E_KEYS.*;

public class SmartCardBundle_tr extends ListResourceBundle
{	
	private Object[][] mContents = 
	{
			{INCORRECT_PIN.name(), "Hatalı PIN."},
			{INCORRECT_PIN_FINAL_TRY.name(), "Hatalı PIN. Bir sonraki hatalı girişte PIN kilitlenecektir."},
			{PIN_LOCKED.name(), "PIN kilitli."},
			{OK.name(), "Tamam"},
			{EXCEEDING_DATA_LENGTH.name(), "Veri uzunluğu, beklenen azami uzunluğu aşıyor."}
	};

	@Override
	protected Object[][] getContents() 
	{
		return mContents;
	}
}
