package models

object ApplicationState extends Enumeration {

  type State = Value
  val Startup, SongSelect, Playing = Value

}
