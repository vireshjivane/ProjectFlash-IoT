package com.sjsu.flash.db.mongo

import com.mongodb.casbah.{MongoCollection, MongoConnection}

object MongoConfigurator {

    var connection : Option[MongoConnection] = None
    def initializeClient() = connection = Some(MongoConnection(“x.x.x.x”,27017))
    def getCollection(db:String,collection:String) : MongoCollection = connection.get(db)(collection)
}
