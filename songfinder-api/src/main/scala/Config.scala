/**
 * Copyright (c) 2010 Christoph Schmidt
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of his contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.unsane.radio.songfinder

import java.util.Properties
import java.io._

object Config {

  private[this] val userDir = System.getProperty("user.dir")
  private[this] val userHome = System.getProperty("user.home")
  private[this] val configFile = userDir + "/../settings.properties"
  private[this] val props = {
    val p = new Properties
    p load new FileInputStream(configFile)
    p
  }

  private[this] val ripperWishSongsPath = userHome + "/Songfinder/WishList"
  private[this] val ripperPlayListPath = userHome + "/Songfinder/PlayList"
  private[this] val ripperSongsPath = userHome + "/Songfinder/Songs"

  val wishSongsPath = userHome + "/Songfinder/WishList/songs.xml"
  val playlistPath = userHome + "/Songfinder/PlayList/songs.xml"

  val ripper = props getProperty "ripper"
  val pathParameter = props getProperty "ripperPathParameter"
  val path = ripperSongsPath
  val lengthParameter = props getProperty "ripperLengthParameter"
  val titleParameter = props getProperty "ripperTitleParameter"

  def checkConfig() {
    val songs = new File(ripperSongsPath)
    val wishList = new File(ripperWishSongsPath)
    val playList = new File(ripperPlayListPath)
    def doesNotExistsMsg(path: String) =
      "[radio/songfinder-config] " + path + " does not exist, try to create ..."
    if(!songs.exists) {
      println(doesNotExistsMsg(ripperSongsPath))
      songs.mkdirs()
    }
    if(!wishList.exists) {
      println(doesNotExistsMsg(ripperWishSongsPath))
      wishList.mkdirs()
    }
    if(!playList.exists) {
      println(doesNotExistsMsg(ripperPlayListPath))
      playList.mkdirs()
    }
  }
}
