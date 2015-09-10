App.LauncherRoute = App.ProtectedRoute.extend({
	actions: {
		doTransitionToLogin: function() {
			this.transitionTo("login");
		}
	}
});
