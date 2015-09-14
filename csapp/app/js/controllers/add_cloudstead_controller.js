App.AddCloudsteadController = App.BaseObjectController.extend({
	clouds: [],

	cloudTypes: function() {
		console.log(this.get("model"));
		return API.get_cloud_types().data;
	}.property(),

	allClouds: function() {
		return this.get("clouds").map(function(cloud) {
			return cloud.name;
		});
	}.property("clouds"),

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

	sshKeys: [],

	allSSHKeys: function() {
		return this.get("sshKeys").map(function(sshKey) {
			return sshKey.name;
		});
	}.property("sshKeys"),

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
