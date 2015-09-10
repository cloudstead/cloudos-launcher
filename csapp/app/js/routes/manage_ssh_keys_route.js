App.ManageSshKeysRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_ssh_keys();
	}
});
