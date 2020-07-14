import java.io.ByteArrayInputStream

import models.Song
import testsupport.BaseSpec

class SongSelectorSpec extends BaseSpec {

  val songSelector = new SongSelector

  val songList: List[Song] = List(
    new Song(1, "song1", "http://localhost:0000/song1"),
    new Song(2, "song2", "http://localhost:0000/song2"),
    new Song(3, "song3", "http://localhost:0000/song3"),
    new Song(4, "song4", "http://localhost:0000/song4"),
    new Song(5, "song5", "http://localhost:0000/song5")
  )

  "getUserSongChoice" should "return Int of user input" in {
    val in = new ByteArrayInputStream("4".getBytes)

    Console.withIn(in)  {
      songSelector.getUserSongChoice(songList) should be (4)
    }
  }

  it should "retry user input until a number is submitted" in {
    val in = new ByteArrayInputStream("\r \ra\r?\r4a\r1".getBytes)

    Console.withIn(in)  {
      songSelector.getUserSongChoice(songList) should be (1)
    }
  }

  "select" should "return Song for a user input matching a song ID" in {
    val in = new ByteArrayInputStream("2".getBytes)
    val resultSong = Console.withIn(in)  {
      songSelector.select(songList)
    }

    resultSong.id should be (songList(1).id)
    resultSong.name should be (songList(1).name)
    resultSong.uri should be (songList(1).uri)
  }

  it should "retry user input until a number matching a song ID in a given list is submitted" in {
    val in = new ByteArrayInputStream("\r \ra\r?\r1a\r3".getBytes)
    val resultSong = Console.withIn(in)  {
      songSelector.select(songList)
    }

    resultSong.id should be (songList(2).id)
    resultSong.name should be (songList(2).name)
    resultSong.uri should be (songList(2).uri)
  }

}
