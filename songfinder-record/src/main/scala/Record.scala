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
package record

import org.osgi.framework.BundleContext

class Record(val context: BundleContext) {

  def record(songsToRecord: List[Song]) {
    val now = getTime
    songsToRecord filter { _.time == now } match {
      case Nil => println("[radio/songfinder-record] nothing to record")
      case songs => songs map record
    }
  }

  private[this] def record(song: Song) {
    import song._
    import java.io.{ BufferedReader, InputStreamReader }
    import Config._
    println("[radio/songfinder-record] recording " + title)
    val process = Runtime.getRuntime().exec(
      ripper + " " + source + " " + pathParameter + " " +
      path + " " + lengthParameter + " " + length + " " +
      titleParameter + " " +
      (title filter { _ !=' ' })
    )
    val r = new BufferedReader(new InputStreamReader(process.getInputStream()))
    while ((r.readLine()) != null) {}
    r.close()
    println("[radio/sonfinder-record] finish recording " + title)
    removeFromWishList(title)
  }

  private[this] def getTime: String = {
    import java.util.Calendar._
    val c = getInstance
    val hour = c.get(HOUR_OF_DAY)
    val minute = c.get(MINUTE)
    hour + (if (minute<=9) "0" else "") + minute
  }

  private[this] def removeFromWishList(title: String) {
    println("[radio/songfinder-record] call radio/songfinder-create to clean" +
            title + " from WishSongs")
    import com.weiglewilczek.scalamodules._
    context findService withInterface[Songfinder] andApply { _.remove(title) }
  }
}
