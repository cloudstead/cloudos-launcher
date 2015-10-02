App.ManageSshKeysRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.SshKeyModel.getAll();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("new_ssh_key");
		}
	},

});
