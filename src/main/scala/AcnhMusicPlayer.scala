import java.nio.file.{Path, Paths}
import java.util.concurrent.TimeUnit

import com.malliina.audio.javasound.FileJavaSoundPlayer
import com.typesafe.scalalogging.LazyLogging
import models.{ApplicationState, Filepath, Song}

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object AcnhMusicPlayer extends App with LazyLogging {

  var appState = ApplicationState.Startup

  val acnhApiConnector: AcnhApiConnector = new AcnhApiConnector
  val songSelector: SongSelector = new SongSelector
  val fileDownloader: FileDownloader = new FileDownloader

  lazy val songList: List[Song] =
    Await.result(acnhApiConnector.songListFut, Duration(10000, TimeUnit.MILLISECONDS)).toList

  @tailrec
  def continueCheck(message: String): Unit = {
    println(message)
    val action: String = readLine()

    action.toLowerCase match {
      case "select" =>
        appState = ApplicationState.SongSelect
        logger.debug(s"ApplicationState: $appState")
      case "quit" =>
        appState = ApplicationState.End
        System.exit(0)
      case _ =>
        logger.warn(s"continueCheck action not recognized: '$action'")
        println("Action not recognized")
        continueCheck(message)
    }
  }

  println("Welcome to ACNH Music Player!")

  do {
    logger.debug(s"ApplicationState: $appState")
    if (appState == ApplicationState.Startup) continueCheck("Select a song to play or Quit ACNH Music Player? 'Select' or 'Quit'")

    val chosenSong = songSelector.select(songList)

    if (!Filepath.existsInResources(chosenSong.filename)) {
      logger.info(s"Song not found in resources directory. Downloading ${chosenSong.name}")
      fileDownloader.download(chosenSong.uri, chosenSong.filename)
    }

    val file: Path = Paths get Filepath(chosenSong.filename).getCanonicalPath
    val player: FileJavaSoundPlayer = new FileJavaSoundPlayer(file)
    player.play()
    logger.debug(s"PlayerState: ${player.state}")

    appState = ApplicationState.Playing
    logger.debug(s"ApplicationState: $appState")

    // TODO: Add continuous play by checking player state
    //  instead of waiting for User to select another song
    // TODO: Add option for shuffle play too

    Thread.sleep(2000) // Wait for player logs to print first
    continueCheck("Select another song to play or Quit ACNH Music Player? 'Select' or 'Quit'")

    player.stop()
  } while (appState == ApplicationState.SongSelect || appState == ApplicationState.Playing)

}