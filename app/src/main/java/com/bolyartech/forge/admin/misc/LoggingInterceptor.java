package com.bolyartech.forge.admin.misc;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.bolyartech.forge.base.misc.ExceptionTools.getRootCause;


/**
 * Logging interceptor to be used for debugging
 */
@SuppressWarnings("unused")
public class LoggingInterceptor implements Interceptor {
    @SuppressWarnings("unused")
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("unused")
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        mLogger.debug(String.format("Sending request %s", request.url()));

        try {
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            mLogger.debug(String.format(Locale.US, "Received response with code: %d for %s in %.1fms", response.code(),
                    response.request().url(), (t2 - t1) / 1e6d));

            return response;
        } catch (IOException e) {
            //noinspection ThrowableResultOfMethodCallIgnored
            mLogger.warn("Problem executing HTTP request: {}", getRootCause(e).getMessage());
            throw e;
        }
    }
}