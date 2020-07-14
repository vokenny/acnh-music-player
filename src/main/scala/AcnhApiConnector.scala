import com.typesafe.scalalogging.LazyLogging
import models.Song
import play.api.libs.json.{JsValue, Json}
import scalaj.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AcnhApiConnector extends RequestBuilder with LazyLogging {

  def getKkSong(id: Int): HttpResponse[String] = {
    logger.debug(s"Making GET request for song Id $id")
    GET(s"http://www.acnhapi.com/v1/songs/$id")
  }

  // TODO: What to do with failed requests after 3 attempts?
  def songListSeqFut: Seq[Future[HttpResponse[String]]] = {
    // There are 95 K.K. Slider songs in Animal Crossing: New Horizons
    for (i <- 1 to 5) yield {
      Future {
        val resp = getKkSong(i)
        logger.debug(s"GET request for song Id $i received ${resp.statusLine}")
        retryRequest(resp, 3)(getKkSong(i))
      }
    }
  }

  def transformToSong(resp: Future[HttpResponse[String]]): Future[Song] = {
    resp.map { r =>
      val json: JsValue = Json.parse(r.body)
      (for {
        id <- (json \ "id").validate[Int]
        name <- (json \ "name" \ "name-EUen").validate[String]
        musicUri <- (json \ "music_uri").validate[String]
      } yield new Song(id, name, musicUri)).get
    }
  }

  def songListFut: Future[Seq[Song]] = Future.traverse(songListSeqFut)(transformToSong)
}
