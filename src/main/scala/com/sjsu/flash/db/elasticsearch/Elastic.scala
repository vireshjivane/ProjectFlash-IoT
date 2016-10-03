package com.sjsu.flash.db.elasticsearch

import java.net.InetAddress
import java.util

import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit

import scala.collection.mutable.ListBuffer

object Elastic {

  var client: Option[TransportClient] = None
  var _index : Option[String] = None
  var _type : Option[String] = None

  def initializeElastic(nodeEndpoint: String,clusterName: String, index: String, recType: String) = {
    client = Some(TransportClient.builder()
      .settings(Settings.settingsBuilder().put("cluster.name", clusterName).build()).build()
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(nodeEndpoint), 9300)))
    _index = Some(index)
    _type = Some(recType)
  }

  def createIndex(_index: String, _type: String) = client.get.prepareIndex(_index, _type).setSource(new util.HashMap[String, Object]()).get()
  def updateDocument(_id: String)(json: util.HashMap[String, Object]) = client.get.update(new UpdateRequest(_index.get, _type.get, _id).doc(json)).get
  def upsertDocument(_id: String)(json: util.HashMap[String, Object]) = client.get.update(new UpdateRequest(_index.get, _type.get, _id).doc(json).upsert(json)).get
  def deleteDocument(_id: String) = client.get.prepareDelete(_index.get, _type.get, _id).get
  def bulkUpsert(documents: ListBuffer[util.HashMap[String, Object]]): Unit = {
    val bulkRequest = client.get.prepareBulk
    documents.foreach({ document => bulkRequest.add(new UpdateRequest(_index.get, _type.get, document.get("host").toString).doc(document).upsert(document)) })
  }
  def insertDocument(_id: String)(json: util.HashMap[String, Object]) = client.get.index( new IndexRequest(_index.get, _type.get).source(json)).get

  def getDocuments(scrollSize: Integer,_index: String, _type: String) = {

      val esData = new ListBuffer[String]//new util.ArrayList[util.HashMap[String,Object]]()
      var response : SearchResponse= null
      var i = 0
      while( response == null || response.getHits().hits().length != 0){
        response = client.get.prepareSearch(_index)
          .setTypes(_type)
          .setQuery(QueryBuilders.matchAllQuery())
          .setSize(scrollSize)
          .setFrom(i * scrollSize)
          .execute()
          .actionGet();

        response.getHits().getHits.foreach(entry => esData.append(entry.sourceAsString()))
        i+=1
      }
       esData
    }





}