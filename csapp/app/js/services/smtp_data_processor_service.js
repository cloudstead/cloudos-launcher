SMTPDataProcessorService = function() {
	BaseDataProcessorService.call();
};

SMTPDataProcessorService.prototype = new BaseDataProcessorService();

SMTPDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var smtpData = this.extractFields(dataToProcess, {});
	var smtpFolder = ouputFolder.folder("email");

	smtpFolder.file("init.json", JSON.stringify(smtpData));
};
