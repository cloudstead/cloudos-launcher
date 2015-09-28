App.ManageSshKeysRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_ssh_keys();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("new_ssh_key");
		}
	},

	setupController: function(controller, model) {
		controller.set("model", App.SshKeyModel.createFromArray(model));
	}

});
