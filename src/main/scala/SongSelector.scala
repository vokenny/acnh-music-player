import com.typesafe.scalalogging.LazyLogging
import models.Song

import scala.io.StdIn.readLine

class SongSelector extends LazyLogging {

  def getUserSongChoice(songList: List[Song]): Int = {
    songList.foreach(s => println(s"ID: ${s.id} -> Song Name: ${s.name}"))
    println("Choose a song to play by ID:")
    val chosenSongId: Option[Int] = try {
      Some(readLine().toInt)
    } catch {
      case e: Exception => None
    }

    chosenSongId match {
      case Some(id) => id
      case None =>
        logger.warn("User did not submit an integer for Song ID selection")
        println("Please submit a Song ID number")
        getUserSongChoice(songList)
    }
  }

  // TODO: Build in "pagination" for song list
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
