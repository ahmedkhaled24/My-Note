package com.example.mynoteapp

class Note {
    var nodeID:Int?=null
    var nodeName:String?=null
    var nodeDes:String?=null
//    var nodeDate:String? = null

    constructor(nodeID:Int,nodeName:String,nodeDes:String/*,nodeDate:String*/){
        this.nodeID = nodeID
        this.nodeName = nodeName
        this.nodeDes = nodeDes
//        this.nodeDate = nodeDate
    }
}