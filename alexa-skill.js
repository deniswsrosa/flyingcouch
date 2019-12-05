{
    "interactionModel": {
    "languageModel": {
        "invocationName": "flying couch",
            "intents": [
            {
                "name": "AMAZON.FallbackIntent",
                "samples": []
            },
            {
                "name": "AMAZON.CancelIntent",
                "samples": []
            },
            {
                "name": "AMAZON.HelpIntent",
                "samples": []
            },
            {
                "name": "AMAZON.StopIntent",
                "samples": []
            },
            {
                "name": "AMAZON.NavigateHomeIntent",
                "samples": []
            },
            {
                "name": "salesIntent",
                "slots": [],
                "samples": [
                    "are there any flight tickets on sale",
                    "what are the flight deals",
                    "give the flight deals "
                ]
            },
            {
                "name": "getDefaultAirportIntent",
                "slots": [],
                "samples": [
                    "what is my airport",
                    "what is my default airport"
                ]
            },
            {
                "name": "setDefaultAirportIntent",
                "slots": [
                    {
                        "name": "location",
                        "type": "AMAZON.Airport"
                    }
                ],
                "samples": [
                    "my airport is {location}",
                    "my default airport is {location}",
                    "set my default airport to {location}"
                ]
            }
        ],
            "types": []
    }
}
}