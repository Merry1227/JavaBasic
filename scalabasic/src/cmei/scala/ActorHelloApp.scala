package cmei.scala

import scala.actors.Actor

/**
  * ActorHelloApp
  *
  * @author meicanhua
  *
  *
  **/
object ActorHelloApp extends Actor {

  import java.net.{ InetAddress, UnknownHostException }

  def act(): Unit = {
    react {

      case (name: String, actor: Actor) =>
        actor ! getIp(name)
        act()
      case "EXIT" =>
        println("Name resolver existing.")
      case msg =>
        println("Unhandled messages: " + msg)
        act()
    }
  }

  def getIp(name: String): Option[InetAddress] = {
    try {
      Some(InetAddress.getAllByName(name)(0))
    } catch {
      case _: UnknownHostException => None
    }
  }

}

class newList extends List


object Main extends App {



    ActorHelloApp.start()
    ActorHelloApp ! ("www.baidu.com", ActorHelloApp)
    ActorHelloApp ! ("www.scala-lang.org", ActorHelloApp)




}
