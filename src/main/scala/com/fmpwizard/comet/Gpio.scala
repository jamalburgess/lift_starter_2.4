package com.fmpwizard
package comet


import com.fmpwizard._
import gpio.Controller._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.json._
import js.{JsCmds, JE, JsCmd}
import com.pi4j.io.gpio.GpioPinDigitalOutput
import net.liftweb.common.Loggable


class Gpio extends CometActor with Loggable with CometListener {

  def registerWith = GpioCometManager

  def render = {
    "#pinRow *" #> (1 to 10).toList.map{ p =>
      "#pin *"            #> ("pin status: " + str2Pin("pin" + p.toString).isHigh) &
      "#pin [id]"         #> ("pin" + p.toString) &
      "#pinTitle *"       #> ("Pin " + p.toString + ": ") &
      "#toggle [onclick]" #> SHtml.jsonCall(JE.JsRaw("""{"pin" : "pin%s"}""".format(p)), togglePin _)
    }


  }
  override def lowPriority = {
    case PinUp(pin)     => pin.high()
    case PinDown(pin)   => pin.low()
    case PinToggle(pin) =>
      pin.toggle()
      partialUpdate(JE.JsRaw("""$("#%s").html("pin status: %s")""".format(pin.getName, pin.isHigh)).cmd)
  }

  private[this] def togglePin(pin: JValue): JsCmd = {
    GpioCometManager ! PinToggle(jv2Pin(pin))
    JsCmds.Noop
  }

  private[this] def jv2Pin(in: JValue): GpioPinDigitalOutput = {
    implicit val foprmats = DefaultFormats
    logger.debug("in is %s" format in)
    logger.debug("pin is %s" format ((in \ "pin" ).extract[String]))
    (in \ "pin").extract[String] match {
      case "pin1" => pin1
      case "pin2" => pin2
      case "pin3" => pin3
      case "pin4" => pin4
      case "pin5" => pin5
      case "pin6" => pin6
      case "pin7" => pin7
      case "pin8" => pin8
      case "pin9" => pin9
      case "pin10" => pin10
      case _ => println("ee") ; pin1
    }
  }

  private[this] def str2Pin(s: String): GpioPinDigitalOutput = {
    jv2Pin(JObject(List(JField("pin",JString(s)))))
  }

}