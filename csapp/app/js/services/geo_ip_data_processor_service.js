GeoIPDataProcessorService = function() {
	BaseDataProcessorService.call();
};

GeoIPDataProcessorService.prototype = new BaseDataProcessorService();

GeoIPDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var geoipData = this.extractFields(dataToProcess, {});

	var fieldNames = [];

	for (var fieldName in geoipData) {
		fieldNames.push(fieldName);
	}

	fieldNames.forEach(function(fieldName){

		var baseFolder = ouputFolder;
		var currentFolder = ouputFolder;

		geoipData[fieldName].fileNameData.folders.forEach(function(folder, index) {
			if (index === 0) {
				currentFolder = ouputFolder.folder(folder);
			} else {
				currentFolder = currentFolder.folder(folder);
			}
		});

		currentFolder.file(geoipData[fieldName].fileNameData.fileName, geoipData[fieldName].fileData);

	});
};
