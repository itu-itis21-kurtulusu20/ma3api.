package test.esya.api.signature;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class PojoUtils {

    public static <T> T testPojo(Class<T> pojoClass) {

        final T instance;
        final T instance2;
        try {
            instance = pojoClass.newInstance();
            instance2 = pojoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create instance from pojo: " + pojoClass + " maybe you forgot to " +
                    "create empty constructor");
        }

        try {
            invokeConstructors(pojoClass);
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke constructors of " + pojoClass, e);
        }

        return testPojo(pojoClass, instance, instance2);
    }

    public static <T> T testPojo(Class<T> pojoClass, T instance, T instance2) {
        Method[] methods = pojoClass.getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (isGetter(method)) {
                    method.invoke(instance);
                } else if (isSetter(method)) {
                    callSetter(instance, method);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to invoke method: " + method);
            }
        }

        instance.equals(null);
        instance.equals(instance);
        instance.equals(instance2);
        instance.equals(1L);
        instance.hashCode();
        instance.toString();

        return instance;
    }

    private static <T> void invokeConstructors(Class<T> pojoClass) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        for (Constructor<?> constructor : pojoClass.getConstructors()) {
            Object[] args = new Object[constructor.getGenericParameterTypes().length];
            for (int i = 0; i < args.length; i++) {
                Class<?> paramClass = constructor.getParameterTypes()[i];
                args[i] = getParamValue(paramClass);
            }
            constructor.newInstance(args);

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg != null && arg instanceof List) {
                    List nullList = null;
                    args[i] = nullList;
                }
            }
            constructor.newInstance(args);
        }
    }

    private static <T> void callSetter(T instance, Method method) throws IllegalAccessException,
            InvocationTargetException {
        Class<?> paramClass = method.getParameterTypes()[0];
        method.invoke(instance, getParamValue(paramClass));
        if (paramClass.isAssignableFrom(List.class)) {
            List nullList = null;
            method.invoke(instance, nullList);
        }
    }

    private static Object getParamValue(Class<?> paramClass) {
        if (paramClass.isPrimitive()) {
            if (paramClass.equals(boolean.class)) {
                return false;
            }
            if (paramClass.equals(char.class)) {
                return 'c';
            }
            if (paramClass.equals(float.class)) {
                return 1.0F;
            }
            if (paramClass.equals(double.class)) {
                return 1.0D;
            }
            return 1;
        } else {
            if (paramClass.isAssignableFrom(List.class)) {
                return new ArrayList<Object>();
            }
            return null;
        }
    }

    public static boolean isSetter(final Method method) {
        return Modifier.isPublic(method.getModifiers())
                && void.class.equals(method.getReturnType())
                && method.getParameterTypes().length == 1
                && method.getName().startsWith("set");
    }

    public static boolean isGetter(final Method method) {
        if (method.getParameterTypes().length == 0
                && Modifier.isPublic(method.getModifiers())) {
            final String methodName = method.getName();

            if (methodName.startsWith("get")) {
                return !void.class.equals(method.getReturnType());
            } else if (methodName.startsWith("is")) {
                return boolean.class.equals(method.getReturnType()) || Boolean.class.equals(method.getReturnType());
            }
        }
        return false;
    }

    public static <T> T testPrivateConstructor(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        assertTrue("Constructor should be private", Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

}