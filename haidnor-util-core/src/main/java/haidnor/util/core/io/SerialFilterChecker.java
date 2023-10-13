package haidnor.util.core.io;


import java.security.Security;

public final class SerialFilterChecker {
    /* Property key for the JEP-290 serialization filters */
    private static final String JDK_SERIAL_FILTER = "jdk.serialFilter";
    private static final boolean SERIAL_FILTER_MISSING;
    private static boolean firstInvocation = true;

    static {
        Object serialFilter;
        try {
            Class<?> objectFilterConfig = Class.forName("java.io.ObjectInputFilter$Config");
            serialFilter = objectFilterConfig.getMethod("getSerialFilter").invoke(null);
        } catch (ReflectiveOperationException e) {
            // Java 1.8
            serialFilter = System.getProperty(JDK_SERIAL_FILTER, Security.getProperty(JDK_SERIAL_FILTER));
        }
        SERIAL_FILTER_MISSING = serialFilter == null;
    }

    private SerialFilterChecker() {
    }

    public static void check() {
        if (firstInvocation && SERIAL_FILTER_MISSING) {
            firstInvocation = false;
        }
    }

}
