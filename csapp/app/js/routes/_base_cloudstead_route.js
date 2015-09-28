App.BaseCloudsteadRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudsteadModel.createNewEmpty();
	},

	loadRelatedData: function(controller, model) {
		this._super(controller, model);

		API.loadCloudTypes().then(function(cloudTypes) {
			controller.set("cloudTypes", cloudTypes);
		}, function(reason){
			controller.send("handleErrorResponse", reason);
		});

		API.get_clouds().then(function(clouds) {
			controller.set("clouds", clouds);
		}, function(reason){
			controller.send("handleErrorResponse", reason);
		});

		API.get_ssh_keys().then(function(sshKeys) {
			controller.set("sshKeys", sshKeys);
		}, function(reason){
			controller.send("handleErrorResponse", reason);
		});
	},
});
