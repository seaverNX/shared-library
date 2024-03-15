package com.build.utils

@Grab('com.urswolfer.gerrit.client.rest:gerrit-rest-java-client:0.8.8')
import com.urswolfer.gerrit.client.rest.http.changes.ChangesRestClient
import com.google.gerrit.extensions.api.changes.ReviewInput

class gerritReview implements Serializable{
    Integer changeId
    Integer patchId
    String result
    String url
    ReviewInput reviewInput = new ReviewInput()
    ChangesRestClient changesRestClient
    gerritReview (Integer changeId,Integer patchId, String result, String url){
        this.gerrit
        this.changeId = changeId
        this.patchId = patchId
        this.result = result
        this.url = url
        this.changesRestClient = new changesClient().changesRestClient
    }

    def setReview (){
        if (this.result == 'SUCCESS'){
            this.reviewInput.label("Verified", 1).message("Build Successful!\n${this.url}: ${result}" );
        }else{
            this.reviewInput.label("Verified", -1).message("Build Failed!\n${this.url}: ${result}");
        }
        def reviewResult = this.changesRestClient.id(this.changeId).revision(this.patchId).review(this.reviewInput)
        return reviewResult
    }

}
