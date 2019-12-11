# flyingcouch

A demos of an alexa skill using Couchbase and AWS Lambda


## Setup

If you are not familiar with Alexa Skills, I highly recommend you to watch this video first https://www.youtube.com/watch?v=BB3wwxgqPOU


* In Couchbase, go to Settings -> Sample Buckets, select "travel-sample" and click on "Load Sample Data"
* Clone this repository
* Go to the java application and open the Database class. Adjust the IP address of your database in the following line:
`cluster = CouchbaseCluster.create("127.0.0.1");`
* Deploy your the java app by runnning on the root directory de following command:
`mvn spring-boot:run`
* Your application should be acessible to your lambda function. The easiest way to do that is to use like Local Tunnel (https://localtunnel.github.io/www/):
`npm install -g localtunnel`

`lt --port 8080`


 Once you run the last command, it will print a URL which you can use to access your local application from outside of your network
* Create a new Alexa Skill and give a name to it
* Under the option "JSON Editor" paste the content of the file "alexa-skill.js"
* Save your skill
* On AWS, go to the Lambda service and click on "Create function"
* Select "Browse serverless app repository" and choose a skill *called alexa-skills-kit-nodejs-howtoskill*
* Delete all the content of your lambda function and paste the content of the file "aws-lambda.js" 
* Update the following line in your function with the URL of the java application
`const URL = "YOUR URL HERE"`
* Save your function
* Copy the ARN of your lamda function and paste it on the field "Default Region" under the menu "Endpoint" of your alexa skill
* Save your Alexa Skill
* Go back to one of the intents and click on "Build Model"
* Go to the Test tab and change the item "Skill testing is enabled in:" to *Development*
* Say "Alexa, open 'NAME_OF_YOUR_SKILL'"
* Use the skill.





