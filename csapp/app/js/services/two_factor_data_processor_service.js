TwoFactorDataProcessorService = function() {
	BaseDataProcessorService.call();
};

TwoFactorDataProcessorService.prototype = new BaseDataProcessorService();

TwoFactorDataProcessorService.prototype.appName = "two_factor";
