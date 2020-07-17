import models.Song
import testsupport.BaseSpec

class SongSpec extends BaseSpec {

  val songList: List[Song] = (for (i <- 1 to 20) yield {
    Song(i, s" S o Ng - & NaM E 0*$i", s"http://localhost:0000/v1/music/$i")
  }).toList

  "filename" should "produce a normalized mp3 filename with only lowercase letters and digits (no spaces)" in {
    songList.head.filename should be ("songname01.mp3")
  }

  it should "have a unique filename specific to the Song" in {
    songList.foreach(s => s.filename should be (s"songname0${s.id}.mp3"))
  }
}
