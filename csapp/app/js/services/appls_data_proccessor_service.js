ApplsDataProcessorService = function() {
	BaseDataProcessorService.call();
};

ApplsDataProcessorService.prototype = new BaseDataProcessorService();

ApplsDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	for(var app in dataToProcess){
		var data = this.extractFields(dataToProcess[app], {});
		ouputFolder.file(app + ".json", JSON.stringify(data));
	}
};