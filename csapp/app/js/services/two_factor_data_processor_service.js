TwoFactorDataProcessorService = function() {
	BaseDataProcessorService.call();
};

TwoFactorDataProcessorService.prototype = new BaseDataProcessorService();

TwoFactorDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var twoFactorData = this.extractFields(dataToProcess, {});
	var twoFactorFolder = ouputFolder.folder("two_factor");

	twoFactorFolder.file("init.json", JSON.stringify(twoFactorData));

	$.extend(extensionData, {
		authy: twoFactorData
	});
};
