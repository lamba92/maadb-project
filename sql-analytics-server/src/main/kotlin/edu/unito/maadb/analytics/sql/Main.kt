package edu.unito.maadb.analytics.sql

import edu.unito.maadb.analytics.core.getServer

fun main() {
  getServer(Datasource).start(true)
}
