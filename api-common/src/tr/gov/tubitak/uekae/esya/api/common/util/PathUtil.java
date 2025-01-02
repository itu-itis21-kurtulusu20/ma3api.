package tr.gov.tubitak.uekae.esya.api.common.util;


public class PathUtil 
{
	public static String getRawPath(String aPath)
	{
        if(aPath == null){
            return null;
        }
		String path = new String(aPath);
		int beginIndex = path.indexOf("%");
		while(beginIndex >= 0)
		{
			int lastIndex = path.indexOf("%", beginIndex+1);
			
			String envVariable = path.substring(beginIndex+1, lastIndex);
			String envVariableValue = System.getenv(envVariable);
			
			path = path.replace(path.substring(beginIndex, lastIndex+1), envVariableValue);
			
			beginIndex = path.indexOf("%", lastIndex+1);
		}
		
		return path;
	}
}
