# ACNH Music Player

This Scala application runs from the terminal.

1. It uses Scalaj-HTTP to make requests to [ACNH API](http://acnhapi.com/) by Alexis Lours, to get information about the 95 K.K. Slider songs
2. Transforms them into Song objects
3. Takes user input in the terminal to select a song
4. Downloads the song using song information if it's not already downloaded
5. Lastly, plays them using [Util-audio](https://github.com/malliina/util-audio) by malliina

The music assets used are the sole property of Nintendo.

You can pick and choose songs at will, however it doesn't offer continuous or shuffle play yet, so you need to select a new song if the song finishes.

To run the application, navigate to the project directory and use the following command:

    sbt run

Similarly, to run tests:

    sbt test
