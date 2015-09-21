DNSDataProcessorService = function() {
	BaseDataProcessorService.call();
};

DNSDataProcessorService.prototype = new BaseDataProcessorService();

DNSDataProcessorService.prototype.createTabFiles = function(folder, file, newFolder, fieldData) {
	var fd = {};
	fieldData[folder][file].fields.forEach(function(field) {
		var nfd = {};
		nfd[field.elementId] = field.value;
		fd = $.extend(nfd, fd);
	});
	newFolder.file(file, JSON.stringify(fd));
};

DNSDataProcessorService.prototype.processTabs = function(tab, ouputFolder) {
	var self = this;
	var fieldData = {
		};
	tab.tabFields.forEach(function(field) {
			// dnsData[tab.tabName][field.elementId] = field.value
			console.log("FIELD: ", field);
			var fieldDataArray = field.nameData;
			var fieldAppName = "cloudos-dns";
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

			var info = {
				folder: fieldAppName,
				file: fieldCategory,
				field: field
			};

			fieldData[fieldAppName] = fieldData[fieldAppName] === undefined ? {} : fieldData[fieldAppName];

			fieldData[fieldAppName][fieldCategory] = fieldData[fieldAppName][fieldCategory] === undefined ? {} : fieldData[fieldAppName][fieldCategory];

			fieldData[fieldAppName][fieldCategory]["fields"] = fieldData[fieldAppName][fieldCategory]["fields"] === undefined ? [] : fieldData[fieldAppName][fieldCategory]["fields"];

			fieldData[fieldAppName][fieldCategory]["fields"].push(field);

		});


		for (var folder in fieldData) {
			var newFolder = ouputFolder.folder(folder);
			for (var file in fieldData[folder]) {
				self.createTabFiles(folder, file, newFolder, fieldData);
				// var fd = {};
				// fieldData[folder][file].fields.forEach(function(field) {
				// 	var nfd = {};
				// 	nfd[field.elementId] = field.value;
				// 	fd = $.extend(nfd, fd);
				// });
				// newFolder.file(file, JSON.stringify(fd));
			}
		}
};

DNSDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var self = this;
	var dnsData = {
		id: "init",
		admin: {
			name: "cloudos-dns-admin",
			password: "bcrypted-password"
		}
	};

	console.log("DATA TO PROCESS: ", dataToProcess);

	dnsData = this.extractFields(dataToProcess, dnsData);

	var prefixOutput = {};
	var mainFieldsData = {};

	if (dataToProcess["field_prefix"] !== undefined) {

		var pathArray = dataToProcess["field_prefix"].split("/");
		var folderName = pathArray[0];
		var fileName = pathArray[1] +".json";


		var baseFolder = ouputFolder.folder(folderName);

		dataToProcess.fields.forEach(function(field){
			mainFieldsData[field.elementId] = field.value;
		});

		baseFolder.file(fileName, JSON.stringify(mainFieldsData));
	}

	console.log("DNS DATA: ", dnsData);

	for (var tabGroup in dataToProcess.tabGroups) {
		console.log("TAB GROUP: ", dataToProcess.tabGroups[tabGroup]);
		var tab = dataToProcess.tabGroups[tabGroup].tab;
		console.log("TAB GROUP TAB: ", tab);

		// var fieldData = {
		// };

		self.processTabs(tab, ouputFolder);

		// tab.tabFields.forEach(function(field) {
		// 	// dnsData[tab.tabName][field.elementId] = field.value
		// 	console.log("FIELD: ", field);
		// 	var fieldDataArray = field.nameData;
		// 	var fieldAppName = "cloudos-dns";
		// 	var fieldCategory = "init";
		// 	var fieldKey = "";

		// 	if (fieldDataArray.length === 3) {
		// 		fieldAppName = fieldDataArray[0];
		// 		fieldCategory = fieldDataArray[1];
		// 		fieldKey = fieldDataArray[2];
		// 	} else if (fieldDataArray.length === 2) {
		// 		fieldCategory = fieldDataArray[0];
		// 		fieldKey = fieldDataArray[1];
		// 	} else if (fieldDataArray.length === 1) {
		// 		fieldKey = fieldDataArray[0];
		// 	}

		// 	var info = {
		// 		folder: fieldAppName,
		// 		file: fieldCategory,
		// 		field: field
		// 	};

		// 	fieldData[fieldAppName] = fieldData[fieldAppName] === undefined ? {} : fieldData[fieldAppName];

		// 	fieldData[fieldAppName][fieldCategory] = fieldData[fieldAppName][fieldCategory] === undefined ? {} : fieldData[fieldAppName][fieldCategory];

		// 	fieldData[fieldAppName][fieldCategory]["fields"] = fieldData[fieldAppName][fieldCategory]["fields"] === undefined ? [] : fieldData[fieldAppName][fieldCategory]["fields"];

		// 	fieldData[fieldAppName][fieldCategory]["fields"].push(field);

		// });

		// for (var folder in fieldData) {
		// 	var newFolder = ouputFolder.folder(folder);
		// 	for (var file in fieldData[folder]) {
		// 		var fd = {};
		// 		fieldData[folder][file].fields.forEach(function(field) {
		// 			var nfd = {};
		// 			nfd[field.elementId] = field.value;
		// 			fd = $.extend(nfd, fd);
		// 		});
		// 		newFolder.file(file, JSON.stringify(fd));
		// 	}
		// }


	// 	if (tab.tabName === "dyn") {

	// 		dnsData[tab.tabName] = {
	// 			account: this.findFieldIn(tab.tabFields, "dns.account").value,
	// 			user: this.findFieldIn(tab.tabFields, "dns.user").value,
	// 			password: this.findFieldIn(tab.tabFields, "dns.password").value,
	// 			zone: this.findFieldIn(tab.tabFields, "dns.zone").value,
	// 		};

	// 		$.extend(extensionData, {
	// 			dns: dnsData[tab.tabName]
	// 		});

	// 	} else if (tab.tabName === "external") {
	// 		var cloudosDNSFolder = ouputFolder.folder("cloudos-dns");
	// 		var djbDNSFolder = ouputFolder.folder(tab.tabName);

	// 		var djbJSON = {
	// 			id: "init",
	// 			allow_axfr: this.findFieldIn(tab.tabFields, "allow_axfr").value,
	// 		};
	// 		dnsData[tab.tabName] = true;
	// console.log("DNS DATA: ", dnsData);
	// 		djbDNSFolder.file("init.json", JSON.stringify(djbJSON));

	// 		cloudosDNSFolder.file("init.json", JSON.stringify(dnsData));
	// 	}

	}
};
