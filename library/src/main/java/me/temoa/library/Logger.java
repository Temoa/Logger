package me.temoa.library;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static void d(String s) {
        getPrinter().d(s);
    }

    public static void d(String s, Object object) {
        getPrinter().d(s, object);
    }

    public static void e(String s) {
        getPrinter().e(s);
    }

    public static void e(String s, Object object) {
        getPrinter().e(s, object);
    }

    public static void w(String s) {
        getPrinter().w(s);
    }

    public static void w(String s, Object object) {
        getPrinter().w(s, object);
    }

    public static void v(String s) {
        getPrinter().v(s);
    }

    public static void v(String s, Object object) {
        getPrinter().v(s, object);
    }

    public static void i(String s) {
        getPrinter().i(s);
    }

    public static void i(String s, Object object) {
        getPrinter().i(s, object);
    }

    public static void wtf(String s) {
        getPrinter().wtf(s);
    }

    public static void wtf(String s, Object object) {
        getPrinter().wtf(s, object);
    }

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

        public void d(@NonNull String s) {
            print(Log.DEBUG, s);
        }

        public void d(@NonNull String s, Object... obj) {
            print(Log.DEBUG, s, obj);
        }

        public void e(@NonNull String s) {
            print(Log.ERROR, s);
        }

        public void e(@NonNull String s, Object... obj) {
            print(Log.ERROR, s, obj);
        }

        public void w(@NonNull String s) {
            print(Log.WARN, s);
        }

        public void w(@NonNull String s, Object... obj) {
            print(Log.WARN, s, obj);
        }

        public void v(@NonNull String s) {
            print(Log.VERBOSE, s);
        }

        public void v(@NonNull String s, Object... obj) {
            print(Log.VERBOSE, s, obj);
        }

        public void i(@NonNull String s) {
            print(Log.INFO, s);
        }

        public void i(@NonNull String s, Object... obj) {
            print(Log.INFO, s, obj);
        }

        public void wtf(@NonNull String s) {
            print(Log.ASSERT, s);
        }

        public void wtf(@NonNull String s, Object... obj) {
            print(Log.ASSERT, s, obj);
        }

        public void json(String s) {
            print(Log.DEBUG, toJson(s));
        }

        public void json(int priority, String s) {
            print(priority, toJson(s));
        }

        private void print(int priority, String s) {
            if (priority != Log.ASSERT && !sisLoggable) {
                return;
            }

            String tag = getTag();
            String lineNumber = mExplicitLineNumber.get();
            String msg = "";
            if (lineNumber != null) {
                mExplicitLineNumber.remove();
                msg = lineNumber;
            }
            s = msg + s;

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

        private void print(int priority, String s, Object... obj) {
            if (priority != Log.ASSERT && !sisLoggable) {
                return;
            }

            String tag = getTag();
            String lineNumber = mExplicitLineNumber.get();
            String msg = "";
            if (lineNumber != null) {
                mExplicitLineNumber.remove();
                msg = lineNumber;
            }
            msg += String.format(s, obj);
            Log.println(priority, tag, msg);
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

        @NonNull
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

        public String toJson(@NonNull String json) {
            try {
                if (json.startsWith("{")) {
                    return new JSONObject(json).toString(JSON_INDENT);
                } else if (json.startsWith("[")) {
                    return new JSONArray(json).toString(JSON_INDENT);
                }
            } catch (JSONException e) {
                return e.getCause().getMessage() + "\n" + json;
            }
            return "Log error!. Can't parser string to json";
        }
    }
}
