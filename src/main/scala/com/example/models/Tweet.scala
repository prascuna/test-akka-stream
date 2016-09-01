package com.example.models

final case class Author(handle: String)

final case class Hashtag(name: String)

final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] = body.split(" ").collect { case h if h.startsWith("#") => Hashtag(h) }.toSet
}