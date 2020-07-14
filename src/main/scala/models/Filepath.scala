package models

import java.io.File

object Filepath {

  def apply(sourcePath: String): File = new File(s"src/main/resources/$sourcePath")

  private def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }.toList
  }

  def existsInResources(sourcePath: String): Boolean = {
    val mp3List: List[File] = getListOfFiles(new File("src/main/resources"), List("mp3"))

    if (mp3List.contains(Filepath(sourcePath))) true else false
  }

}
