GeoIPDataProcessorService = function() {
	BaseDataProcessorService.call();
};

GeoIPDataProcessorService.prototype = new BaseDataProcessorService();

GeoIPDataProcessorService.prototype.appName = "geoip";
