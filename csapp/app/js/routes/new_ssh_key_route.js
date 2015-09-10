App.NewSshKeyRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.SshKeyModel.createNewEmpty();
	},
});
