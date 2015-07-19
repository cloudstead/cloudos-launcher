ClaimDataProcessorService = function() {
	BaseDataProcessorService.call();
};

ClaimDataProcessorService.prototype = new BaseDataProcessorService();

ClaimDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var claimData = this.extractFields(dataToProcess, {});
	$.extend(extensionData, claimData);
};
