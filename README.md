# xml_json_test

1. Project created in VScode and built with Marven.

2. Dependicies (located in pom.xml): 
	
	org.json (https://mvnrepository.com/artifact/org.json/json)

	org.everit.json (https://mvnrepository.com/artifact/org.everit.json/org.everit.json.schema).

3. Aguments to pass and how:
	
	Arguments are located in .vscode (which is generated when the program is built) -> launch.json

	To convert a json file to xml file: pass json file then --xml command. [file].json --xml
		If successful, output.xml is created in root of project.

	To convert a xml file to json file: pass xml file then --json. [file].xml --json
		If successful, output.json is created in root of project.

	To validate json/xml file against scheme: pass file then --schema. [file].[json | xml] --schema.

Important note: 
  json schema located at "/src/main/java/com/test/schema.json"

	json schema makes every member required and postal codes can be either string or numbers. 
	I was not sure if I needed to restrict it
	to numbers only or not since other countries have a mixture of letters and numbers.
	
  xml schema located at ""./src/main/java/com/test/schema.xsd"

	xml schema created with the structure of "/src/main/java/com/test/ab.xml" in mind. 
	Was not sure which members of the contact object 
        were optional so I made everything but "region" required.
	
	
