# UploadProcessor

Simple Spring Boot application to process HTTP uploads by executing an external command for each uploaded file. It provides a REST API and a SPA to upload files and to display the status of the executed commands.

Usage scenario examples:
* Compressing or uncompressing file upload before storing them on a disk
* Processing uploaded images with ImageMagick
* Deployment of a remote server

## Building and Running

### Requirements

- Java 1.7 or higher
- Gradle 2.3 or higher

### Building

Run `gradle test assemble` to build the project and generate the artifacts in `build/distributions`.

### Running

Run `gradle run` execute to Spring Boot Application from the project folder.

The distribution artifacts contain start scripts to run the application after the deployment in other locations.
 
## REST API

__Upload a file__

`POST /uploads`

Sample request with curl

`curl --form file=@localfilename http://localhost:8090/uploads`

Sample response

```javascript
{
	"id":1,
	"creationDate":1446924073850,
	"command":["test.sh","input.txt"],
	"exitValue":-1,
	"output":"",
	"workingDirectory":"."
	"status":"PENDING",
}
```

__List all uploaded files and executed processes__

`GET /uploads`

__Show details of a uploaded file and executed process__

`GET /uploads/{id}`

## Single Page Application

The page is accessible at the root url of the running service. e.g. http://localhost:8090/

It is implemented with AngularJS and Twitter Bootstrap. The sources are in `/src/main/resources/static`.

## Configuration

See `/src/main/resources/application.properties` for all configuration options.

## Copyright and License

Copyright 2015 JK. Code released under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0) license.

