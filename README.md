Routing app
===========

## How to build

Assuming you've already unzipped the zip..

`cd routing`

`./mvnw clean package`

## How to start the app

Assuming you have java on your PATH and nothing is running on port 8080

`java -jar target/routing.jar`

## How to run the integration tests

Assuming the app is running - open another console..

`./mvnw test -Dtest=RoutingRestExercise`

## How to test manually

Again - assuming the app is running..

Then test route e.g. from CZE to SVK

`curl http://localhost:8080/routing/CZE/SVK`
