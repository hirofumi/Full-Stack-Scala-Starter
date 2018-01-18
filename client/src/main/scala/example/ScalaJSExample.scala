package example

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import controllers.{CounterAjax, Void}
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Event, HTMLElement}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js



object ScalaJSExample extends js.JSApp {

  implicit def makeIntellijHappy(x: scala.xml.Elem): Binding[HTMLElement] = ???

  /**
    * Ajax Request to server, updates data state with number
    * of requests to count.
    * @param data
    */
  def countRequest(data: Var[String]) = {
    CounterAjax().count(Void()).foreach { count =>
      data.value = count.value.toString
    }
  }

  @dom
  def render = {
    val data = Var("")
    countRequest(data) // initial population
    <div>
      <button onclick={event: Event => countRequest(data) }>
        Boop
      </button>
      From Play: The server has been booped { data.bind } times. Shared Message: {shared.SharedMessages.itWorks}.
    </div>
  }

  def main(): Unit = {
    dom.render(document.body, render)
  }
}
