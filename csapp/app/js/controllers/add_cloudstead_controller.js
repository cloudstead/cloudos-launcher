App.AddCloudsteadController = Ember.ObjectController.extend({
	cloudTypes: function() {
		console.log(this.get("model"));
		return API.get_cloud_types();
	}.property(),

	allClouds: function() {
		return this.get("cloudTypes").map(function(cloudType) {
			return cloudType["name"];
		});
	}.property("cloudTypes"),

	selectedCloudType: function() {
		var cloudName = this.get("cloud");
		return this.get("cloudTypes").find(function(cloudType) {
			return cloudType["name"] === cloudName;
		});
	}.property('cloud'),

	allRegions: function() {
		var regions = Ember.isNone(this.get("selectedCloudType.regions")) ?
			[] :
			this.get("selectedCloudType.regions").map(function(region) {
				return region["name"];
			});
		return regions;
	}.property("selectedCloudType"),

	allInstanceTypes: function() {
		var instaceTypes = Ember.isNone(this.get("selectedCloudType.instanceTypes")) ?
			[] :
			this.get("selectedCloudType.instanceTypes").map(function(instanceType) {
				return instanceType["name"];
			});
		return instaceTypes;
	}.property("selectedCloudType"),

	actions: {
		doCreate: function () {
			console.log("CLOUD: ", this.get("model.vendor"));
		},

		doNewCloud: function() {
			this.send("doTransitionToAddCloud");
		},

		doNewConfig: function() {
			this.send("doTransitionToAddLaunch");
		}
	},
});
