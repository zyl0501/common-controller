package com.ray.common.exception;

import com.ray.common.lang.Arrays;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Exceptions {
    private static String[] CAUSE_METHOD_NAMES = {
            "getCause",
            "getNextException",
            "getTargetException",
            "getException",
            "getSourceException",
            "getRootCause",
            "getCausedByException",
            "getLinkedException",
            "getLinkedCause",
            "getThrowable",
    };

    public static <T extends Throwable> T getCause(Throwable throwable, Class<T> clazz) {
        if (throwable == null) return null;
        Throwable cause = throwable;
        do {
            if (cause.getClass() == clazz) break;
            cause = cause.getCause();
        } while (cause != null && cause != cause.getCause());
        return (T) cause;
    }

    public static String getFullStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        Throwable[] ts = getThrowable(throwable);
        for (int i = 0; i < ts.length; i++) {
            ts[i].printStackTrace(pw);
        }
        return sw.getBuffer().toString();
    }

    public static Throwable[] getThrowable(Throwable throwable) {
        List list = getThrowableList(throwable);
        return (Throwable[]) list.toArray(new Throwable[list.size()]);
    }

    public static List getThrowableList(Throwable throwable) {
        List list = new ArrayList();
        while (throwable != null && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = getCause(throwable);
        }
        return list;
    }

    public static Throwable getCause(Throwable throwable) {
        synchronized (CAUSE_METHOD_NAMES) {
            return getCause(throwable, CAUSE_METHOD_NAMES);
        }
    }

    public static Throwable getCause(Throwable throwable, String[] methodNames) {
        if (throwable == null) {
            return null;
        }
        Throwable cause = getCauseUsingWellKnownTypes(throwable);
        if (cause == null) {
            if (methodNames == null) {
                synchronized (CAUSE_METHOD_NAMES) {
                    methodNames = CAUSE_METHOD_NAMES;
                }
            }
            for (int i = 0; i < methodNames.length; i++) {
                String methodName = methodNames[i];
                if (methodName != null) {
                    cause = getCauseUsingMethodName(throwable, methodName);
                    if (cause != null) {
                        break;
                    }
                }
            }

            if (cause == null) {
                cause = getCauseUsingFieldName(throwable, "detail");
            }
        }
        return cause;
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
        Method method = null;
        try {
            method = throwable.getClass().getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException ignored) {
            // exception ignored
        } catch (SecurityException ignored) {
            // exception ignored
        }

        if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable) method.invoke(throwable, Arrays.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException ignored) {
                // exception ignored
            } catch (IllegalArgumentException ignored) {
                // exception ignored
            } catch (InvocationTargetException ignored) {
                // exception ignored
            }
        }
        return null;
    }

    private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName) {
        Field field = null;
        try {
            field = throwable.getClass().getField(fieldName);
        } catch (NoSuchFieldException ignored) {
            // exception ignored
        } catch (SecurityException ignored) {
            // exception ignored
        }

        if (field != null && Throwable.class.isAssignableFrom(field.getType())) {
            try {
                return (Throwable) field.get(throwable);
            } catch (IllegalAccessException ignored) {
                // exception ignored
            } catch (IllegalArgumentException ignored) {
                // exception ignored
            }
        }
        return null;
    }

    private static Throwable getCauseUsingWellKnownTypes(Throwable throwable) {
        if (throwable instanceof SQLException) {
            return ((SQLException) throwable).getNextException();
        } else if (throwable instanceof InvocationTargetException) {
            return ((InvocationTargetException) throwable).getTargetException();
        } else {
            return null;
        }
    }
}
