import java.nio.file.{Path, Paths}
import java.util.concurrent.TimeUnit

import com.malliina.audio.javasound.FileJavaSoundPlayer
import com.typesafe.scalalogging.LazyLogging
import models.{ApplicationState, Filepath, Song}

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn.readLine

object AcnhMusicPlayer extends App with LazyLogging {

  var appState = ApplicationState.Startup

  val acnhApiConnector: AcnhApiConnector = new AcnhApiConnector
  val songSelector: SongSelector = new SongSelector

  lazy val songList: List[Song] =
    Await.result(acnhApiConnector.songListFut, Duration(10000, TimeUnit.MILLISECONDS)).toList

  var songChoice = Song(0, "", "")

  @tailrec
  def continueCheck(message: String): Unit = {
    println(message)
    val action: String = readLine()

    action.toLowerCase match {
      case "select" =>
        appState = ApplicationState.SongSelect
        logger.debug(s"ApplicationState: $appState")
      case "quit" => System.exit(0)
      case _ =>
        logger.warn(s"continueCheck action not recognized: '$action'")
        println("Action not recognized")
        continueCheck(message)
    }
  }

  def playSong(song: Song): FileJavaSoundPlayer = {
    val file: Path = Paths get Filepath(song.filename).getCanonicalPath
    val player: FileJavaSoundPlayer = new FileJavaSoundPlayer(file)
    player.play()
    logger.debug(s"PlayerState: ${player.state}")

    appState = ApplicationState.Playing
    logger.debug(s"ApplicationState: $appState")
    player
  }

  println(
    s"""Welcome to ACNH Music Player!
       |This application can play the 95 K.K. Slider Songs from Animal Crossing: New Horizons
       |""".stripMargin)

  continueCheck("Select a song to play or Quit ACNH Music Player?\n'Select' or 'Quit'")

  do {
    if (appState == ApplicationState.SongSelect) {
      songChoice = songSelector.select(songList)
      songSelector.downloadCheck(songChoice)
    }

    // TODO: Add continuous play by checking player state
    //  instead of waiting for User to select another song
    // TODO: Add option for shuffle play too

    // Player and Song are unable to be decoupled, since you need a File to create a new Player
    // This also means it's not recommended or possible to reuse a Player when changing song, or when a song ends
    // which makes continuous play slightly more complex
    var player: FileJavaSoundPlayer = playSong(songChoice)

    Thread.sleep(2000) // Wait for player logs to print first
    println(s"Playing ${songChoice.name}")
    continueCheck("Select another song to play or Quit ACNH Music Player?\n'Select' or 'Quit'")

    player.stop()
  } while (appState == ApplicationState.SongSelect || appState == ApplicationState.Playing)

}