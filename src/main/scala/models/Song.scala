package models

class Song(idNumber: Int, songName: String, musicUri: String) {

  val id: Int = this.idNumber
  val name: String = this.songName
  val uri: String = this.musicUri

  def filename: String =
    // Normalized song names
    name.toLowerCase.filter(_.isLetterOrDigit) + ".mp3"

}