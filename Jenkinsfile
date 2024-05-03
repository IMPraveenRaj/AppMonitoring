#!groovy
import groovy.json.JsonSlurper;
import net.jpmchase.gti.jules.util.YamUtil
import groovy.json.JsonOutPut

@Library('julesGlobalLibrary@6.STABLE') _
buildPipeline()

def buildPipeline(){

   jules_pipelineRunner{
   yml='jules.yml'
   runCanary=runCanary()
   imageUri=getImageUri
   }
}

def runCanary() {
{ steps,domain,config ->
String canaryApiUrl = 'https://cloudcanary-api-gaiacloud.jpmchase.net'
String canaryAction = this.promptCanaryAction()

if((canaryAction!="SKIP"){
  Sting containerImage= steps.env.CANARY_IMAGE_URI

  def authDetails = this.loadAuthDetails(config.environment)
  def deploymentDetails= this.loadDeploymentDetails(canaryAction,config.environment)

  String changeTicket= null;

  if(config.environment.toLowerCase()=='PROD'){

    changeTicket= domain.goodToGo?.replace('servicenow=','')
    steps.echo "Snow CHG# is: ${changeTicket}"
  }

  if(canaryAction=='CREATE'){

  this.createCanary(steps,authDetails,deploymentDetails,containerImage,canaryApiUrl,changeTicket)

  }else if(canaryAction =='UPDATE'){
  this.updateCanary(steps,authDetails,deploymentDetails,containerImage,canaryApiUrl,changeTicket)
  }else if(canaryAction=='DELETE'){

  this.deleteCanary(steps,authDetails,deploymentDetails,canaryApiUrl,changeTicket)
  }

}
}
}


def getImageUri(){
{steps,domain,config ->
steps.env.CANARY_IMAGE_URI= this.parseImageDetailsFromOutput()
steps.echo "Image URL : ${steps.env.CANARY_IMAGE_URI}"
}
}

private String parseImageDetailsFromOutput(){

try{
String content = readFile("skaffoldBuild.out")
Object skaffoldBuildOutput= new JsonSlurper().parseText(content);
String imageUri = skaffoldBuildOutput.build.get(0).tag;
String digest=iamgeuri.subString(imageUri.lastIndexOf('@')+1)
Sting imageName=skaffoldBuildOutput.builds.get(0).imageName;
String newURI= imageName.replace("container-publish","container-read");
String containerImage= newURI +'@'+digest;
return containerImage
}
catch(any){
println "failed in parsing container image from skaffold build output."
println "Contact canary Team (Platform_services-canayr@restricted.chase.com)" to resolve this issue
throw new Exception("caught exception: ${any}")
}
}


private String askforCanaryAccessToken(){

stage("prompt canary Access Token"){

String defaultAction= "Input access token"
timeout(time:60,unit:'MINUTES'){

String canaryAction= input(
message: "No auth-prod.yaml file found ,\n please input access token for canary control API (e.g using curl command"),
parameters: [
text(description: "access token for canary contorl Api ",name:"access_token")
]
)
println "Canary Access token is : ${canaryAction.access_token}"
return canaryAction.access_token
}
}
}

private String promptCanaryAction(){

stage("promptCanaryAction")
String defaultAction="CREATE"
try{
timeout(time:60 ,unit:'MINUTES'){
String canaryAction = input(
    message : "choose canary action to perform"
    parameters : [Choice(
       description:"choose canary action to perform",
       name: "actionToPerform",
       choices:"CREATE\nUPDATE\nDELETE\nSKIP"

    )]
    println "Canary action to perform is : ${canaryAction}"
    return canaryAction


}

}
catch(any){

println " NO Action Selected . Error : ${any}.using defaultAction : ${defaultAction}"
return defaultAction;
}

}
}