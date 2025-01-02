using System;
using System.IO;
using System.Text;
using log4net.Config;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class MA3ApiLogUtil
    {
        public static string log4jConfiguration =

@"<log4net>  
<appender name=""FileAppender"" type=""log4net.Appender.FileAppender""> 
<file value=""[FILE_PATH]"" /> 
<appendToFile value=""true"" /> 
<lockingModel type=""log4net.Appender.FileAppender+MinimalLock"" /> 
<layout type=""log4net.Layout.PatternLayout""> 
<conversionPattern value=""%level %date [%thread] %logger{4} (%M:%L) - %message%newline"" /> 
</layout> 
</appender> 
<!-- Defining namespace of MA3 API in order to not change log4net configuration of other libraries -->
<logger name=""tr.gov.tubitak"">
  <level value=""ALL""/>
   <appender-ref ref=""FileAppender"" /> 
</logger>
</log4net> ";
           

        public static void openFileLogging(String fileName)
        { 
            if (fileName.Contains("\\"))
            {
                fileName = fileName.Replace("\\", "/");
            }

            string log4jConfigurationWithFileName = log4jConfiguration.Replace("[FILE_PATH]", fileName);
            MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(log4jConfigurationWithFileName));
            XmlConfigurator.Configure(ms);
        }

    }
}
