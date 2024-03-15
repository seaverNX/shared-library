package com.build.utils

@Grab('com.urswolfer.gerrit.client.rest:gerrit-rest-java-client:0.8.8')
import com.urswolfer.gerrit.client.rest.http.changes.ChangesRestClient
import com.google.gerrit.extensions.api.changes.ReviewInput




class gerritReview implements Serializable {
    ChangesRestClient changesRestClient
    gerritReview (Integer changeId,Integer patchId, String result, String url){
        ReviewInput reviewInput = new ReviewInput();
        this.changesRestClient = new changesClient().changesRestClient
        if (result == 'SUCCESS'){
            reviewInput.label("Verify", 1)
            reviewInput.message("Verify Build pass!\n${url}" );
        }else{
            reviewInput.label("Verify", -1)
            reviewInput.message("Verify Build failed!\n${url}");
        }
        try{
            def reviewResult = this.changesRestClient.id(changeId).revision(patchId).review(reviewInput)
            println reviewResult
        }catch(Exception ex) {
         println("Catching the exception");
      }
        
    }

}
