App.ProtectedRoute = Ember.Route.extend({
	beforeModel: function(transition) {
		var loginController = this.controllerFor('login');
		loginController.set('previousTransition', transition);
		if (!LauncherStorage.hasLoggedInUser()){
			this.transitionTo('login');
		}
	},

	actions: {
		doTransitionTo: function(route_name) {
			var loginController = this.controllerFor('login');
			var controller = this.controllerFor(route_name);
			controller.set("previousTransition", loginController.get("previousTransition"));

			this.transitionTo(route_name);
		},
	},

	// actions: {
	// 	doTransitionToPreviuosRoute: function() {
	// 		var loginController = this.controllerFor('login');

	// 		var previousTransition = loginController.get('previousTransition');

	// 		previousTransition.retry();
	// 	}
	// }
});
