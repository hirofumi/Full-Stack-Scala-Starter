package services

import com.google.protobuf.wrappers.Int32Value
import controllers.CounterGrpc.Counter
import java.util.concurrent.atomic.AtomicInteger
import javax.inject._
import scala.concurrent.Future

/**
  * This class is a concrete implementation of the [[Counter]] trait.
  * It is configured for Guice dependency injection in the [[Module]]
  * class.
  *
  * This class has a `Singleton` annotation because we need to make
  * sure we only use one counter per application. Without this
  * annotation we would get a new instance every time a [[Counter]] is
  * injected.
  */
@Singleton
class AtomicCounter extends Counter {
  private val atomicCounter = new AtomicInteger()
  override def count(request: controllers.Void): Future[Int32Value] =
    Future.successful(Int32Value(1 + atomicCounter.getAndIncrement()))
}