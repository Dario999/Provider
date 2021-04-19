1.Add event from file event.json on endpoint: localhost:8080/api/events/add

2.Test odds change message:
-file: odds_change_message.json
-endpoint: localhost:8080/api/message/send

3.Test bet stop message:
-file: bet_stop_message.json
-endpoint: localhost:8080/api/message/send

4.Test fixture change message:
-file: fixture_change_message.json
-endpoint: localhost:8080/api/message/send

When sending post request on "/api/message/send" for each of the previous types of messages it compresses the message
and save in audit_message table.

We can get all the messages for specific event with id on "/api/message/event/{id}" where id represents the event id.
This return rar file with all the messages in it.
Also we can get a message by id on: "/api/message/{id}"
(both of these do not work in postman, only in browser)
