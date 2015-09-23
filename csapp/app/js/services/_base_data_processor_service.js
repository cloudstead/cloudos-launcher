BaseDataProcessorService = function() {
};

BaseDataProcessorService.prototype.extractFields = function(dataObj, initObj) {
	var retObj = initObj;
	var self = this;

	if (!Ember.isNone(dataObj["fields"])) {

		dataObj["fields"].forEach( function(field) {
			if (field.isTimeOfDay) {
				var valArray = field.value.split(" ");
				var valTimeArray = valArray[0].split(":");
				var hours = parseInt(valTimeArray[0]);
				var minutes = parseInt(valTimeArray[1]);

				hours = valArray[valArray.length - 1] === "PM" ? hours + 12 : hours;
				retObj[field.elementId] = "" + minutes + " " + hours + " * * * *";
			} else {
				var value = field.get("value");
				if(field.get("type").typeName === "password"){
					var hash = self.encryptData(value);
					retObj[field.elementId] = Ember.isNone(hash) ? value : hash;
				}else { retObj[field.elementId] = value; }
			}
		});
	}


	if (!Ember.isNone(dataObj["files"])) {

		dataObj["files"].forEach( function(field) {
			var aditionalInfoArray = field.get("type.additional").split("/");
			var fileNameData = {
				folders: aditionalInfoArray.slice(0, aditionalInfoArray.length -1),
				fileName: aditionalInfoArray[aditionalInfoArray.length -1]
			};
			retObj[field.elementId] = {
				fileData: field.get("fileData"),
				fileNameData: fileNameData
			};
		});

	}

	return retObj;
};


BaseDataProcessorService.prototype.findFieldIn = function(fieldsArray, fieldName) {
	return fieldsArray.find(function(field){
		var elementIdArray = field.get("elementId").split("/");
		var elementId = elementIdArray[elementIdArray.length - 1];
		return elementId === fieldName;
	});
};

BaseDataProcessorService.prototype.encryptData = function(data) {
	var salt, hash;
	try{
		salt = TwinBcrypt.genSalt(9);
	}catch(err){
		console.log("Generate salt error: ", err);
		salt = "";
	}
	try{
		hash = TwinBcrypt.hashSync( data, salt);
	}catch(err){
		console.log("Generate hash error: ", err);
		hash = undefined;
	}
	return hash;
};

BaseDataProcessorService.prototype.appName = "cloudos-dns";

BaseDataProcessorService.prototype.createTabFiles = function(folder, file, newFolder, fieldData) {
	var fd = {};

	fieldData[folder][file]["fields"].forEach(function(field) {
		var nfd = {};
		nfd[field.elementId] = field.value;
		fd = $.extend(nfd, fd);
	});

	fd["id"] = file;

	newFolder.file(file, JSON.stringify(fd));
};

BaseDataProcessorService.prototype.getFieldPath = function(field) {
	var fieldDataArray = field.nameData;
	var fieldAppName = this.appName;
	var fieldCategory = "init";
	var fieldKey = "";

	if (fieldDataArray.length === 3) {
		fieldAppName = fieldDataArray[0];
		fieldCategory = fieldDataArray[1];
		fieldKey = fieldDataArray[2];
	} else if (fieldDataArray.length === 2) {
		fieldCategory = fieldDataArray[0];
		fieldKey = fieldDataArray[1];
	} else if (fieldDataArray.length === 1) {
		fieldKey = fieldDataArray[0];
	}

	return {
		appName: fieldAppName,
		category: fieldCategory
	};
};


BaseDataProcessorService.prototype.createOrExtendFieldData = function(fieldData, field) {

		var fieldPath = this.getFieldPath(field);

		var fieldAppName = fieldPath.appName;
		var fieldCategory = fieldPath.category;

		// create data entry for the app or extend it if it already exists
		fieldData[fieldAppName] = fieldData[fieldAppName] === undefined ? {} : fieldData[fieldAppName];

		fieldData[fieldAppName][fieldCategory] = fieldData[fieldAppName][fieldCategory] === undefined ? {} : fieldData[fieldAppName][fieldCategory];

		fieldData[fieldAppName][fieldCategory]["fields"] = fieldData[fieldAppName][fieldCategory]["fields"] === undefined ? [] : fieldData[fieldAppName][fieldCategory]["fields"];

		fieldData[fieldAppName][fieldCategory]["fields"].push(field);

		return fieldData;

};

BaseDataProcessorService.prototype.writeFieldDataToFilesInFolder = function(fieldData, ouputFolder) {
	// create databags from filed data
	for (var folder in fieldData) {
		var newFolder = ouputFolder.folder(folder);
		for (var file in fieldData[folder]) {
			this.createTabFiles(folder, file, newFolder, fieldData);
		}
	}

};

BaseDataProcessorService.prototype.processTab = function(tab, ouputFolder) {

	var self = this;
	var fieldData = {};

	// Create data structure for fields in a tab.
	tab.tabFields.forEach(function(field) {
		fieldData = self.createOrExtendFieldData(fieldData, field);
	});

	console.log("FDATA: ", fieldData);

	self.writeFieldDataToFilesInFolder(fieldData, ouputFolder);

};

BaseDataProcessorService.prototype.processTabGroups = function(tabGroups, ouputFolder) {
	var self = this;
	for (var tabGroup in tabGroups) {
		self.processTab(tabGroups[tabGroup].tab, ouputFolder);
	}
};

BaseDataProcessorService.prototype.processMainFields = function(dataToProcess, ouputFolder, extensionData) {
	// var self = this;
	// var dnsData = {
	// 	id: "init",
	// 	admin: {
	// 		name: "cloudos-dns-admin",
	// 		password: "bcrypted-password"
	// 	}
	// };

	// dnsData = this.extractFields(dataToProcess, dnsData);

	var mainFieldsData = {};
	var fieldPrefix = this.appName + "/init";

	if (dataToProcess["field_prefix"] !== undefined) {
		fieldPrefix = dataToProcess["field_prefix"];
	}

	var pathArray = fieldPrefix.split("/");
	var folderName = pathArray[0];
	var fileName = pathArray[1] +".json";

	mainFieldsData.id = pathArray[1];

	var baseFolder = ouputFolder.folder(folderName);

	if (dataToProcess.hasOwnProperty("fields")){
		dataToProcess.fields.forEach(function(field){
			mainFieldsData[field.elementId] = field.value;
		});

		baseFolder.file(fileName, JSON.stringify(mainFieldsData));
	}

};

BaseDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var self = this;

	self.processMainFields(dataToProcess, ouputFolder, extensionData);

	self.processTabGroups(dataToProcess.tabGroups, ouputFolder);
};
