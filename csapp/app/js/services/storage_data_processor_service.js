StorageDataProcessorService = function() {
	BaseDataProcessorService.call();
};

StorageDataProcessorService.prototype = new BaseDataProcessorService();

StorageDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var storageData = this.extractFields(dataToProcess, {});

	$.extend(extensionData, storageData);
};
