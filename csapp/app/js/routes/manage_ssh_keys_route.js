App.ManageSshKeysRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.SshKeyModel.getAll();
	}
});
