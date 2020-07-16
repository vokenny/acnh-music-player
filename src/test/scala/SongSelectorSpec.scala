import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import models.Song
import testsupport.BaseSpec

class SongSelectorSpec extends BaseSpec {

  val songSelector = new SongSelector

  val songList: List[Song] = (for (i <- 1 to 20) yield {
    new Song(i, s"songName$i", s"http://localhost:0000/v1/music/$i")
  }).toList

  "getUserSongChoice" should "print out the first ten songs" in {
    val in = new ByteArrayInputStream("1".getBytes)
    val out = new ByteArrayOutputStream
    val result =
      s"""ID: 1 -> Song Name: songName1
         |ID: 2 -> Song Name: songName2
         |ID: 3 -> Song Name: songName3
         |ID: 4 -> Song Name: songName4
         |ID: 5 -> Song Name: songName5
         |ID: 6 -> Song Name: songName6
         |ID: 7 -> Song Name: songName7
         |ID: 8 -> Song Name: songName8
         |ID: 9 -> Song Name: songName9
         |ID: 10 -> Song Name: songName10
         |Choose a song to play by ID:
         |""".stripMargin

    Console.withOut(out) {
      Console.withIn(in) {
        songSelector.getUserSongChoice(songList)
      }
    }

    out.toString should be (result)
  }

  "getUserSongChoice" should "return Int of user input" in {
    val in = new ByteArrayInputStream("4".getBytes)

    Console.withIn(in) {
      songSelector.getUserSongChoice(songList) should be (4)
    }
  }

  it should "retry user input until a number is submitted" in {
    val in = new ByteArrayInputStream("\r \ra\r?\r4a\r1".getBytes)

    Console.withIn(in) {
      songSelector.getUserSongChoice(songList) should be (1)
    }
  }

  "select" should "return Song for a user input matching a song ID" in {
    val in = new ByteArrayInputStream("2".getBytes)
    val resultSong = Console.withIn(in) {
      songSelector.select(songList)
    }

    resultSong.id should be (songList(1).id)
    resultSong.name should be (songList(1).name)
    resultSong.uri should be (songList(1).uri)
  }

  it should "retry user input until a number matching a song ID in a given list is submitted" in {
    val in = new ByteArrayInputStream("\r \ra\r?\r1a\r3".getBytes)
    val resultSong = Console.withIn(in) {
      songSelector.select(songList)
    }

    resultSong.id should be (songList(2).id)
    resultSong.name should be (songList(2).name)
    resultSong.uri should be (songList(2).uri)
  }

}
