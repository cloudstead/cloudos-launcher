App.ProtectedRoute = Ember.Route.extend({
	beforeModel: function(transition) {
		var loginController = this.controllerFor('login');
		loginController.set('previousTransition', transition);
		if (!LauncherStorage.hasLoggedInUser()){
			this.transitionTo('login');
		}
	},

	logout: function(){
		LauncherStorage.removeLogin();
		// error substate and parent routes do not handle this error
		return this.transitionTo('login');
	},

	actions: {
		doTransitionTo: function(route_name) {
			var loginController = this.controllerFor('login');
			var controller = this.controllerFor(route_name);
			controller.set("previousTransition", loginController.get("previousTransition"));

			this.transitionTo(route_name);
		},

		error: function(error, transition) {
			if (error && error.status === 403) {
				return this.logout();
			}
			return true;
		},

		handleForbiddenResponse: function() {
			return this.logout();
		},

		handleErrorResponse: function(reason) {
			console.log("ERROR HANDLED: ", reason);
			if (reason.status === 403) {
				this.send("handleForbiddenResponse");
			} else {
				$.notify(
					"An unexpected error occured.",
					{ position: "bottom-right", autoHideDelay: 10000, className: 'error' }
				);
			}
		},
	},
});
