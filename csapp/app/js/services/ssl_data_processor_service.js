SSLDataProcessorService = function() {
	BaseDataProcessorService.call();
};

SSLDataProcessorService.prototype = new BaseDataProcessorService();

SSLDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var sslData = this.extractFields(dataToProcess, {});

	var fieldNames = [];

	for (var fieldName in sslData) {
		fieldNames.push(fieldName);
	}

	fieldNames.forEach(function(fieldName){

		var baseFolder = ouputFolder;
		var currentFolder = ouputFolder;

		sslData[fieldName].fileNameData.folders.forEach(function(folder, index) {
			if (index === 0) {
				currentFolder = ouputFolder.folder(folder);
			} else {
				currentFolder = currentFolder.folder(folder);
			}
		});
		currentFolder.file(sslData[fieldName].fileNameData.fileName, sslData[fieldName].fileData);

	});
};
