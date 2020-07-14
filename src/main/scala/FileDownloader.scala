import sys.process._
import java.net.URL
import java.io.File

class FileDownloader {

  def download(url: String, filename: String): String = {
    // #> method writes output of the URL to the given file
    // !! method starts the process in a blocking fashion
    new URL(url) #> new File(s"src/main/resources/$filename") !!
  }

}
