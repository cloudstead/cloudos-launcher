App.ProtectedRoute = Ember.Route.extend({
	beforeModel: function(transition) {
		var loginController = this.controllerFor('login');
		loginController.set('previousTransition', transition);
		if (!LauncherStorage.hasLoggedInUser()){
			this.transitionTo('login');
		}
	},

	// actions: {
	// 	doTransitionToPreviuosRoute: function() {
	// 		var loginController = this.controllerFor('login');

	// 		var previousTransition = loginController.get('previousTransition');

	// 		previousTransition.retry();
	// 	}
	// }
});
