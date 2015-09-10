App.ManageSshKeysRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_ssh_keys();
	},

	actions: {
		doTransitionToNewSshKey: function() {
			var loginController = this.controllerFor('login');
			var NewSshKeyController = this.controllerFor('new_ssh_key');
			NewSshKeyController.set("previousTransition", loginController.get("previousTransition"));

			this.transitionTo("new_ssh_key");
		}
	}
});
