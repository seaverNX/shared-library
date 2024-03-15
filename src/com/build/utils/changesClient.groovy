package com.build.utils

@Grab('com.urswolfer.gerrit.client.rest:gerrit-rest-java-client:0.8.8')

import com.google.gerrit.extensions.api.GerritApi
import com.urswolfer.gerrit.client.rest.GerritAuthData
import com.urswolfer.gerrit.client.rest.GerritRestApiFactory
import com.urswolfer.gerrit.client.rest.http.changes.ChangesRestClient

class changesClient implements Serializable{
    GerritRestApiFactory gerritRestApiFactory 
    GerritAuthData.Basic authData 
    GerritApi gerritApi 
    ChangesRestClient changesRestClient

    public changesClient(){
        this.init()
    }

    @NonCPS
    def init(){
        this.gerritRestApiFactory = new GerritRestApiFactory()
        this.authData = new GerritAuthData.Basic('http://172.16.5.149/', 'jenkins', 'nzztMReKEK46bOJ6IpVdsrQYPV+RsZyO/D+uphalag')
        this.gerritApi = gerritRestApiFactory.create(authData)
        this.changesRestClient = gerritApi.changes()
    }

}
