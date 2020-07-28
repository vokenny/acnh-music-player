package models

import com.typesafe.scalalogging.LazyLogging

case class Song(id: Int, name: String, uri: String) extends LazyLogging {

  val filename: String =
    // Normalized song names
    name.toLowerCase.filter(_.isLetterOrDigit) + ".mp3"

  def isDownloaded: Boolean = Filepath.existsInResources(filename)

}