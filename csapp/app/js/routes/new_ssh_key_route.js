App.NewSshKeyRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.SshKeyModel.createNewEmpty();
	},

	actions: {
		doTransitionToPreviuosRoute: function() {
			this.transitionTo("add_cloudstead");
		}
	}
});
