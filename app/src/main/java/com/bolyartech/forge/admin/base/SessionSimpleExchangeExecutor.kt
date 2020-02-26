//package com.bolyartech.forge.admin.base
//
//import com.bolyartech.forge.base.exchange.HttpExchange
//import com.bolyartech.forge.base.exchange.ResultProducer
//import com.bolyartech.forge.base.exchange.SimpleExchangeResult
//import com.bolyartech.forge.base.session.Session
//import java.io.IOException
//import javax.inject.Inject
//
//interface SessionSimpleExchangeExecutor {
//    @Throws(IOException::class, ResultProducer.ResultProducerException::class)
//    fun execute(x: HttpExchange<SimpleExchangeResult>): SimpleExchangeOutcome
//}
//
//
//class SessionSimpleExchangeExecutorImpl @Inject
//constructor(private val session: Session) :
//    SessionSimpleExchangeExecutor {
//
//    @Throws(IOException::class, ResultProducer.ResultProducerException::class)
//    override fun execute(x: HttpExchange<SimpleExchangeResult>): SimpleExchangeOutcome {
//        return try {
//            val ret = x.execute()
//            session.prolong()
//
//            SimpleExchangeOutcomeOk(ret.httpCode, ret.payload)
//        } catch (e: Exception) {
//            ExchangeOutcomeException(e)
//        }
//    }
//}
//
//sealed class SimpleExchangeOutcome
//data class SimpleExchangeOutcomeOk(private val httpCode: Int, private val payload: String) : SimpleExchangeOutcome()
//
//class ExchangeOutcomeException(val e: Exception) : SimpleExchangeOutcome()
//
//
