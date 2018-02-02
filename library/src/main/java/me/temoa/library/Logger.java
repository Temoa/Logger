package me.temoa.library;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lai
 * on 2017/12/29.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Logger {

    private static Printer sPrinter;

    public static Printer tag(String tag) {
        return getPrinter().tag(tag);
    }

    public static Printer lineNumber() {
        return getPrinter().lineNumber();
    }

    /* ------------------------------------------------------------------------------------------ */

    public static void d(String s) {
        getPrinter().d(s, (Object) null);
    }

    public static void d(Throwable e) {
        getPrinter().d(e, null, (Object) null);
    }

    public static void d(String s, Object... object) {
        getPrinter().d(s, object);
    }

    public static void d(Throwable e, String s, Object... object) {
        getPrinter().d(e, s, object);
    }

    /* ------------------------------------------------------------------------------------------ */

    public static void e(String s) {
        getPrinter().e(s, (Object) null);
    }

    public static void e(Throwable e) {
        getPrinter().e(e, null, (Object) null);
    }

    public static void e(String s, Object... object) {
        getPrinter().e(s, object);
    }

    public static void e(Throwable e, String s, Object... object) {
        getPrinter().e(e, s, object);
    }

    /* ------------------------------------------------------------------------------------------ */

    public static void w(String s) {
        getPrinter().w(s, (Object) null);
    }

    public static void w(Throwable e) {
        getPrinter().w(e, null, (Object) null);
    }

    public static void w(String s, Object... object) {
        getPrinter().w(s, object);
    }

    public static void w(Throwable e, String s, Object... object) {
        getPrinter().w(e, s, object);
    }

    /* ------------------------------------------------------------------------------------------ */

    public static void v(String s) {
        getPrinter().v(s, (Object) null);
    }

    public static void v(Throwable e) {
        getPrinter().v(e, null, (Object) null);
    }

    public static void v(String s, Object... object) {
        getPrinter().v(s, object);
    }

    public static void v(Throwable t, String s, Object... object) {
        getPrinter().v(t, s, object);
    }

    /* ------------------------------------------------------------------------------------------ */

    public static void i(String s) {
        getPrinter().i(s, (Object) null);
    }

    public static void i(Throwable e) {
        getPrinter().i(e, null, (Object) null);
    }

    public static void i(String s, Object... object) {
        getPrinter().i(s, object);
    }

    public static void i(Throwable t, String s, Object... object) {
        getPrinter().i(t, s, object);
    }

    /* ------------------------------------------------------------------------------------------ */

    public static void wtf(String s) {
        getPrinter().wtf(s, (Object) null);
    }

    public static void wtf(Throwable e) {
        getPrinter().wtf(e, null, (Object) null);
    }

    public static void wtf(String s, Object... object) {
        getPrinter().wtf(s, object);
    }

    public static void wtf(Throwable t, String s, Object... object) {
        getPrinter().wtf(t, s, object);
    }

    /* ------------------------------------------------------------------------------------------ */

    public static void json(String s) {
        getPrinter().json(s);
    }

    public static void json(int priority, String s) {
        getPrinter().json(priority, s);
    }

    private static Printer getPrinter() {
        if (sPrinter == null) {
            sPrinter = new Printer();
        }
        return sPrinter;
    }

    public static class Printer {

        private static final boolean sisLoggable = true;

        private final ThreadLocal<String> mExplicitTag = new ThreadLocal<>();
        private final ThreadLocal<String> mExplicitLineNumber = new ThreadLocal<>();

        private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

        private static final int STACK_MIN_COUNT = 5;
        private static final int TAG_STACK_INDEX = 4;
        private static final int METHOD_NUMBER_STACK_INDEX = 3;
        private static final int TAG_MAX_LENGTH = 23;
        private static final int LOG_MAX_LENGTH = 4000;
        private static final int JSON_INDENT = 2;

        private static final String RIGHT_ARROW = " -> ";

        public Printer tag(String tag) {
            mExplicitTag.set(tag);
            return this;
        }

        public Printer lineNumber() {
            mExplicitLineNumber.set(getMethodLineNumber());
            return this;
        }

        /* -------------------------------------------------------------------------------------- */

        public void d(String s) {
            prepareLog(Log.DEBUG, null, s, (Object) null);
        }

        public void d(Throwable e) {
            prepareLog(Log.DEBUG, e, null, (Object) null);
        }

        public void d(String s, Object... obj) {
            prepareLog(Log.DEBUG, null, s, obj);
        }

        public void d(Throwable t, String s, Object... obj) {
            prepareLog(Log.DEBUG, t, s, obj);
        }

        /* -------------------------------------------------------------------------------------- */

        public void e(String s) {
            prepareLog(Log.ERROR, null, s, (Object) null);
        }

        public void e(Throwable e) {
            prepareLog(Log.ERROR, e, null, (Object) null);
        }

        public void e(String s, Object... obj) {
            prepareLog(Log.ERROR, null, s, obj);
        }

        public void e(Throwable t, String s, Object... obj) {
            prepareLog(Log.ERROR, t, s, obj);
        }

        /* -------------------------------------------------------------------------------------- */

        public void w(String s) {
            prepareLog(Log.WARN, null, s, (Object) null);
        }

        public void w(Throwable e) {
            prepareLog(Log.WARN, e, null, (Object) null);
        }

        public void w(String s, Object... obj) {
            prepareLog(Log.WARN, null, s, obj);
        }

        public void w(Throwable t, String s, Object... obj) {
            prepareLog(Log.WARN, t, s, obj);
        }

        /* -------------------------------------------------------------------------------------- */

        public void v(String s) {
            prepareLog(Log.VERBOSE, null, s, (Object) null);
        }

        public void v(Throwable e) {
            prepareLog(Log.VERBOSE, e, null, (Object) null);
        }

        public void v(String s, Object... obj) {
            prepareLog(Log.VERBOSE, null, s, obj);
        }

        public void v(Throwable t, String s, Object... obj) {
            prepareLog(Log.VERBOSE, t, s, obj);
        }

        /* -------------------------------------------------------------------------------------- */

        public void i(String s) {
            prepareLog(Log.INFO, null, s, (Object) null);
        }

        public void i(Throwable e) {
            prepareLog(Log.INFO, e, null, (Object) null);
        }

        public void i(String s, Object... obj) {
            prepareLog(Log.INFO, null, s, obj);
        }

        public void i(Throwable t, String s, Object... obj) {
            prepareLog(Log.INFO, t, s, obj);
        }

        /* -------------------------------------------------------------------------------------- */

        public void wtf(String s) {
            prepareLog(Log.ASSERT, null, s, (Object) null);
        }

        public void wtf(Throwable e) {
            prepareLog(Log.ASSERT, e, null, (Object) null);
        }

        public void wtf(String s, Object... obj) {
            prepareLog(Log.ASSERT, null, s, obj);
        }

        public void wtf(Throwable t, String s, Object... obj) {
            prepareLog(Log.ASSERT, t, s, obj);
        }

        /* -------------------------------------------------------------------------------------- */

        public void json(String s) {
            prepareLog(Log.INFO, null, toJson(s), (Object) null);
        }

        public void json(int priority, String s) {
            prepareLog(priority, null, toJson(s), (Object) null);
        }

        /* -------------------------------------------------------------------------------------- */

        private void prepareLog(int priority, Throwable e, String s, Object... obj) {
            if (priority != Log.ASSERT && !sisLoggable) {
                return;
            }

            String tag = getTag();
            String lineNumber = mExplicitLineNumber.get();
            if (lineNumber != null) {
                mExplicitLineNumber.remove();
            }

            if (s == null) {
                if (e == null) {
                    return;
                }
                s = getStackTraceString(e);
            } else {
                if (obj != null && obj.length > 0) {
                    s = String.format(s, obj);
                }
                if (e != null) {
                    s += "\n" + getStackTraceString(e);
                }
                if (lineNumber != null) {
                    s = lineNumber + s;
                }
            }

            print(priority, tag, s);
        }

        private void print(int priority, String tag, String s) {
            if (s.length() < LOG_MAX_LENGTH) {
                Log.println(priority, tag, s);
                return;
            }

            for (int i = 0, length = s.length(); i < length; i++) {
                int newline = s.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + LOG_MAX_LENGTH);
                    String part = s.substring(i, end);
                    Log.println(priority, tag, part);
                    i = end;
                } while (i < newline);
            }
        }

        private String getTag() {
            String tag = mExplicitTag.get();
            if (tag != null) {
                mExplicitTag.remove();
                return tag;
            }

            int count = TAG_STACK_INDEX;
            if (mExplicitLineNumber.get() != null) {
                count -= 1;
            }

            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length < STACK_MIN_COUNT) {
                throw new IllegalStateException("Didn't have enough elements. Are you using proguard?");
            }

            /*
             * 匿名内部类获取时没有类名, 会这样子显示 {Foo$1}
             * 所以添加方法, 让其显示成 {Foo}
             */
            tag = stackTrace[count].getClassName();
            Matcher m = ANONYMOUS_CLASS.matcher(tag);
            if (m.find()) {
                tag = m.replaceAll("");
            }

            tag = tag.substring(tag.lastIndexOf('.') + 1);
            // API 24 移除了 TAG 的长度限制
            if (tag.length() <= TAG_MAX_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return tag;
            }
            return tag.substring(0, TAG_MAX_LENGTH);
        }

        private String getMethodLineNumber() {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length < STACK_MIN_COUNT) {
                throw new IllegalStateException("Didn't have enough elements. Are you using proguard?");
            }

            int index = METHOD_NUMBER_STACK_INDEX;
            if (mExplicitTag.get() != null) {
                index -= 1;
            }

            String fileName = stackTrace[index].getFileName();
            int lineNumber = stackTrace[index].getLineNumber();
            return "(" + fileName + ":" + lineNumber + ")" + RIGHT_ARROW;
        }

        private String getStackTraceString(Throwable e) {
            StringWriter sw = new StringWriter(256);
            PrintWriter pw = new PrintWriter(sw, false);
            e.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        }

        public String toJson(String json) {
            try {
                if (json.startsWith("{")) {
                    return new JSONObject(json).toString(JSON_INDENT);
                } else if (json.startsWith("[")) {
                    return new JSONArray(json).toString(JSON_INDENT);
                }
            } catch (JSONException e) {
                return e.getMessage() + "\n" + json;
            }
            return "Log error!. Can't parser string to json";
        }
    }
}
