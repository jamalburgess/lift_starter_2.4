package bootstrap.liftweb

import net.liftweb._
import sitemap.Loc.TemplateBox
import util._

import common._
import http._
import sitemap._
import com.fmpwizard.util.{InitPort, ArduinoBridge}


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("com.fmpwizard")

    // Build SiteMap
    val entries = List(
      Menu.i("Door") / "index" >> TemplateBox(() => Templates( "doorcontrol" :: Nil))
    )

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))


    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // set DocType to HTML5
    LiftRules.htmlProperties.default.set((r: Req) =>new Html5Properties(r.userAgent))

    ArduinoBridge ! InitPort("/dev/tty.usbmodemfa131")

  } //boot

} //Boot
