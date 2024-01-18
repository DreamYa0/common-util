package com.g7.framwork.common.util.extension;

import java.io.PrintWriter;

/**
 * @author dreamyao
 * @description
 * @date 2017/12/14 下午9:28
 * @since 1.0.0
 */
public final class StringUtils {

    private StringUtils() {

    }

    protected static String toString(Throwable e) {
        UnsafeStringWriter w = new UnsafeStringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName());
        if (e.getMessage() != null) {
            p.print(": " + e.getMessage());
        }
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }
}
