import com.typesafe.scalalogging.LazyLogging
import scalaj.http.{Http, HttpOptions, HttpResponse}

trait RequestBuilder extends LazyLogging {

  // TODO: Add exponential backoff
  def retryRequest(resp: HttpResponse[String], count: Int)(f: => HttpResponse[String]): HttpResponse[String] = {
    if (count == 0) resp
    else {
      if (resp.isServerError) {
        val r = f
        retryRequest(r, count - 1)(f)
      } else resp
    }
  }

  def GET(url: String, header: Map[String, String]*): HttpResponse[String] = {
    Http(url)
      .headers(header.flatten)
      .option(HttpOptions.readTimeout(10000))
      .asString
  }

}
