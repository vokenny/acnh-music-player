import models.Song
import testsupport.BaseSpec

class SongSpec extends BaseSpec {

  "filename" should "produce a normalized mp3 filename with only lowercase letters and digits (no spaces)" in {
    val song = Song(1, " S o Ng - & NaM E 0*1", "http://localhost:0000/v1/music/1")

    song.filename should be ("songname01.mp3")
  }

}
