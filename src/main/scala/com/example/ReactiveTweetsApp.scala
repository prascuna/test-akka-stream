package com.example

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}
import com.example.models.{Author, Hashtag, Tweet}

object ReactiveTweetsApp {
  def main(args: Array[String]) = {
    implicit val system = ActorSystem("reactive-tweets")
    implicit val materializer = ActorMaterializer()

    val tweets: Source[Tweet, NotUsed] = ???

    val akkaHashtag: Hashtag = Hashtag("#akka")

    val authors: Source[Author, NotUsed] = tweets
      .collect { case t: Tweet if t.hashtags.contains(akkaHashtag) => t.author }

    authors.runWith(Sink.foreach(println(_)))


    val ht: Source[Hashtag, NotUsed] = tweets
      .mapConcat(_.hashtags)


    val writeAuthors: Sink[Author, Unit] = ???
    val writeHashtags: Sink[Hashtag, Unit] = ???

    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val bcast = b.add(Broadcast[Tweet](2))
      tweets ~> bcast

      bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
      bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags) ~> writeHashtags
      ClosedShape

    })


  }

}
