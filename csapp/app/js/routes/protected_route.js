App.ProtectedRoute = Ember.Route.extend({
	beforeModel: function(transition) {
		if (!LauncherStorage.hasLoggedInUser()){
			var loginController = this.controllerFor('login');
			loginController.set('previousTransition', transition);
			this.transitionTo('login');
		}
	}
});
