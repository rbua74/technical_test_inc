# Challenge
The application will consume payments from a Kafka stream and will process according "online" or "offline" type (topic). Online payments will be validated first through a rest call. 
When a payment is not valid the payment is logged as an error through another rest call. No custom exceptions thrown during the process. Every successful payment is saved in the repository. 
In case there is an account linked to the payment (for online there should be one, for offline I have considered it is optional) the account should be updated with the payment timestamp 
(as the last payment date).

## :computer: How to execute
- After cloning project just run _mvn clean install_ in the terminal.
- Ensure the environment is ready (provided _docker-compose.yml_ file is executed with _up_ command).
- Start the spring boot application (by running TechnicalTestIncApplication.java, _ie_.)
- Access localhost:9000 in a browser and press START TEST button.
- Logs can be read in the console as well as the tables in the provided postgres database (dockerized). With the browser we can also press the button for logged errors.

## :memo: Notes
Some commands to use:
- docker-compose up -d
- $ winpty docker exec -it b7aa325473c1 bash 
- root@b7aa325473c1:/# psql -U tech payments

## :pushpin: Things to improve
If I'd had more time:
- I would have analyzed the timeout issues.
- I would have added a kafka integration tests.
- I would have refactored in order to clean code and simplified solution.
- I would have used the docker-compose file within the application.
