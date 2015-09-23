DNSDataProcessorService = function() {
	BaseDataProcessorService.call();
};

DNSDataProcessorService.prototype = new BaseDataProcessorService();

DNSDataProcessorService.prototype.appName = "cloudos-dns";
