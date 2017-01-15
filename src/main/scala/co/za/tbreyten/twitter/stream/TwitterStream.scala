package co.za.tbreyten.twitter.stream

import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.Status

import scala.io.Source

object TwitterStream extends App {

  val sparkConfiguration = new SparkConf().
    setAppName("twitter-stream").
    setMaster("local[*]")

  val sparkContext = new SparkContext(sparkConfiguration)

  val streamingContext = new StreamingContext(sparkContext, Seconds(5))

  val source = Source.fromInputStream(getClass.getResourceAsStream("/filters.txt"))
  val filteredWords = source.getLines.toList
  source.close()

  // Creating a stream from Twitter
  val tweets: DStream[Status] =
    TwitterUtils.createStream(streamingContext, None)

  //extract words in order to apply filters
  val textAndWords: DStream[(String, Seq[String])] =
    tweets.
      map(_.getText).
      map(tweetText => (tweetText, tweetText.split(" ")))

  //apply filter (throw away tweets without filtered words)
  //use the words for more than just filtering ? some basic analytics ?
  val filteredTweets: DStream[(String, Seq[String])] =
    textAndWords.
      mapValues(filterText).
      filter { case (_, words) => words.length > 0}

  filteredTweets.map(formatText).print

  //start stream and await termination
  streamingContext.start()
  streamingContext.awaitTermination()


  //funtions
  //move functions to package ???

  //map all text to lowercase in order to apply filters
  def filterText(words: Seq[String]): Seq[String] = {
    words.map(_.toLowerCase).filter(filteredWords.contains(_))
  }

  //add any other formatting
  def formatText(data: (String, Seq[String])): String = {
    s"${data._1.takeWhile(_ != '\n')}"
  }
}