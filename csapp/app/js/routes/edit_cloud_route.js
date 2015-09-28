App.EditCloudRoute = App.ProtectedRoute.extend({
	controllerName: 'add_cloud',

	model: function(params) {
		return API.get_cloud(params.cloud_name);
	},

	setupController: function(controller, model) {
		this._super( controller, model);
		var self = this;
		API.loadAvailableVendors().then(function(response){
			var m = App.CloudModel.createNewFromData(model);
			m.availableVendors = response;
			controller.set("model", m);
		}, function(reason){
			if (reason.status === 403) {
				self.send("handleForbiddenResponse");
			}
		});
		controller.set("isEdit", true);
	},

	renderTemplate: function() {
		this.render('add_cloud');
	},
});
