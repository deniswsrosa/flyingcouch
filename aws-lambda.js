/* eslint-disable  func-names */
/* eslint-disable  no-console */

const Alexa = require('ask-sdk');
var http = require('http');

const LaunchHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        return request.type === 'LaunchRequest';
    },
    handle(handlerInput) {
        return handlerInput.responseBuilder
            .speak("Welcome to Flying Couch, what do you want to do?")
            .withShouldEndSession(false)
            .getResponse();
    },
};

const SetDefaultAirportHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        return request.type === 'IntentRequest'
            && request.intent.name === 'setDefaultAirportIntent';
    },
    async handle(handlerInput) {
    var userId = handlerInput.requestEnvelope.context.System.user.userId;
    var airport = handlerInput.requestEnvelope.request.intent.slots.location.value;
    var url = "/api/user/saveUserAirport?alexaId="+userId+"&airport="+airport;

    const response = await httpGet(url);
    return handlerInput.responseBuilder
        .speak(response.message)
        .withShouldEndSession(false)
        .getResponse();
},
};

const GetDefaultAirportHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        return request.type === 'IntentRequest'
            && request.intent.name === 'getDefaultAirportIntent';
    },
    async handle(handlerInput) {
    var userId = handlerInput.requestEnvelope.context.System.user.userId;
    var url = "/api/user/getMyAirport?alexaId="+userId;
    const response = await httpGet(url);
    return handlerInput.responseBuilder
        .speak(response.message)
        .withShouldEndSession(false)
        .getResponse();
},
};


const SalesIntentHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        return request.type === 'IntentRequest'
            && request.intent.name === 'salesIntent';
    },
    async handle(handlerInput) {
    var userId = handlerInput.requestEnvelope.context.System.user.userId;
    var url = "/api/user/getFlightsOnSales?alexaId="+userId;
    const response = await httpGet(url);
    return handlerInput.responseBuilder
        .speak(response.message)
        .withShouldEndSession(false)
        .getResponse();
},
};


const HelpHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        return request.type === 'IntentRequest'
            && request.intent.name === 'AMAZON.HelpIntent';
    },
    handle(handlerInput) {
        return handlerInput.responseBuilder
            .speak(HELP_MESSAGE)
            .reprompt(HELP_REPROMPT)
            .getResponse();
    },
};

const ExitHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        return request.type === 'IntentRequest'
            && (request.intent.name === 'AMAZON.CancelIntent'
                || request.intent.name === 'AMAZON.StopIntent');
    },
    handle(handlerInput) {
        return handlerInput.responseBuilder
            .speak(STOP_MESSAGE)
            .getResponse();
    },
};

const SessionEndedRequestHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        return request.type === 'SessionEndedRequest';
    },
    handle(handlerInput) {
        console.log(`Session ended with reason: ${handlerInput.requestEnvelope.request.reason}`);

        return handlerInput.responseBuilder.getResponse();
    },
};

const ErrorHandler = {
    canHandle() {
        return true;
    },
    handle(handlerInput, error) {
        console.log(`Error handled: ${error.message}`);

        return handlerInput.responseBuilder
            .speak("Sorry, I could not understand.")
            .reprompt("Sorry,I could not understand.")
            .getResponse();
    },
};


const SKILL_NAME = 'Flying Couch';
const URL = "YOUR URL HERE"
const HELP_MESSAGE = 'You can set your default airport, ask me for flights on sales or you can say exit... What can I help you with?';
const HELP_REPROMPT = 'What can I help you with?';
const STOP_MESSAGE = 'See you soon!';




const skillBuilder = Alexa.SkillBuilders.standard();


function httpGet(query) {
    return new Promise(((resolve, reject) => {
        const request = http.request(URL+query, (response) => {
            response.setEncoding('utf8');
    let returnData = '';

    response.on('data', (chunk) => {
        returnData += chunk;
});

    response.on('end', () => {
        resolve(JSON.parse(returnData));
});

    response.on('error', (error) => {
        reject(error);
});
});
    request.end();
}));
}


exports.handler = skillBuilder
    .addRequestHandlers(
        LaunchHandler,
        SetDefaultAirportHandler,
        GetDefaultAirportHandler,
        SalesIntentHandler,
        HelpHandler,
        ExitHandler,
        SessionEndedRequestHandler
    )
    .addErrorHandlers(ErrorHandler)
    .lambda();
