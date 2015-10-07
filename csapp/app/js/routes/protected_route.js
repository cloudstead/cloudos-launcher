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

	generalErrorTranslation: function () {
		return getFirstTranslation()['error']['general'];
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
			Notify.globally(this.generalErrorTranslation().forbidden);

			return this.logout();
		},

		handleErrorResponse: function(reason) {
			if (reason.status === 403) {
				this.send("handleForbiddenResponse");
			} else {
				Notify.globally(this.generalErrorTranslation().unknown);
			}
		},
	},
});
