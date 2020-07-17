package models

case class Song(id: Int, name: String, uri: String) {

  val filename: String =
    // Normalized song names
    name.toLowerCase.filter(_.isLetterOrDigit) + ".mp3"

}