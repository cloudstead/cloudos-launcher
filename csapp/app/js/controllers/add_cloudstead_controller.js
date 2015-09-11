App.AddCloudsteadController = App.BaseObjectController.extend({
	cloudTypes: function() {
		console.log(this.get("model"));
		return API.get_cloud_types().data;
	}.property(),

	allClouds: function() {
		return App.CloudModel.getAll().map(function(cloud) {
			return cloud.name;
		});
	}.property(),

	selectedCloud: function() {
		var cloudName = this.get("cloud");
		return Ember.isEmpty(cloudName) ? App.CloudModel.createNewEmpty() : App.CloudModel.get(cloudName);
	}.property('cloud'),

	selectedCloudType: function() {
		var cloudTypeName = this.get("selectedCloud.vendor");
		return this.get("cloudTypes").find(function(cloudType) {
			return cloudType.id === cloudTypeName;
		});
	}.property('selectedCloud'),

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
			this.send("doTransitionTo", "add_cloud");
		},

		doNewConfig: function() {
			this.send("doTransitionTo", "addlaunch");
		},

		doLaunch: function() {
			console.log("Do Launch");
		},

		doCancel: function() {
			this.doTransitionToPreviuosRoute();
		},

		doNewSsh: function() {
			this.send("doTransitionTo", "new_ssh_key");
		},

		doManageSsh: function() {
			this.send("doTransitionTo", "manage_ssh_keys");
		}
	},
});
