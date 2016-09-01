package com.example

import java.io.File

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString
import scala.concurrent.duration._

import scala.concurrent.Future

object Hello {
  def main(args: Array[String]): Unit = {

    val source: Source[Int, NotUsed] = Source(1 to 100)

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val factorial = source.scan(BigInt(1))((acc, curr) => acc * curr)

    factorial
      .map(_.toString)
      .runWith(lineSink("factorial.txt"))


    factorial
      .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
      .throttle(1, 1 second, 1, ThrottleMode.Shaping)
      .runForeach(println(_))

  }

  def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(n => ByteString(s"$n\n"))
      .toMat(FileIO.toFile(new File(filename)))(Keep.right)
}
