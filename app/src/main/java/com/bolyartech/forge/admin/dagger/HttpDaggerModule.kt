package com.bolyartech.forge.admin.dagger

import android.annotation.SuppressLint
import com.bolyartech.forge.admin.misc.LoggingInterceptor
import com.bolyartech.forge.base.http.cookie_jar.PersistentCookieJar
import com.bolyartech.forge.base.http.cookie_jar.cache.SetCookieCache
import com.bolyartech.forge.base.http.cookie_jar.persistence.CookiePersistor
import dagger.Module
import dagger.Provides
import okhttp3.Cookie
import okhttp3.OkHttpClient
import java.security.cert.CertificateParsingException
import java.security.cert.X509Certificate
import java.util.*
import javax.inject.Singleton
import javax.net.ssl.X509TrustManager

@Module
class HttpDaggerModule {


    private val okHttpClient: OkHttpClient = createOkHttpClient()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return okHttpClient
    }

    private fun createOkHttpClient(): OkHttpClient {
        val b = OkHttpClient.Builder()
        b.addInterceptor(LoggingInterceptor())

        val cookieJar = PersistentCookieJar(SetCookieCache(), createFakePersistor())
        b.cookieJar(cookieJar)

        return b.build()
    }

    private fun getSubjectAltNames(certificate: X509Certificate, type: Int): List<String> {
        try {
            val subjectAltNames = certificate.subjectAlternativeNames ?: return emptyList()
            val result = mutableListOf<String>()
            for (subjectAltName in subjectAltNames) {
                if (subjectAltName == null || subjectAltName.size < 2) continue
                if (subjectAltName[0] != type) continue
                val altName = subjectAltName[1] ?: continue
                result.add(altName as String)
            }
            return result
        } catch (_: CertificateParsingException) {
            return emptyList()
        }
    }

    private fun createFakePersistor(): CookiePersistor {
        return object : CookiePersistor {
            override fun loadAll(): List<Cookie> {
                return ArrayList()
            }


            override fun saveAll(collection: Collection<Cookie>) {}


            override fun removeAll(collection: Collection<Cookie>) {}


            override fun clear() {}
        }
    }


    private fun createDummyTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf<java.security.cert.X509Certificate>()
            }


            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(
                certs: Array<java.security.cert.X509Certificate>, authType: String
            ) {
            }


            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(
                certs: Array<java.security.cert.X509Certificate>, authType: String
            ) {
            }
        }
    }
}