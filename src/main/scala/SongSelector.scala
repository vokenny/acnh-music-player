import com.typesafe.scalalogging.LazyLogging
import models.Song

import scala.io.StdIn.readLine

class SongSelector extends LazyLogging {

  def getUserSongChoice(songList: List[Song]): Int = {
    def showSongs(): Unit = songList.take(10).foreach(s => println(s"ID: ${s.id} -> Song Name: ${s.name}"))

    showSongs()
    println("Choose a song to play by ID:")
    val chosenSongId: Either[String, Int] = {
      val userInput: String = readLine()
      if (userInput.nonEmpty) {
        if (userInput.forall(_.isDigit)) Right(userInput.toInt) else Left(userInput)
      } else Left(userInput)
    }

    chosenSongId match {
      case Right(id) => id
      case Left(value) if value.equalsIgnoreCase("next") =>
        val (firstTen, rest) = songList.splitAt(10)
        getUserSongChoice(rest ::: firstTen)
      case _ =>
        logger.info(s"User did not submit an integer or 'Next' for Song ID selection. They submitted: '$chosenSongId'")
        println("Please submit a Song ID number or 'Next' to see more songs")
        getUserSongChoice(songList)
    }
  }

  def select(songList: List[Song]): Song = {
    val chosenSongId: Int = getUserSongChoice(songList)
    val filteredSongList: List[Song] = songList.filter(s => s.id == chosenSongId)

    filteredSongList match {
      case s :: Nil => s
      case _ =>
        logger.warn(s"Cannot find song ID: $chosenSongId")
        logger.warn(s"Found $filteredSongList in filteredSongList")
        println(s"Cannot find song ID: $chosenSongId")
        select(songList)
    }
  }

}
