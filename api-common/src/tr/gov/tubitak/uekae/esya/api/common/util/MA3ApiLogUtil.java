package tr.gov.tubitak.uekae.esya.api.common.util;


import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

public class MA3ApiLogUtil {

    public static final String log4jConfiguration =
            "#PropertConfigurator.configure(\"log4j.properties\"); \n" +
            "# Set root logger level to DEBUG and its appender to console,rolling,lf5rolling \n" +
            "#log4j.rootLogger=debug,rolling \n" +
            "#Defining namespace of MA3 API in order to not change log4j configuration of other libraries \n" +
            "log4j.logger.tr.gov.tubitak=debug,rolling\n" +
            "log4j.logger.gnu.crypto=debug,rolling\n" +
            "log4j.appender.rolling=org.apache.log4j.RollingFileAppender \n" +
            "log4j.appender.rolling.File=[FILE_PATH] \n" +
            "log4j.appender.rolling.MaxFileSize=50MB \n" +
            "log4j.appender.rolling.MaxBackupIndex=20 \n" +
            "log4j.appender.rolling.layout=org.apache.log4j.PatternLayout \n" +
            "log4j.appender.rolling.layout.ConversionPattern=%p %d [%t] %c{4} (%M:%L) - %m%n \n" +
            "# END APPENDER: ROLLING FILE APPENDER (rolling)";



    public static void openFileLogging(String fileName) throws ESYAException {
        try {
            if(fileName.contains("\\"))
            {
                fileName = fileName.replace("\\","/");
            }

            String log4jConfigurationWithFileName = log4jConfiguration.replace("[FILE_PATH]", fileName);
            ByteArrayInputStream bis = new ByteArrayInputStream(log4jConfigurationWithFileName.getBytes("UTF-8"));

            //Call with reflection "PropertyConfigurator.configure(bis);"
            Class<?> classPropertyConfigurator = Class.forName("org.apache.log4j.PropertyConfigurator");
            Method methodConfigure = classPropertyConfigurator.getMethod("configure", InputStream.class);
            methodConfigure.invoke(null, bis);
        } catch (UnsupportedEncodingException e) {
            throw new ESYAException("UTF-8 Encoding is not supported in your System!", e);
        } catch (ClassNotFoundException e) {
            throw new ESYAException("Be sure that log4j.jar is added!", e);
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    //This is for java logging. If log4j will not work properly, we can switch to java logging
    /*public static void openFileLogging(String fileName) throws IOException {
        Handler fileHandler = new FileHandler(fileName);

        fileHandler.setFormatter(new SimpleFormatter(){
            private static final String format = "%4$s: %1$tc %2$s - %5$s%6$s%n";
            private final Date dat = new Date();

            @Override
            public synchronized String format(LogRecord record){
                dat.setTime(record.getMillis());
                String source;
                if (record.getSourceClassName() != null) {
                    source = record.getSourceClassName();
                    if (record.getSourceMethodName() != null) {
                        source += " " + record.getSourceMethodName();
                    }
                } else {
                    source = record.getLoggerName();
                }
                String message = formatMessage(record);
                String throwable = "";
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    throwable = sw.toString();
                }
                return String.format(format,
                        dat,
                        source,
                        record.getLoggerName(),
                        record.getLevel().getLocalizedName(),
                        message,
                        throwable);
            }
        });

        Logger logger = Logger.getLogger("tr.gov.tubitak");
        logger.setLevel(Level.ALL);
        logger.addHandler(fileHandler);
    }*/

}
