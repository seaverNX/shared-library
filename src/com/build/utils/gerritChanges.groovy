package com.build.utils

@Grab('com.urswolfer.gerrit.client.rest:gerrit-rest-java-client:0.8.8')
import com.google.gerrit.extensions.common.ChangeInfo
import com.google.gerrit.extensions.api.GerritApi
import com.urswolfer.gerrit.client.rest.GerritAuthData
import com.urswolfer.gerrit.client.rest.GerritRestApiFactory
import com.urswolfer.gerrit.client.rest.http.changes.ChangesRestClient


class gerritChanges implements Serializable{
    Integer Id
    def changes = []
    GerritRestApiFactory gerritRestApiFactory = new GerritRestApiFactory()
    @NonCPS
    GerritAuthData.Basic authData = new GerritAuthData.Basic('http://172.16.5.149/', 'jenkins', 'nzztMReKEK46bOJ6IpVdsrQYPV+RsZyO/D+uphalag')
    GerritApi gerritApi = gerritRestApiFactory.create(authData)
    ChangesRestClient changesRestClient = gerritApi.changes()

    public gerritChanges(Integer id){
        this.Id = id
        this.getChangesOfTopic()
    }

    @NonCPS
    def getPatchId(Integer id){
        def changeApiRestClient = this.changesRestClient.id(id)
        def versionInfo = changeApiRestClient.get()
        def revision =  versionInfo.currentRevision
        return versionInfo.revisions.get(revision)._number
    }
    @NonCPS
    def getTopic(Integer id){
        def changeApiRestClient = this.changesRestClient.id(id)
        def versionInfo = changeApiRestClient.get()
        return versionInfo.topic
    }

    @NonCPS
    def getChangesOfTopic(){
        def topic =  getTopic(this.Id)
        def queryTopic='status:open+topic:' + topic
        def result = []
        def change
        List<ChangeInfo> changes = this.changesRestClient.query(queryTopic).get()
        changes.each{ci ->
            change = [:]
            change['number'] = ci._number
            change['project'] = ci.project
            change['patch'] = getPatchId(ci._number)
            result.add(change)
        }
        this.changes = result.sort()
    }

    @NonCPS
    def queryStaus(){
        def query='status:open+'+ 'change:'+ id.toString() +'+Verified=ok'
        def result = true
        def change
        println("Quary status: " + query)
        List<ChangeInfo> changes = this.changesRestClient.query(query).get()
        if(changes.size()>0){
            result = false
        }
        return result
    }

    def getFiles( Integer patchId){
        def mapFiles = [:]
        def revisionApiRestClient =  this.changesRestClient.id(this.Id).revision(patchId.toString())
        def scanFiles  = new HashMap<String,List>()
        def listCpp = new ArrayList()
        def listJava = new ArrayList()
        revisionApiRestClient.files().each{ f->
            if(f.getKey().matches("(.*).(cc|h|cpp|cu|cuh|c)")){
                listCpp.add(f.getKey())
            }
            if(f.getKey().matches("(.*).(java)")){
                listJava.add(f.getKey())
            }
        }
        if(listCpp.size() >0){
            mapFiles.put('cpplint',listCpp)
        }
        if(listJava.size()>0){
            mapFiles.put('google-java-format',listJava)
        }
        return mapFiles
       }
}
