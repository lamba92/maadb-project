package edu.unito.maadb.analytics.nosql

import edu.unito.maadb.analytics.core.getServer

fun main() {
  getServer(Datasource).start(true)
}
